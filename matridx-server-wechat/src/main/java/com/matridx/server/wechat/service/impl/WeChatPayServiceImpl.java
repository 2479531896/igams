package com.matridx.server.wechat.service.impl;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.util.RabbitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWeChatPayService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class WeChatPayServiceImpl implements IWeChatPayService {

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	
	// 商户ID
	@Value("${matridx.wechat.mchid:}")
	private String mchid;
	
	// API密钥
	@Value("${matridx.wechat.paternerkey:}")
	private String paternerkey;
	
	@Autowired
	IPayinfoService payinfoService;
	
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IWbcxService wbcxService;
	
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Autowired
	RedisUtil redisUtil;
	
	//微信下单api
	private final static String WX_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//微信支付查询api
	private final static String SELECT_WXPAY = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	final Logger log = LoggerFactory.getLogger(WeChatPayServiceImpl.class);
	
	/**
	 * 进行微信支付
	 * @param sjxxDto
	 * @param code
	 * @param wbcxDto
	 * @param request
     */
	@Override
	public String createWechatPayOrder(SjxxDto sjxxDto, String code, WbcxDto wbcxDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		log.info("调用JSAPI支付成功--------service ybbh:"+sjxxDto.getYbbh()+"-------  code:"+code);
		JSONObject result = new JSONObject();
		//只取第一页的送检检测项目
		SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto);
		if(t_sjxxDto==null){
			result.put("msg", "未获取到商品信息");
			return result.toString();
		}
		if("1".equals(t_sjxxDto.getFkbj())){
			result.put("msg", "商品已完成支付");
			return result.toString();
		}
		try {
			DBEncrypt dbEncrypt = new DBEncrypt();
			//获取openid
			WeChatUserModel userModel = WeChatUtils.getUserBaseInfoByLink(restTemplate, code, wbcxDto,redisUtil);
			if(userModel == null){
				result.put("msg", "未获取到openid");
				return result.toString();
			}
			String openid = userModel.getOpenid();
			if(StringUtil.isBlank(sjxxDto.getFkje())){
				result.put("msg", "未获取到付款金额");
				return result.toString();
			}
			//生成订单号
			if(StringUtil.isBlank(t_sjxxDto.getDdh())){
				t_sjxxDto.setDdh(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (int) (Math.random() * 90000 + 88888));
				//保存至微信服务端
				boolean isSuccess = sjxxService.update(t_sjxxDto);
				if(!isSuccess){
					result.put("msg", "订单号保存未成功");
					return result.toString();
				}
			}
			//转换金额
			int total_fee = new BigDecimal(sjxxDto.getFkje()).multiply(new BigDecimal("100")).intValue();
			//获取参数
			Map<String, String> parameterMap = new HashMap<>();
			parameterMap.put("appid", dbEncrypt.dCode(wbcxDto.getAppid()));//微信分配的公众账号ID
			parameterMap.put("mch_id", dbEncrypt.dCode(mchid));//微信支付分配的商户号
			parameterMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串，不长于32位。
			parameterMap.put("body", "matridx-test");//商品简单描述，该字段须严格按照规范传递
			parameterMap.put("out_trade_no", t_sjxxDto.getDdh());//商户系统内部的订单号,32个字符内、可包含字母
			parameterMap.put("total_fee", String.valueOf(total_fee));//订单总金额，单位为分
			parameterMap.put("spbill_create_ip", request.getRemoteAddr());//必须传正确的用户端IP,支持ipv4、ipv6格式
			parameterMap.put("notify_url", dbEncrypt.dCode(applicationurl)+"/wechat/pay/wxPayNotify");//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
			parameterMap.put("trade_type", "JSAPI");//H5支付的交易类型为MWEB
			parameterMap.put("openid", openid);//支付人的微信公众号对应的唯一标识
		
			//生成签名
			String sign = WXPayUtil.generateSignature(parameterMap, dbEncrypt.dCode(paternerkey));
			parameterMap.put("sign", sign);
			//组装xml
			String requestXML = WXPayUtil.mapToXml(parameterMap);
			//发送统一下单请求
			String xmlresult = restTemplate.postForObject(WX_PAY, requestXML, String.class);
			xmlresult = new String(xmlresult.getBytes("ISO-8859-1"), "UTF-8");
			//返回参数xml解析
			Map<String, String> map = WXPayUtil.xmlToMap(xmlresult);
			//判断是否成功
			String return_code = map.get("return_code");
			String result_code = map.get("result_code");
			if (return_code.contains("SUCCESS") && result_code.contains("SUCCESS")) {
				String prepay_id = map.get("prepay_id");//获取到prepay_id
				//获取参数
				Map<String, String> signParam = new HashMap<>();
				long timeStamp = WXPayUtil.getCurrentTimestamp();//获取时间戳前10位
				signParam.put("appId", dbEncrypt.dCode(wbcxDto.getAppid()));//调用接口提交的公众账号ID
				signParam.put("package", "prepay_id=" + prepay_id);//默认sign=WXPay
				signParam.put("nonceStr", WXPayUtil.generateNonceStr());//自定义不重复的长度不长于32位
				signParam.put("timeStamp", String.valueOf(timeStamp));//北京时间时间戳
				signParam.put("signType", "MD5");//签名方式
				//再次生成签名
				String signAgain = WXPayUtil.generateSignature(signParam, dbEncrypt.dCode(paternerkey));
				signParam.put("paySign", signAgain);
				//返回数据
				result.put("signParam", signParam);
			}else{
				String return_msg = map.get("return_msg");
				result.put("msg", return_msg);
				return result.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.put("msg", e.getMessage());
			return result.toString();
		}
		result.put("msg", "success");
		return result.toString();
	}
	
	/**
	 * 微信异步结果通知
	 * @param request
	 * @param response
     */
	@Override
	public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		log.info("调用异步结果通知 ------ service");
		try {
			BufferedReader reader = request.getReader();
	        String line = "";
	        Map<String, String> map = new HashMap<>();
	        String xml = "<xml><return_code><![CDATA[FAIL]]></xml>";
	        StringBuffer inputString = new StringBuffer();
	        while ((line = reader.readLine()) != null) {
	            inputString.append(line);
	        }
	        request.getReader().close();
	        log.info("异步结果通知返回的xml ------ "+inputString);
	        
	        PayinfoDto payinfoDto = new PayinfoDto();
	        payinfoDto.setZfid(StringUtil.generateUUID());
	        payinfoDto.setDylx("WX");//支付类型为微信
			payinfoDto.setDyjk("wxPayNotify");//调用接口名
	        if(inputString.toString().length()>0){
	        	log.info("接受微信报文不为空 ------ "+inputString);
				map = WXPayUtil.xmlToMap(inputString.toString());
				// payinfoDto.setHdxx(inputString.toString());
	        }else{
	        	//接受微信报文为空
	        	log.info("接受微信报文为空 ------ "+inputString);
	        	return;
	        }
	        if(map!=null){
	        	log.info("异步结果通知返回的map ------ "+map);
	        	if(map!=null && "SUCCESS".equals(map.get("result_code"))){
	        		log.info("异步结果通知返回的result_code ------ "+map.get("result_code"));
		        	//支付成功
		        	xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		        	//业务处理
					// String msg = JSONUtils.toJSONString(map);
					// payinfoDto.setHdxx(msg);
					String ddh = map.get("out_trade_no");//获订单号
					log.info("异步结果通知返回的订单号 ------ "+ddh);
					if(StringUtil.isBlank(ddh)) {
						payinfoDto.setJg("0");
						payinfoService.insertPayinfoDto(payinfoDto);
						return;
					}
					SjxxDto sjxxDto = new SjxxDto();
					sjxxDto.setDdh(ddh);
					String result_code = map.get("result_code");//判断交易结果
					if(result_code.contains("SUCCESS")){
						//获取系统当前时间
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date = df.format(new Date());
						//完成
						sjxxDto.setFkbj("1");
						sjxxDto.setFkrq(date);
						payinfoDto.setJg("1");
						
					}else{
						//未完成
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
					}
					payinfoService.insertPayinfoDto(payinfoDto);
					sjxxService.updateByDdh(sjxxDto);
					//付款成功，发送消息
					if("1".equals(sjxxDto.getFkbj())){
						log.info("付款成功，发送消息------fkbj: "+sjxxDto.getFkbj());
						sjxxDto = sjxxService.getDtoByDdh(sjxxDto);
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_RES.getCode() + JSONObject.toJSONString(sjxxDto));
					}
		        }else{
		        	//支付失败，业务处理
					payinfoDto.setJg("0");
					// String msg = JSONUtils.toJSONString(map);
					// payinfoDto.setHdxx(msg);
					payinfoService.insertPayinfoDto(payinfoDto);
		        	return;
		        }
	        }else{
	        	//转换后map为空
	        	payinfoService.insertPayinfoDto(payinfoDto);
	        	return;
	        }
	        //告诉微信端已经确认支付成功
	        response.getWriter().write(xml);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询微信支付结果
	 * @param request
	 * @param response
     */
	@Override
	public void selectPayNotify(SjxxDto sjxxDto, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
		wbcxDto = wbcxService.getDto(wbcxDto);
		
		PayinfoDto payinfoDto = new PayinfoDto();
        payinfoDto.setZfid(StringUtil.generateUUID());
        payinfoDto.setDylx("WX");//支付类型为微信
		payinfoDto.setDyjk("selectPayNotify");//调用接口名
		
		DBEncrypt dbEncrypt = new DBEncrypt();
		Map<String, String> parameterMap = new HashMap<>();
		parameterMap.put("appid", dbEncrypt.dCode(wbcxDto.getAppid()));//微信支付分配的公众账号ID
		parameterMap.put("mch_id", dbEncrypt.dCode(mchid));//微信支付分配的商户号
		parameterMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串，不长于32位。
		parameterMap.put("transaction_id", "");//微信的订单号，建议优先使用
		parameterMap.put("out_trade_no", sjxxDto.getDdh());//商户系统内部的订单号,32个字符内、可包含字母
		parameterMap.put("signType", "MD5");//签名方式
		
		try {
			//生成签名
			String sign = WXPayUtil.generateSignature(parameterMap, dbEncrypt.dCode(paternerkey));
			parameterMap.put("sign", sign);
			//组装xml
			String requestXML = WXPayUtil.mapToXml(parameterMap);
			//发送查询请求
			String xmlresult = restTemplate.postForObject(SELECT_WXPAY, requestXML, String.class);
			if(xmlresult == null){
				payinfoService.insertPayinfoDto(payinfoDto);
				return;
			}
			xmlresult = new String(xmlresult.getBytes("ISO-8859-1"), "UTF-8");
			// payinfoDto.setHdxx(xmlresult);
			//返回参数xml解析
			Map<String, String> map = WXPayUtil.xmlToMap(xmlresult);
			//判断查询是否成功
			String return_code = map.get("return_code");
			if(return_code.contains("SUCCESS")){
				//判断支付是否成功
				String trade_state = map.get("trade_state");
				//获取系统当前时间
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = df.format(new Date());
				// 判断交易结果
				switch (trade_state){
					case "SUCCESS": // 支付成功
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("1");
						sjxxDto.setFkrq(date);
						payinfoDto.setJg("1");
						// payinfoDto.setTzlx("SUCCESS");
						break;
					case "REFUND": // 转入退款
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
						// payinfoDto.setTzlx("REFUND");
						break;
					case "NOTPAY": // 待未支付
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
						// payinfoDto.setTzlx("NOTPAY");
						break;
					case "CLOSED": // 已关闭
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
						// payinfoDto.setTzlx("CLOSED");
						break;
					case "REVOKED": // 已撤销（付款码支付）
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
						// payinfoDto.setTzlx("REVOKED");
						break;
					case "USERPAYING": // 用户支付中（付款码支付）
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
						// payinfoDto.setTzlx("USERPAYING");
						break;
					case "PAYERROR": // 支付失败(其他原因，如银行返回失败)
						//付款标记 0：未付款 1：已付款 2：部分付款
						sjxxDto.setFkbj("0");
						payinfoDto.setJg("0");
						// payinfoDto.setTzlx("PAYERROR");
						break;
					default:
						break;
				}
			}
			sjxxService.updateByDdh(sjxxDto);
			//付款成功，发送消息
			if("1".equals(sjxxDto.getFkbj())){
				sjxxDto = sjxxService.getDto(sjxxDto);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(),RabbitEnum.PYIF_RES.getCode() + JSONObject.toJSONString(sjxxDto));
			}
			payinfoService.insertPayinfoDto(payinfoDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 进行微信支付(H5)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public String createWechatH5PayOrder(SjxxDto sjxxDto, WbcxDto wbcxDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		log.info("调用H5支付--------service ybbh:"+sjxxDto.getYbbh());
		//只取第一页的送检检测项目
		SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto);
		if(t_sjxxDto==null){
			result.put("msg", "未获取到商品信息");
			return result.toString();
		}
		if("1".equals(t_sjxxDto.getFkbj())){
			result.put("msg", "商品已完成支付");
			return result.toString();
		}
		try {
			DBEncrypt dbEncrypt = new DBEncrypt();
			if(StringUtil.isBlank(sjxxDto.getFkje())){
				result.put("msg", "未获取到付款金额");
				return result.toString();
			}
			//生成订单号
			if(StringUtil.isBlank(t_sjxxDto.getDdh())){
				t_sjxxDto.setDdh(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (int) (Math.random() * 90000 + 88888));
				//保存至微信服务端
				boolean isSuccess = sjxxService.update(t_sjxxDto);
				if(!isSuccess){
					result.put("msg", "订单号保存未成功");
					return result.toString();
				}
			}
			//转换金额
			int total_fee = new BigDecimal(sjxxDto.getFkje()).multiply(new BigDecimal("100")).intValue();
			//获取参数
			Map<String, String> parameterMap = new HashMap<>();
			parameterMap.put("appid", dbEncrypt.dCode(wbcxDto.getAppid()));//微信分配的公众账号ID
			parameterMap.put("mch_id", dbEncrypt.dCode(mchid));//微信支付分配的商户号
			parameterMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串，不长于32位。
			parameterMap.put("body", "matridx-test");//商品简单描述，该字段须严格按照规范传递
			parameterMap.put("out_trade_no", t_sjxxDto.getDdh());//商户系统内部的订单号,32个字符内、可包含字母
			parameterMap.put("total_fee", String.valueOf(total_fee));//订单总金额，单位为分
			parameterMap.put("spbill_create_ip", request.getRemoteAddr());//必须传正确的用户端IP,支持ipv4、ipv6格式
			parameterMap.put("notify_url", dbEncrypt.dCode(applicationurl)+"/wechat/pay/wxPayNotify");//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
			parameterMap.put("trade_type", "MWEB");//H5支付的交易类型为MWEB
			parameterMap.put("scene_info", "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"www.matridx.com\",\"wap_name\": \"订单支付\"}}");//场景信息
		
			//生成签名
			String sign = WXPayUtil.generateSignature(parameterMap, dbEncrypt.dCode(paternerkey));
			log.info("调用H5支付--------service 生成签名:"+sign);
			parameterMap.put("sign", sign);
			//组装xml
			String requestXML = WXPayUtil.mapToXml(parameterMap);
			//发送统一下单请求
			String xmlresult = restTemplate.postForObject(WX_PAY, requestXML, String.class);
			xmlresult = new String(xmlresult.getBytes("ISO-8859-1"), "UTF-8");
			//返回参数xml解析
			Map<String, String> map = WXPayUtil.xmlToMap(xmlresult);
			log.info("调用H5支付--------service 签名返回结果:"+map);
			//判断是否成功
			String return_code = map.get("return_code");
			String result_code = map.get("result_code");
			log.info("调用H5支付--------service 成功标识: return_code:"+return_code+" result_code:"+result_code);
			if (return_code.contains("SUCCESS") && result_code.contains("SUCCESS")) {
				String mweb_url = map.get("mweb_url");//获取到跳转链接mweb_url
				log.info("调用H5支付--------service 取得地址mweb_url:"+mweb_url);
				//支付完返回浏览器跳转的地址，如跳到查看订单页面
				String redirect_url = dbEncrypt.dCode(applicationurl)+"/pay/wxPayComplete";
				String redirect_urlEncode =  URLEncoder.encode(redirect_url,"utf-8");//对上面地址urlencode
				mweb_url = mweb_url + "&redirect_url=" + redirect_urlEncode;//拼接返回地址
				log.info("调用H5支付--------service 拼接返回地址mweb_url:"+mweb_url);
				//返回数据
				result.put("mweb_url", mweb_url);
			}else{
				String return_msg = map.get("return_msg");
				result.put("msg", return_msg);
				return result.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.put("msg", e.getMessage());
			return result.toString();
		}
		result.put("msg", "success");
		return result.toString();
	}

}
