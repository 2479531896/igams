package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.enums.PayTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.IPayService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.HttpUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IBankService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.util.Constant;
import com.matridx.server.wechat.util.MD5Utils;
import com.matridx.springboot.util.qrcode.QrCodeUtil;
import com.matridx.server.wechat.util.SelectPayResultThread;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.SignatureUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements IBankService, ApplicationRunner {
	
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Value("${matridx.bankpay.merid:}")
	private String merid;
	@Value("${matridx.bankpay.userid:}")
	private String userid;
	@Value("${matridx.bankpay.appid:}")
	private String appid;
	@Value("${matridx.bankpay.secret:}")
	private String secret;
	@Value("${matridx.bankpay.private_key:}")
	private String private_key;
	@Value("${matridx.bankpay.cmb_public_key:}")
	private String cmb_public_key;
	@Value("${matridx.bankpay.description:}")
	private String description;
	@Autowired
	IPayinfoService payinfoService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IWxyhService wxyhservice;
	@Autowired
	IWbcxService wbcxService;
	@Autowired
	IXxglService xxglService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	protected RestTemplate restTemplate;
	@Autowired
	private RedisUtil redisUtil;
	
	private Logger log = LoggerFactory.getLogger(BankServiceImpl.class);
	
	/**
	 * 生成订单银标码
	 * @param restTemplate
	 * @param request
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public String createOrderQRCode(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		// 接口调用保存
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setLxqf("send"); // 类型区分 发送 send;接收recv
		jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null)); // 调用时间
		jkdymxDto.setDydz("createOrderQRCode"); // 调用地址
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PAY.getCode());
		jkdymxDto.setSfcg("0");
		try {
			// json转换
			ObjectMapper mapper = new ObjectMapper();
			// 公共请求参数
			Map<String, String> submitMap = new HashMap<String, String>();
			submitMap.put("signMethod", "01"); // 签名方法 01：RSA2， 02：SM2 必填
			submitMap.put("version", "0.0.1"); // 版本号 必填
			submitMap.put("encoding", "UTF-8"); // 编码方式 必填
			// 业务参数
			Map<String, String> detailMap = new HashMap<String, String>();
			detailMap.put("merId", merid); // 招行分配的商户号 必填
			detailMap.put("userId", userid); // 收银员 必填
			detailMap.put("notifyUrl", Constant.NOTIFY_URL); // 交易通知地址 必填
			detailMap.put("tradeScene", Constant.TRADE_SCENE); // 交易场景 必填 
			detailMap.put("body", description); // 商品描述
			detailMap.put("payValidTime", Constant.PAY_TIME); // 支付有效时间
			detailMap.put("currencyCode", Constant.CURRENCY_CODE); // 交易币种
			detailMap.put("orderId", payinfoDto.getYbbh() + DateUtils.getTimeSuffix()); // 商户订单号 唯一 必填 ybbh+时间
			detailMap.put("txnAmt", String.valueOf(new BigDecimal(payinfoDto.getFkje()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP))); // 交易金额 单位为分 必填
			Map<String, String> map = new HashMap<>();
			if (payinfoDto.getYwlx().equals(BusinessTypeEnum.XG.getCode())){
				map.put("bh", payinfoDto.getYwid());
			}else{
				map.put("bh", payinfoDto.getYbbh());
			}
			map.put("je", payinfoDto.getFkje()); // 交易金额
			map.put("dd", detailMap.get("orderId")); // 支付订单编号
			map.put("fl", PayTypeEnum.PAY.getCode()); // 支付分类
			map.put("lx", payinfoDto.getYwlx()); // 区分送检和复检
			detailMap.put("mchReserved", mapper.writeValueAsString(map)); // 商户保留域 用于传输商户自定义数据，在支付结果查询和支付结果通知时原样返回（不上送则不返回此字段）
			submitMap.put("biz_content", mapper.writeValueAsString(detailMap));
			
			// 对待加签内容进行排序拼接
            String signContent = SignatureUtil.getSignContent(submitMap);
            // 私钥签名
            submitMap.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8")); // 签名 必填
            jkdymxDto.setNr(mapper.writeValueAsString(submitMap)); // 保存发送内容
            
            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", submitMap.get("sign"));
            apiSign.put("timestamp", timestamp);
            
            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();
            
            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);
            
            // 发送HTTP post请求
            Map<String, String> returnMap = HttpUtil.postForEntity(restTemplate, Constant.QRCODE_APPLY_URL, mapper.writeValueAsString(submitMap), apiHeader);
            // 返回结果验签
            boolean checkResult = checkSign(mapper.writeValueAsString(returnMap));
            if(!checkResult){
            	log.error("验证签名失败！");
				throw new BusinessException("msg", "验证签名失败！");
            }
			// 判断 respCode:SUCCESS/FAIL
			String respCode = returnMap.get("respCode");
			if(StringUtil.isNotBlank(respCode) && "SUCCESS".equals(respCode)) {
				@SuppressWarnings("unchecked")
				Map<String, String> returnCont = JSON.parseObject(returnMap.get("biz_content"), HashMap.class);
				// 保存订单数据
				PayinfoDto t_payinfoDto = new PayinfoDto();
				t_payinfoDto.setZfid(StringUtil.generateUUID());
				t_payinfoDto.setYwid(payinfoDto.getYwid());
				t_payinfoDto.setYwlx(payinfoDto.getYwlx());
				t_payinfoDto.setDdbh(detailMap.get("orderId"));
				t_payinfoDto.setPtddh(returnCont.get("cmbOrderId")); // 平台订单号,招行生成的订单号
				t_payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
				t_payinfoDto.setDyjk("createOrderQRCode");
				t_payinfoDto.setYwyxx(detailMap.get("mchReserved"));
				t_payinfoDto.setJg("0");
				t_payinfoDto.setFkje(payinfoDto.getFkje());
				t_payinfoDto.setLrry(payinfoDto.getLrry());
				t_payinfoDto.setZfjcxm(payinfoDto.getZfjcxm());
				if (StringUtil.isBlank(payinfoDto.getWbcxdm())){
					payinfoDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
				}

				log.error("createOrderQRCode wbcxdm:"+payinfoDto.getWbcxdm());
				Map<Object, Object> wbcxInfoList = redisUtil.hmget("WbcxInfo");
				for(Object wbcxInfoObj : wbcxInfoList.values()) {
					com.alibaba.fastjson.JSONObject wbcxInfo = JSON.parseObject(wbcxInfoObj.toString());
					if (wbcxInfo!=null){
						if (payinfoDto.getWbcxdm().equals(wbcxInfo.getString("wbcxdm"))){
							t_payinfoDto.setWbcxid(wbcxInfo.getString("wbcxid"));
							log.error("createOrderQRCode wbcxid:"+wbcxInfo.getString("wbcxid"));
							break;
						}
					}
				}
				// 订单发送时间 txnTime,格式为yyyyMMddHHmmss
				String cjsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(returnCont.get("txnTime")));
				t_payinfoDto.setCjsj(cjsj);
				payinfoService.insertPayinfoDto(t_payinfoDto); // 同步新增
				
				// 保存发送信息
				jkdymxDto.setQtxx(PayTypeEnum.PAY.getCode()); // 其他信息
				jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
				insertJkdymxDto(jkdymxDto); // 同步新增
				// 二维码地址
				String qrCode = returnCont.get("qrCode");
				log.error("createOrderQRCode qrCode:"+qrCode);
				return qrCode;
			}else {
				String errCode = String.valueOf(returnMap.get("errCode"));
				String respMsg = String.valueOf(returnMap.get("respMsg"));
				log.error("收款码申请失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
				throw new BusinessException("msg", "收款码申请失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
			}
		} catch (BusinessException e) {
			jkdymxDto.setQtxx(e.getMsg());
			insertJkdymxDto(jkdymxDto); // 同步新增
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e){
			jkdymxDto.setQtxx("生成订单异常！");
			insertJkdymxDto(jkdymxDto); // 同步新增
            log.error("生成订单异常！" + e.toString());
            throw new BusinessException("msg", "生成订单异常！");
        }
	}

	/**
	 * 根据链接生成二维码临时文件
	 * @param qrCode
	 * @return
     * @throws BusinessException 
     * @throws UnsupportedEncodingException 
	 */
	@Override
	public String generateQRCode(String qrCode) throws BusinessException {
		String fileName = System.currentTimeMillis() + ".jpg";
		if("0".equals(Constant.SETTING)) {
			qrCode = qrCode.replace("https://qr.95516.com", "http://payment-uat.cs.cmburl.cn");
		}
		String path = prefix + tempFilePath + BusTypeEnum.IMP_BANK_PAY_QRCODE.getCode();
		String filePath = QrCodeUtil.createQrCode(qrCode, path, fileName);
		try {
			filePath = URLEncoder.encode(URLEncoder.encode(filePath, "utf-8"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.toString());
			throw new BusinessException("msg", "链接转换异常！");
		}
		return filePath;
	}

	/**
	 * 二维码临时文件
	 * @param qrCode
	 * @return
	 * @throws BusinessException
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public String getQRCode(String qrCode) throws BusinessException {
		String fileName = System.currentTimeMillis() + ".jpg";
		String path = prefix + tempFilePath + BusTypeEnum.IMP_BANK_PAY_QRCODE.getCode();
		String filePath = QrCodeUtil.createQrCode(qrCode, path, fileName);
		try {
			filePath = URLEncoder.encode(URLEncoder.encode(filePath, "utf-8"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.toString());
			throw new BusinessException("msg", "链接转换异常！");
		}
		return filePath;
	}

	/**
	 * 微信统一下单
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public Map<String, Object> wechatPayOrder(RestTemplate restTemplate, PayinfoDto payinfoDto, String ipAddress) throws BusinessException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 接口调用保存
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setLxqf("send"); // 类型区分 发送 send;接收recv
		jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null)); // 调用时间
		jkdymxDto.setDydz("wechatPayOrder"); // 调用地址
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PAY.getCode());
		jkdymxDto.setSfcg("0");
        try {
        	ObjectMapper mapper = new ObjectMapper();
        	Map<String, String> requestPublicParams = new TreeMap<>();
            //公共请求参数
            requestPublicParams.put("version", "0.0.1"); // 版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8"); // 编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "01"); // 签名方法，固定为01，表示签名方式为RSA2(必传)
            //业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("merId", merid); // 商户号(必传)
            requestTransactionParams.put("userId", userid); // 收银员(必传)
            requestTransactionParams.put("currencyCode", Constant.CURRENCY_CODE); // 交易币种
            requestTransactionParams.put("notifyUrl", Constant.NOTIFY_URL); // 交易通知地址（必传）
            requestTransactionParams.put("body", description); // 商品描述（必传）
            requestTransactionParams.put("payValidTime", Constant.PAY_TIME); // 支付数据的有效时间，单位为秒，应不小于60
            if("public".equals(payinfoDto.getWxzflx())) {
            	// 公众号支付
            	requestTransactionParams.put("tradeType", "JSAPI"); // 交易类型 APP/MWEB/JSAPI（必传）
            	requestTransactionParams.put("tradeScene", "OFFLINE"); //交易场景
            	// requestTransactionParams.put("deviceInfo", "WEB");   //设备号 PC网页或公众号内支付请传"WEB"
            	requestTransactionParams.put("subAppId", "wx5d9f03bc14a17f46"); // 子公众帐号ID（必传）
            	// o6iX56llrPIb7FoyKFWkSjxoyOAQ
            	requestTransactionParams.put("subOpenId", "o6iX56llrPIb7FoyKFWkSjxoyOAQ"); // 用户在subAppId下的唯一标识（JSAPI必传）
                requestTransactionParams.put("spbillCreateIp", ipAddress); //终端IP（必传）
            }else if("mini".equals(payinfoDto.getWxzflx())) {
            	// 小程序支付
            	requestTransactionParams.put("tradeType", "JSAPI"); // 交易类型 APP/MWEB/JSAPI（必传）
            	requestTransactionParams.put("tradeScene", "OFFLINE"); //交易场景
            	requestTransactionParams.put("subAppId", "wx5e22039e998efb5e"); // 子公众帐号ID（必传）
            	requestTransactionParams.put("subOpenId", payinfoDto.getWxid()); // 用户在subAppId下的唯一标识（JSAPI必传）
                requestTransactionParams.put("spbillCreateIp", ipAddress); //终端IP（必传）必须传正确的用户端IP
            }else {
            	log.error("支付类型异常！" + payinfoDto.getWxzflx());
                throw new BusinessException("msg", "支付类型异常！" + payinfoDto.getWxzflx());
            }
            requestTransactionParams.put("txnAmt", String.valueOf(new BigDecimal(payinfoDto.getFkje()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP))); //交易金额,单位为分(必传)
            requestTransactionParams.put("orderId", payinfoDto.getYbbh() + DateUtils.getTimeSuffix()); // 商户订单号(必传)
            Map<String, String> map = new HashMap<String, String>();
            if (payinfoDto.getYwlx().equals(BusinessTypeEnum.XG.getCode())){
				map.put("bh", payinfoDto.getYwid());
			}else{
				map.put("bh", payinfoDto.getYbbh());
			}
			map.put("je", payinfoDto.getFkje());
			map.put("dd", requestTransactionParams.get("orderId"));
			map.put("fl", PayTypeEnum.PAY.getCode());
			map.put("lx", payinfoDto.getYwlx()); // 区分送检和复检
            requestTransactionParams.put("mchReserved", mapper.writeValueAsString(map)); // 商户保留域
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
            log.info("wechatPayOrder 加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));

            // 对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            // 私钥加签
            requestPublicParams.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));
            String requestStr = mapper.writeValueAsString(requestPublicParams);
            jkdymxDto.setNr(requestStr);
            log.info("wechatPayOrder 加签后的报文内容：" + requestStr);
            
            @SuppressWarnings("unchecked")
			Map<String,String> signResultMap = mapper.readValue(requestStr, Map.class);
            
            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", signResultMap.get("sign"));
            apiSign.put("timestamp", timestamp);

            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);

            // 发送HTTP post请求
            Map<String,String> response = HttpUtil.postForEntity(restTemplate, Constant.ONLINE_PAY_URL, requestStr, apiHeader);

            // 返回结果验签
            Boolean checkResult = checkSign(mapper.writeValueAsString(response));
            if(!checkResult){
            	log.error("wechatPayOrder 验证签名失败！");
				throw new BusinessException("msg", "wechatPayOrder 验证签名失败！");
            }
            String respCode = response.get("respCode");
			if(StringUtil.isNotBlank(respCode) && "SUCCESS".equals(respCode)) {
				@SuppressWarnings("unchecked")
				Map<String, String> returnCont = JSON.parseObject(response.get("biz_content"), HashMap.class);
				// 保存订单数据
				PayinfoDto t_payinfoDto = new PayinfoDto();
				t_payinfoDto.setZfid(StringUtil.generateUUID());
				t_payinfoDto.setPtddh(returnCont.get("cmbOrderId")); // 平台订单号,招行生成的订单号
				t_payinfoDto.setYwid(payinfoDto.getYwid());
				t_payinfoDto.setYwlx(payinfoDto.getYwlx());
				t_payinfoDto.setDdbh(requestTransactionParams.get("orderId"));
				t_payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
				t_payinfoDto.setDyjk("wechatPayOrder");
				t_payinfoDto.setYwyxx(requestTransactionParams.get("mchReserved"));
				t_payinfoDto.setJg("0");
				t_payinfoDto.setFkje(payinfoDto.getFkje());
				t_payinfoDto.setLrry(payinfoDto.getWxid());
				// 订单发送时间 txnTime,格式为yyyyMMddHHmmss
				String cjsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(returnCont.get("txnTime")));
				t_payinfoDto.setCjsj(cjsj);
				payinfoService.insertPayinfoDto(t_payinfoDto); // 同步新增
				
				// 保存发送信息
				jkdymxDto.setQtxx(PayTypeEnum.PAY.getCode()); // 其他信息
				jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
				insertJkdymxDto(jkdymxDto); // 同步新增
				// 返回支付信息
				// resultMap.put("webUrl", returnCont.get("mwebUrl"));
				resultMap.put("payData", returnCont.get("payData"));
				resultMap.put("status", "success");
	            return resultMap;
			}else {
				String errCode = String.valueOf(response.get("errCode"));
				String respMsg = String.valueOf(response.get("respMsg"));
				log.error("微信统一下失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
				throw new BusinessException("msg", "微信统一下失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
			}
        } catch (BusinessException e) {
        	jkdymxDto.setQtxx(e.getMsg());
			insertJkdymxDto(jkdymxDto); // 同步新增
			throw new BusinessException("msg", e.getMsg());
		}catch (Exception e){
			jkdymxDto.setQtxx("微信统一下单异常！");
			insertJkdymxDto(jkdymxDto); // 同步新增
            log.error("微信统一下单异常！" + e.toString());
            throw new BusinessException("msg", "微信统一下单异常！");
        }
	}
	
    /**
	 * 支付宝服务窗支付(暂不使用)
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
     * @throws BusinessException 
	 */
    @Override
	public Map<String, Object> alipayService(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		// TODO Auto-generated method stub
    	Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	 Map<String, String> requestPublicParams = new TreeMap<>();
        	 ObjectMapper mapper = new ObjectMapper();
            // 公共请求参数
            requestPublicParams.put("version", "0.0.1"); // 版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8"); // 编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "01"); // 签名方法，固定为01，表示签名方式为RSA2(必传)
            // 业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("merId", merid); // 商户号(必传)
            requestTransactionParams.put("userId", userid); // 收银员(必传)
            requestTransactionParams.put("notifyUrl", Constant.NOTIFY_URL); // 交易通知地址(必传)
            requestTransactionParams.put("currencyCode", Constant.CURRENCY_CODE); // 交易币种，默认156，目前只支持人民币（156）
            requestTransactionParams.put("buyerLogonId", "1234567890@qq.com"); // 买家支付宝帐号（非必传，但buyerLogonId和buyerId不能同时为空）
            requestTransactionParams.put("buyerId", "2088522115115513"); // 买家支付宝用户ID（非必传，但buyerLogonId和buyerId不能同时为空）
            requestTransactionParams.put("orderId", payinfoDto.getYbbh() + DateUtils.getTimeSuffix()); //商户订单号(必传)
            requestTransactionParams.put("txnAmt", "1"); // 交易金额,单位为分(必传)
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));

            System.out.println("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));
            
            // 对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            // 加签
            requestPublicParams.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));

            String requestStr = mapper.writeValueAsString(requestPublicParams);

            System.out.println("加签后的报文内容：" + requestStr);
            
            @SuppressWarnings("unchecked")
			Map<String,String> signResultMap = mapper.readValue(requestStr, Map.class);

            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", signResultMap.get("sign"));
            apiSign.put("timestamp", timestamp);

            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);

            // 发送HTTP post请求
            Map<String,String> response = HttpUtil.postForEntity(restTemplate, "", requestStr, apiHeader);

            // 返回结果验签
            Boolean checkResult = checkSign(mapper.writeValueAsString(response));
            if(!checkResult){
                return resultMap;
            }

            String success = response.get("returnCode");
            System.out.println(mapper.writeValueAsString(response));
            System.out.println("返回结果：" + success);
            
            return resultMap;
        } catch (BusinessException e) {
			throw new BusinessException("msg", e.getMsg());
		}catch (Exception e){
            log.error("生成支付宝服务窗订单异常！" + e.toString());
            throw new BusinessException("msg", "生成支付宝服务窗订单异常！");
        }
	}
	
    /**
	 * 支付宝native码
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
     * @throws BusinessException 
	 */
	@Override
	public Map<String, Object> alipayNative(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 接口调用保存
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setLxqf("send"); // 类型区分 发送 send;接收recv
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PAY.getCode());
		jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null)); // 调用时间
		jkdymxDto.setDydz("alipayNative"); // 调用地址
		jkdymxDto.setSfcg("0");
		try {
			ObjectMapper mapper = new ObjectMapper();
        	Map<String, String> requestPublicParams = new TreeMap<>();
            // 公共请求参数
            requestPublicParams.put("version", "0.0.1"); // 版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8"); // 编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "01"); // 签名方法，固定为01，表示签名方式为RSA2(必传)
            // 业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("merId", merid); // 商户号(必传)
            requestTransactionParams.put("userId", userid); // 收银员
            requestTransactionParams.put("notifyUrl", Constant.NOTIFY_URL);  //交易通知地址(必传)
            requestTransactionParams.put("timeoutExpress", Constant.PAY_TIMEOUT); // 支付有效时间
            requestTransactionParams.put("qrCodeTimeoutExpress", Constant.PAY_TIMEOUT); // 二维码有效时间
            requestTransactionParams.put("tradeScene", Constant.TRADE_SCENE); // 交易场景(必传)
            requestTransactionParams.put("txnAmt", String.valueOf(new BigDecimal(payinfoDto.getFkje()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP))); //交易金额,单位为分(必传)
//            requestTransactionParams.put("orderId", payinfoDto.getYbbh() + DateUtils.getTimeSuffix()); // 商户订单号(必传)
            Map<String, String> map = new HashMap<String, String>();
			if (payinfoDto.getYwlx().equals(BusinessTypeEnum.XG.getCode())){
				requestTransactionParams.put("orderId", StringUtil.generateUUID()); // 商户订单号(必传)
				map.put("bh", payinfoDto.getYwid());
			}else{
				requestTransactionParams.put("orderId", payinfoDto.getYbbh() + DateUtils.getTimeSuffix()); // 商户订单号(必传)
				map.put("bh", payinfoDto.getYbbh());
			}
			map.put("je", payinfoDto.getFkje());
			map.put("dd", requestTransactionParams.get("orderId"));
			map.put("fl", PayTypeEnum.PAY.getCode());
			map.put("lx", payinfoDto.getYwlx()); // 区分送检和复检
            requestTransactionParams.put("mchReserved", mapper.writeValueAsString(map)); // 商户保留域
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
            log.info("alipayNative 加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));

            // 对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            // 私钥加签
            requestPublicParams.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));
            String requestStr = mapper.writeValueAsString(requestPublicParams);
            jkdymxDto.setNr(requestStr);
            log.info("alipayNative 加签后的报文内容：" + requestStr);
			
            @SuppressWarnings("unchecked")
			Map<String,String> signResultMap = mapper.readValue(requestStr, Map.class);

            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", signResultMap.get("sign"));
            apiSign.put("timestamp", timestamp);

            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);

            // 发送HTTP post请求
            Map<String,String> response = HttpUtil.postForEntity(restTemplate, Constant.ZFB_QRCODE_URL, requestStr, apiHeader);

            // 返回结果验签
            Boolean checkResult = checkSign(mapper.writeValueAsString(response));
            if(!checkResult){
            	log.error("alipayNative 验证签名失败！");
				throw new BusinessException("msg", "alipayNative 验证签名失败！");
            }
            
            String respCode = response.get("respCode");
			if(StringUtil.isNotBlank(respCode) && "SUCCESS".equals(respCode)) {
				@SuppressWarnings("unchecked")
				Map<String, String> returnCont = JSON.parseObject(response.get("biz_content"), HashMap.class);
				// 保存订单数据
				PayinfoDto t_payinfoDto = new PayinfoDto();
				t_payinfoDto.setZfid(StringUtil.generateUUID());
				t_payinfoDto.setYwid(payinfoDto.getYwid());
				t_payinfoDto.setYwlx(payinfoDto.getYwlx());
				if ("XG".equals(payinfoDto.getYwlx())){
					redisUtil.set("PAY_XG_"+payinfoDto.getYwid(),t_payinfoDto.getZfid(),1800);
				}
				t_payinfoDto.setDdbh(requestTransactionParams.get("orderId"));
				t_payinfoDto.setPtddh(returnCont.get("cmbOrderId")); // 平台订单号,招行生成的订单号
				t_payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
				t_payinfoDto.setDyjk("alipayNative");
				t_payinfoDto.setYwyxx(requestTransactionParams.get("mchReserved"));
				t_payinfoDto.setJg("0");
				t_payinfoDto.setFkje(payinfoDto.getFkje());
				t_payinfoDto.setZfjcxm(payinfoDto.getZfjcxm());
				t_payinfoDto.setLrry(payinfoDto.getLrry());
				//设置外部程序id
				if (StringUtil.isBlank(payinfoDto.getWbcxdm())){
					payinfoDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
				}
				Map<Object, Object> wbcxInfoList = redisUtil.hmget("WbcxInfo");
				for(Object wbcxInfoObj : wbcxInfoList.values()) {
					com.alibaba.fastjson.JSONObject wbcxInfo = JSON.parseObject(wbcxInfoObj.toString());
					if (wbcxInfo!=null){
						if (payinfoDto.getWbcxdm().equals(wbcxInfo.getString("wbcxdm"))){
							t_payinfoDto.setWbcxid(wbcxInfo.getString("wbcxid"));
							break;
						}
					}
				}
				// 订单发送时间 txnTime,格式为yyyyMMddHHmmss
				String cjsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(returnCont.get("txnTime")));
				t_payinfoDto.setCjsj(cjsj);
				payinfoService.insertPayinfoDto(t_payinfoDto); // 同步新增
				
				// 保存发送信息
				jkdymxDto.setQtxx(PayTypeEnum.PAY.getCode()); // 其他信息
				jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
				insertJkdymxDto(jkdymxDto); // 同步新增
				
				resultMap.put("qrCode", returnCont.get("qrCode"));
				resultMap.put("status", "success");
				return resultMap;
			}else {
				String errCode = String.valueOf(response.get("errCode"));
				String respMsg = String.valueOf(response.get("respMsg"));
				log.error("收款码申请失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
				throw new BusinessException("msg", "收款码申请失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
			}
		} catch (BusinessException e) {
			jkdymxDto.setQtxx(e.getMsg());
			insertJkdymxDto(jkdymxDto); // 同步新增
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e){
			jkdymxDto.setQtxx("生成支付宝native码订单异常！");
			insertJkdymxDto(jkdymxDto); // 同步新增
            log.error("生成支付宝native码订单异常！" + e.toString());
            throw new BusinessException("msg", "生成支付宝native码订单异常！");
        }
	}
    
	/**
	 * 支付结果查询
	 * @param restTemplate
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public Map<String, Object> payResultInquire(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(StringUtil.isBlank(payinfoDto.getDdbh()) && StringUtil.isBlank(payinfoDto.getPtddh())) {
				log.error("inquire 缺少订单信息！");
				throw new BusinessException("msg", "inquire 缺少订单信息！");
			}
			// json转换
			ObjectMapper mapper = new ObjectMapper();
			// 公共请求参数
			Map<String, String> submitMap = new TreeMap<String, String>();
			submitMap.put("signMethod", "01"); // 签名方法 01：RSA2，02：SM2 必填
			submitMap.put("version", "0.0.1"); // 版本号 固定为0.0.1 必填
			submitMap.put("encoding", "UTF-8"); // 编码方式 固定为UTF-8 必填
			// 业务参数
			Map<String, String> detailMap = new HashMap<String, String>();
			detailMap.put("merId", merid); // 商户号 必填
			detailMap.put("orderId", payinfoDto.getDdbh()); // 商户订单号，此字段和平台订单号字段至少要上送一个，若两个都上送，则以平台订单号为准
			detailMap.put("cmbOrderId", payinfoDto.getPtddh()); // 平台订单号
			detailMap.put("userId", userid); // 收银员 必填
			submitMap.put("biz_content", mapper.writeValueAsString(detailMap)); // 必填

	        // 对待加签内容进行排序拼接
	        String signContent= SignatureUtil.getSignContent(submitMap);
	        // 私钥加签
	        submitMap.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8")); // 签名 必填
			
            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", submitMap.get("sign"));
            apiSign.put("timestamp", timestamp);

            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);
			// 设置超时时间
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setConnectTimeout(60000);
			requestFactory.setReadTimeout(60000);
			restTemplate.setRequestFactory(requestFactory);
			// 发送HTTP post请求
            Map<String, String> returnMap = HttpUtil.postForEntity(restTemplate, Constant.ORDER_QUERY_URL, mapper.writeValueAsString(submitMap), apiHeader);
            log.info(mapper.writeValueAsString(returnMap));
            
            // 返回结果验签
            boolean checkResult = checkSign(mapper.writeValueAsString(returnMap));
            if(!checkResult){
            	log.error("inquire验证签名失败！");
				throw new BusinessException("msg", "inquire验证签名失败！");
            }
			String respCode = String.valueOf(returnMap.get("respCode"));
			if(StringUtil.isNotBlank(respCode) && "SUCCESS".equals(respCode)) {
				@SuppressWarnings("unchecked")
				Map<String, String> returnCont = JSON.parseObject(returnMap.get("biz_content"), HashMap.class);
				@SuppressWarnings("unchecked")
				Map<String, String> mchMap = JSON.parseObject(returnCont.get("mchReserved"), HashMap.class);
				//D交易已撤销;C订单已关闭;P交易在进行;F交易失败;S交易成功;R转入退款
				if("D".equals(returnCont.get("tradeState")) || "F".equals(returnCont.get("tradeState")) || "C".equals(returnCont.get("tradeState"))) {
					// 订单已关闭
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setPtddh(returnCont.get("cmbOrderId"));
					t_payinfoDto.setYwyxx(returnCont.get("mchReserved"));
					t_payinfoDto.setJg("2");
					payinfoService.callbackInfo(t_payinfoDto);
				}else if("S".equals(returnCont.get("tradeState"))) {
					// 交易成功
					String jysj = DateUtils.getCustomFomratCurrentDate(null);
					if(StringUtil.isNotBlank(returnCont.get("endDate")) && StringUtil.isNotBlank(returnCont.get("endTime"))){
						String payTime = returnCont.get("endDate")+returnCont.get("endTime");
						SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							jysj = simpleDateFormat.format(yyyyMMddHHmmss.parse(payTime));
						} catch (Exception e) {
							log.error("endDate，endTime时间转换异常！采用当前时间为交易时间！");
						}
					}
					PayinfoDto s_payinfoDto = payinfoService.getDto(payinfoDto);
					if(StringUtil.isNotBlank(s_payinfoDto.getJysj()))
						jysj = s_payinfoDto.getJysj();
					// 更新回调支付信息
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setPtddh(returnCont.get("cmbOrderId"));//平台订单号
					t_payinfoDto.setYwyxx(returnCont.get("mchReserved"));//业务域信息
					t_payinfoDto.setJyje(returnCont.get("txnAmt"));//交易金额
					t_payinfoDto.setDdbh(returnCont.get("orderId"));//订单编号
					t_payinfoDto.setZffs(returnCont.get("payType"));//支付方式
					t_payinfoDto.setJg("1");//支付结果，1成功
					t_payinfoDto.setJysj(jysj);//交易时间
					t_payinfoDto.setZfid(s_payinfoDto.getZfid());//支付ID
					t_payinfoDto.setWbcxid(s_payinfoDto.getWbcxid());//外部程序id
					payinfoService.updatePayinfoDto(t_payinfoDto);
					log.error("交易成功:ywlx=" + s_payinfoDto.getYwlx());
					// 判断业务类型 sj fj
					if(BusinessTypeEnum.SJ.getCode().equals(s_payinfoDto.getYwlx())) {
						// 发送送检支付消息通知 
						t_payinfoDto.setHzxm(s_payinfoDto.getHzxm());
						t_payinfoDto.setYbbh(s_payinfoDto.getYbbh());
						t_payinfoDto.setSjhb(s_payinfoDto.getSjhb());
						t_payinfoDto.setLrry(s_payinfoDto.getLrry());
						sendPayMessage(restTemplate, t_payinfoDto);
						
						// 修改送检表支付信息
						SjxxDto sjxxDto = new SjxxDto();
						sjxxDto.setFkbj("1");
						sjxxDto.setSjid(s_payinfoDto.getYwid());
						String sfje = selectPaidAmount(s_payinfoDto.getYwid(), BusinessTypeEnum.SJ.getCode());
						sjxxDto.setSfje(String.valueOf(sfje));
						sjxxDto.setFkrq(jysj);
						sjxxService.modInspPayinfo(sjxxDto,s_payinfoDto.getZfid());
					}else if(BusinessTypeEnum.FJ.getCode().equals(s_payinfoDto.getYwlx())){
						// 发送支付消息通知 
						SjxxDto sjxxDto = new SjxxDto();
						sjxxDto.setYbbh(mchMap.get("bh"));
						sjxxDto = sjxxService.getDto(sjxxDto, 2);
						t_payinfoDto.setHzxm(sjxxDto.getHzxm());
						t_payinfoDto.setYbbh(sjxxDto.getYbbh());
						t_payinfoDto.setSjhb(sjxxDto.getDb());
						t_payinfoDto.setLrry(s_payinfoDto.getLrry());
						sendDdPayMessage(t_payinfoDto);
						
						// 修改复检表支付信息
						FjsqDto fjsqDto = new FjsqDto();
						String sfje = selectPaidAmount(s_payinfoDto.getYwid(), BusinessTypeEnum.FJ.getCode());
						fjsqDto.setSfje(String.valueOf(sfje));
						fjsqDto.setFkrq(jysj);
						fjsqDto.setFkbj("1");
						fjsqDto.setFjid(s_payinfoDto.getYwid());
						fjsqDto.setYwlx(PayTypeEnum.PAY.getCode());
						// 同步复检支付信息
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FDIF_MOD.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(fjsqDto));
					}else if(BusinessTypeEnum.XG.getCode().equals(s_payinfoDto.getYwlx())){
						IPayService payService = (IPayService) ServiceFactory.getService(BusinessTypeEnum.XG.getValue());
						payService.paySuccess(mchMap.get("bh"),returnCont);
					}
					
					// 关闭其它订单
					/** 由于可能存在同时打开多个订单，所以关闭其它订单会导致订单关闭失败
					 * List<PayinfoDto> list = payinfoService.selectPayOrders(payinfoDto);
					 for (int i = 0; i < list.size(); i++) {
					 closeOrder(restTemplate, list.get(i));
					 }*/
					
					// 钉钉消息通知
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_SUC.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(t_payinfoDto));
					
					// 保存调用接口信息
					JkdymxDto jkdymxDto = new JkdymxDto();
					jkdymxDto.setLxqf("send"); // 类型区分 发送 send;接收recv
					jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null)); // 调用时间
					jkdymxDto.setDydz("payResultInquire"); // 调用地址
					jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
					jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PAY.getCode());
					jkdymxDto.setSfcg("1");
					jkdymxDto.setNr(mapper.writeValueAsString(submitMap));
					jkdymxDto.setQtxx("payResultInquire success");
					insertJkdymxDto(jkdymxDto); // 同步新增
				}else if("P".equals(returnCont.get("tradeState")) || "R".equals(returnCont.get("tradeState"))) {
					// 支付未完成，不处理
					log.error("交易状态："+ returnCont.get("tradeState"));
				}else {
					// 未知状态码
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setPtddh(returnCont.get("cmbOrderId"));
					t_payinfoDto.setYwyxx(returnCont.get("mchReserved"));
					t_payinfoDto.setJg("9");
					t_payinfoDto.setCwxx("未知状态码：" + returnCont.get("tradeState"));
					payinfoService.callbackInfo(t_payinfoDto);
				}
				resultMap.put("status", "success");
			}else {
				String errCode = String.valueOf(returnMap.get("errCode"));
				String respMsg = String.valueOf(returnMap.get("respMsg"));
				if("ORDERID_INVALID".equals(errCode)) {
					// 支付失效 关闭订单？
					log.error(payinfoDto.getPtddh()+"订单已失效，关闭订单！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setPtddh(payinfoDto.getPtddh());
					t_payinfoDto.setJg("3");
					t_payinfoDto.setCwxx(errCode + respMsg);
					payinfoService.callbackInfo(t_payinfoDto);
				} else if("UNPAIED_ORDER".equals(errCode)){
					// 订单未支付 不做操作
					log.error(payinfoDto.getPtddh()+"订单未支付！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
				} else {
					// 平台订单号异常  if("CMBORDERID_NOT_EXIST".equals(errCode) || "CMBORDERID_NOT_LAWFUL".equals(errCode))
					log.error(payinfoDto.getPtddh()+"支付结果查询失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
					resultMap.put("message", "支付结果查询失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setPtddh(payinfoDto.getPtddh());
					t_payinfoDto.setJg("9");
					t_payinfoDto.setCwxx("支付结果查询失败！" + errCode +" "+ respMsg);
					payinfoService.callbackInfo(t_payinfoDto);
				}
			}
			return resultMap;
		} catch (BusinessException e) {
			log.error("查询异常！BusinessException:" + e.toString());
			throw new BusinessException("msg", "查询异常！"+e.getMsg());
		} catch (Exception e){
			log.error("查询异常！Exception:" + e.toString());
			throw new BusinessException("msg", "查询异常！"+e.getMessage());
	    }
	}


	/**
	 * 支付/退款结果通知
	 * @param restTemplate
	 * @param
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map<String, String> payResultNotice(RestTemplate restTemplate, String requestBodyString) {
		//设置响应数据
		Map<String, String> respData = new HashMap<String, String>();
		respData.put("version", "0.0.1");
		respData.put("encoding", "UTF-8");
		respData.put("signMethod", "01");
		log.error("结果通知 -- " + requestBodyString);
		// 接口调用保存
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
		jkdymxDto.setLxqf("recv"); // 类型区分 发送 send;接收recv
		jkdymxDto.setFhnr(requestBodyString); // 返回内容
		jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
		jkdymxDto.setDydz("payResultNotice"); // 调用地址
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PAY.getCode());
		try {
			respData.put("returnCode", "SUCCESS");
			//非空校验
			if (requestBodyString == null || "".equals(requestBodyString.trim())) {
				respData.put("returnCode", "FAIL");
			    return respData;
			}
			
			Map<String, String> requestBodyMap = str2Map(requestBodyString);
			Map<String, String> resultMap = requestBodyMap.entrySet().stream().collect(Collectors.toMap(e -> SignatureUtil.decode(e.getKey()), e -> SignatureUtil.decode(e.getValue())));
			if (resultMap == null) {
			    respData.put("returnCode", "FAIL");
			    return respData;
			}
			String sign = resultMap.remove("sign");
			//对待加签内容进行排序拼接
			String contentStr = SignatureUtil.getSignContent(resultMap);
			//验证签名-使用招行公钥进行验签
			boolean flag = SignatureUtil.rsaCheck(contentStr, sign, cmb_public_key, "UTF-8");
			if (!flag) {
				log.error("notice验签失败!");
				jkdymxDto.setQtxx("notice 验签失败!");
				insertJkdymxDto(jkdymxDto); // 同步新增
			    respData.put("returnCode", "FAIL");
			    return respData;
			}
			
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)JSONObject.fromObject(resultMap.get("biz_content"));
			// 更新回调支付信息
			PayinfoDto payinfoDto = new PayinfoDto();
			payinfoDto.setYwyxx(map.get("mchReserved"));
			payinfoDto.setZffs(map.get("payType"));
			payinfoDto.setPtddh(map.get("cmbOrderId"));
			payinfoDto.setDdbh(map.get("orderId"));
			payinfoDto.setJyje(map.get("txnAmt"));
			payinfoDto.setJg("1");
			@SuppressWarnings("unchecked")
			Map<String, String> mchMap = JSON.parseObject(map.get("mchReserved"), HashMap.class);
			payinfoDto.setYwlx(mchMap.get("lx"));
			jkdymxDto.setQtxx(mchMap.get("fl")); // 其他信息
			String jysj = DateUtils.getCustomFomratCurrentDate(null);
			PayinfoDto t_payinfoDto = payinfoService.getDto(payinfoDto);
			if(t_payinfoDto != null) {
				payinfoDto.setZfid(t_payinfoDto.getZfid());
				payinfoDto.setLrry(t_payinfoDto.getLrry());
				if(StringUtil.isNotBlank(t_payinfoDto.getJysj()))
					jysj = t_payinfoDto.getJysj();	
			}
			payinfoDto.setJysj(jysj);
			
			// 判断业务类型
			if(PayTypeEnum.PAY.getCode().equals(mchMap.get("fl"))) {
				// 修改支付信息
				if(StringUtil.isNotBlank(payinfoDto.getZfid())) {
					payinfoService.updatePayinfoDto(payinfoDto);
				}else {
					payinfoService.insertPayinfoDto(payinfoDto);
				}
				
				// 支付回调，判断业务类型
				if(BusinessTypeEnum.SJ.getCode().equals(mchMap.get("lx"))) {
					// 修改送检表支付信息
					SjxxDto sjxxDto = new SjxxDto();
					sjxxDto.setYbbh(mchMap.get("bh"));
					SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto, 2);
					//当该标本编号有修改的时候，根据已存在的sjid，进行再次查找 2021-07-30
					if(t_sjxxDto == null && StringUtil.isNotBlank(t_payinfoDto.getSjid())) {
						SjxxDto tt_sjxxDto = new SjxxDto();
						tt_sjxxDto.setSjid(t_payinfoDto.getSjid());
						t_sjxxDto = sjxxService.getDto(tt_sjxxDto, 2);
					}
					sjxxDto.setSjid(t_sjxxDto.getSjid());
					sjxxDto.setFkbj("1");
					sjxxDto.setFkrq(jysj);
					String sfje = selectPaidAmount(t_sjxxDto.getSjid(), BusinessTypeEnum.SJ.getCode());
					sjxxDto.setSfje(sfje);
					sjxxService.modInspPayinfo(sjxxDto,t_payinfoDto.getZfid());
					payinfoDto.setYwid(t_sjxxDto.getSjid());
					
					// 发送支付消息通知
					if(t_payinfoDto != null) {
						payinfoDto.setHzxm(t_sjxxDto.getHzxm());
						payinfoDto.setYbbh(t_sjxxDto.getYbbh());
						payinfoDto.setSjhb(t_sjxxDto.getDb());
						payinfoDto.setWbcxid(t_payinfoDto.getWbcxid());
						sendPayMessage(restTemplate, payinfoDto);
					}
				}else if(BusinessTypeEnum.FJ.getCode().equals(mchMap.get("lx"))){
					// 发送支付消息通知
					if(t_payinfoDto != null) {
						SjxxDto sjxxDto = new SjxxDto();
						sjxxDto.setYbbh(mchMap.get("bh"));
						sjxxDto = sjxxService.getDto(sjxxDto, 2);
						payinfoDto.setHzxm(sjxxDto.getHzxm());
						payinfoDto.setYbbh(sjxxDto.getYbbh());
						payinfoDto.setSjhb(sjxxDto.getDb());
						sendDdPayMessage(payinfoDto);
					}
					
					// 修改复检表支付信息
					FjsqDto fjsqDto = new FjsqDto();
					fjsqDto.setYwlx(PayTypeEnum.PAY.getCode());
					fjsqDto.setFkrq(jysj);
					fjsqDto.setFkbj("1");
					if(t_payinfoDto != null) {
						fjsqDto.setFjid(t_payinfoDto.getYwid());
						String sfje = selectPaidAmount(t_payinfoDto.getYwid(), BusinessTypeEnum.FJ.getCode());
						fjsqDto.setSfje(sfje);
						// 同步复检支付信息
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FDIF_MOD.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(fjsqDto));
					}
				}else if(BusinessTypeEnum.XG.getCode().equals(mchMap.get("lx"))){
					IPayService payService = (IPayService) ServiceFactory.getService(BusinessTypeEnum.XG.getValue());
					payService.paySuccess(mchMap.get("bh"),map);
				}
				
				// 关闭其它订单
				/** 由于可能存在同时打开多个订单，所以关闭其它订单会导致订单关闭失败
				 if(StringUtil.isNotBlank(payinfoDto.getYwid())) {
				 List<PayinfoDto> list = payinfoService.selectPayOrders(payinfoDto);
				 for (int i = 0; i < list.size(); i++) {
				 closeOrder(restTemplate, list.get(i));
				 }*/

				// 钉钉消息通知
				if(t_payinfoDto != null) {
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_SUC.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(payinfoDto));
				}
			}else {
				payinfoDto.setGlddh(mchMap.get("bh")); // 设置关联订单号
				// 修改支付信息
				if(StringUtil.isNotBlank(payinfoDto.getZfid())) {
					payinfoService.updatePayinfoDto(payinfoDto);
				}else {
					payinfoService.insertPayinfoDto(payinfoDto);
				}
				
				// 退款回调，判断业务类型
				if(BusinessTypeEnum.SJ.getCode().equals(mchMap.get("lx"))) {
					// 修改送检表退款信息
					SjxxDto sjxxDto = new SjxxDto();
					sjxxDto.setYbbh(mchMap.get("bh"));
					SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto, 2);
					sjxxDto.setSjid(t_sjxxDto.getSjid());
					String sfje = selectPaidAmount(t_payinfoDto.getYwid(), BusinessTypeEnum.SJ.getCode());
					sjxxDto.setSfje(sfje);
					// 判断，实付金额大于0时不修改付款标记
					if(new BigDecimal(sfje).compareTo(BigDecimal.ZERO) != 1) {
						sjxxDto.setFkbj("0");
					}else {
						sjxxDto.setFkbj("1");
						sjxxDto.setFkrq(t_sjxxDto.getFkrq());
					}
					sjxxService.modInspPayinfo(sjxxDto,t_payinfoDto.getZfid());
				}else if(BusinessTypeEnum.FJ.getCode().equals(mchMap.get("lx"))){
					// 修改复检表支付信息
					FjsqDto fjsqDto = new FjsqDto();
					fjsqDto.setYwlx(PayTypeEnum.REFUND.getCode());
					if(t_payinfoDto != null) {
						fjsqDto.setFjid(t_payinfoDto.getYwid());
						String sfje = selectPaidAmount(t_payinfoDto.getYwid(), BusinessTypeEnum.FJ.getCode());
						fjsqDto.setSfje(sfje);
						// 判断，实付金额大于0时不修改付款标记
						if(new BigDecimal(sfje).compareTo(BigDecimal.ZERO) != 1) {
							fjsqDto.setFkbj("0");
						}else {
							fjsqDto.setFkbj("1");
						}
						// 同步复检支付信息
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FDIF_MOD.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(fjsqDto));
					}
				}
			}
			
			// 保存调用信息
			jkdymxDto.setSfcg("1");
			insertJkdymxDto(jkdymxDto);
			
			respData.put("respCode", "SUCCESS");
			//对待加签内容进行排序拼接
			String signContent = SignatureUtil.getSignContent(respData);
			//加签-使用商户私钥加签
			respData.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));
			log.info("notice 加签完成!");
			return respData;
		} catch (Exception e) {
			jkdymxDto.setQtxx("支付结果通知异常！" + e.toString());
			log.error("支付结果通知异常！" + e.toString());
			respData.put("returnCode", "FAIL");
		}
		insertJkdymxDto(jkdymxDto); // 同步新增
		return respData;
	}

	/**
	 * 关闭订单
	 * @param restTemplate
	 * @param
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public Map<String, Object> closeOrder(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(payinfoDto == null || StringUtil.isBlank(payinfoDto.getDdbh()) || StringUtil.isBlank(payinfoDto.getPtddh())) {
			log.error("关闭订单信息不完整！" + (payinfoDto!=null?payinfoDto.getDdbh()+" "+payinfoDto.getPtddh():""));
			resultMap.put("status", "fail");
        	resultMap.put("message", "关闭订单信息不完整！" + (payinfoDto!=null?payinfoDto.getDdbh()+" "+payinfoDto.getPtddh():""));
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			Map<String, String> requestPublicParams = new TreeMap<>();
			//公共请求参数
			requestPublicParams.put("version", "0.0.1"); //版本号，固定为0.0.1(必传字段)
			requestPublicParams.put("encoding", "UTF-8"); //编码方式，固定为UTF-8(必传)
			requestPublicParams.put("signMethod", "01"); //签名方法，固定为01，表示签名方式为RSA2(必传)
			//业务要素
			Map<String, String> requestTransactionParams = new HashMap<>();
			requestTransactionParams.put("merId", merid); //商户号(必传)
			requestTransactionParams.put("origCmbOrderId", payinfoDto.getPtddh()); //交易平台订单号,此字段和被关闭交易商户订单号字段至少要上送一个，若两个都上送，则以此字段为准
			requestTransactionParams.put("origOrderId", payinfoDto.getDdbh()); //交易商户订单号 (回调会用，必传)
			requestTransactionParams.put("userId", userid); //收银员(必传)
			requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
			
            // 对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            // 私钥加签
            requestPublicParams.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));
            String signResult = mapper.writeValueAsString(requestPublicParams);
            log.info("closeOrder 加签后的报文内容：" + signResult);
            
            @SuppressWarnings("unchecked")
			Map<String,String> signResultMap = mapper.readValue(signResult, Map.class);
            
            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", signResultMap.get("sign"));
            apiSign.put("timestamp", timestamp);

            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);

            // 发送HTTP post请求
            Map<String,String> response = HttpUtil.postForEntity(restTemplate, Constant.CLOSE_URL, signResult, apiHeader);
            log.error(mapper.writeValueAsString(response));
            // 返回结果验签
            Boolean checkResult = checkSign(mapper.writeValueAsString(response));
            if(!checkResult){
            	log.error("close报文验签失败!");
            	resultMap.put("status", "fail");
            	resultMap.put("message", "close报文验签失败!");
                return resultMap;
            }
			@SuppressWarnings("unchecked")
			Map<String, String> returnCont = JSON.parseObject(response.get("biz_content"), HashMap.class);
            // 判断订单关闭结果
            if(StringUtil.isNotBlank(response.get("respCode")) && "SUCCESS".equals(response.get("respCode"))) {
            	if("C".equals(returnCont.get("closeState"))) {
					// 更新订单状态
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setDdbh(returnCont.get("origOrderId"));
					t_payinfoDto.setJg("2");
					payinfoService.callbackInfo(t_payinfoDto);
            		log.error("关单成功:" + returnCont.get("origOrderId"));
            		// 更新订单状态
            		resultMap.put("status", "success");
            	}else {
            		log.error("关单失败:"+returnCont.get("closeState"));
            		resultMap.put("status", "fail");
            		resultMap.put("message", "订单关闭失败！");
            	}
            }else {
				if ("CMBORDERID_NOT_EXIST".equals(response.get("errCode"))) {
					// 更新订单状态
					PayinfoDto t_payinfoDto = new PayinfoDto();
					t_payinfoDto.setDdbh(returnCont.get("origOrderId"));
					t_payinfoDto.setJg("2");
					payinfoService.callbackInfo(t_payinfoDto);
					log.error("关单成功:订单不存在！" + returnCont.get("origOrderId"));
					// 更新订单状态
					resultMap.put("status", "success");
					return resultMap;
				}
            	log.error("返回结果错误: "+response.get("errCode") + response.get("respMsg"));
            	resultMap.put("status", "fail");
            	resultMap.put("message", response.get("errCode") + response.get("respMsg"));
            }
		} catch (BusinessException e) {
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e){
            log.error("关闭订单异常！" + e.toString());
            throw new BusinessException("msg", "关闭订单异常！");
        }
		return resultMap;
	}
	
	/**
	 * 退款申请
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public Map<String, Object> refundApply(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 接口调用保存
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setLxqf("send"); // 类型区分 发送 send;接收recv
		jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null)); // 调用时间
		jkdymxDto.setDydz("refundApply"); // 调用地址
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PAY.getCode());
		jkdymxDto.setSfcg("0");
        try {
        	ObjectMapper mapper = new ObjectMapper();
        	
        	Map<String, String> requestPublicParams = new TreeMap<>();
            //公共请求参数
            requestPublicParams.put("version", "0.0.1"); //版本号，固定为0.0.1(必传字段)
            requestPublicParams.put("encoding", "UTF-8"); //编码方式，固定为UTF-8(必传)
            requestPublicParams.put("signMethod", "01"); //签名方法，固定为01，表示签名方式为RSA2(必传)
            //业务要素
            Map<String, String> requestTransactionParams = new HashMap<>();
            requestTransactionParams.put("merId", merid); //商户号(必传)
            requestTransactionParams.put("userId", userid); //收银员(必传)
            requestTransactionParams.put("notifyUrl", Constant.NOTIFY_URL); //交易通知地址
            requestTransactionParams.put("txnAmt", payinfoDto.getJyje()); //交易金额，单位为分（必传）
            requestTransactionParams.put("refundAmt", String.valueOf(new BigDecimal(payinfoDto.getTkje()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP))); //退款金额，单位为分（必传）
            requestTransactionParams.put("origOrderId", payinfoDto.getDdbh()); //原交易的商户订单号
            requestTransactionParams.put("origCmbOrderId", payinfoDto.getPtddh()); //原交易招行订单号
//            requestTransactionParams.put("orderId", payinfoDto.getYbbh() + DateUtils.getTimeSuffix()); //商户订单号(必传)
            Map<String, String> map = new HashMap<String, String>();
			if (payinfoDto.getYwlx().equals(BusinessTypeEnum.XG.getCode())){
				requestTransactionParams.put("orderId", StringUtil.generateUUID()); // 商户订单号(必传)
				map.put("bh", payinfoDto.getYwid());
			}else{
				requestTransactionParams.put("orderId", "XGBDJC" + DateUtils.getTimeSuffix()); // 商户订单号(必传)
				map.put("bh", payinfoDto.getYbbh());
			}
			map.put("je", payinfoDto.getTkje());
			map.put("dd", payinfoDto.getDdbh());
			map.put("fl", PayTypeEnum.REFUND.getCode());
			map.put("lx", payinfoDto.getYwlx());
            requestTransactionParams.put("mchReserved", mapper.writeValueAsString(map)); // 商户保留域
            requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
            log.info("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));
            
            // 对待加签内容进行排序拼接
            String signContent= SignatureUtil.getSignContent(requestPublicParams);
            // 私钥加签
            requestPublicParams.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));
            String requestStr = mapper.writeValueAsString(requestPublicParams);
            log.info("加签后的报文内容：" + requestStr);
            jkdymxDto.setNr(requestStr); // 保存发送内容
            
            @SuppressWarnings("unchecked")
			Map<String,String> signResultMap = mapper.readValue(requestStr, Map.class);
            
            // 组apiSign加密Map
            String timestamp = "" + System.currentTimeMillis()/1000;
            Map<String,String> apiSign = new TreeMap<>();
            apiSign.put("appid", appid);
            apiSign.put("secret", secret);
            apiSign.put("sign", signResultMap.get("sign"));
            apiSign.put("timestamp", "" + System.currentTimeMillis()/1000);

            // MD5加密
            String MD5Content = SignatureUtil.getSignContent(apiSign);
            String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

            // 组request头部Map
            Map<String, String> apiHeader = new HashMap<>();
            apiHeader.put("appid", appid);
            apiHeader.put("timestamp", timestamp);
            apiHeader.put("apisign", apiSignString);

            // 发送HTTP post请求
            Map<String,String> response = HttpUtil.postForEntity(restTemplate, Constant.REFUND_URL, requestStr, apiHeader);

            // 返回结果验签
            Boolean checkResult = checkSign(mapper.writeValueAsString(response));
            if(!checkResult){
            	jkdymxDto.setQtxx("refundApply 报文验签失败!");
				insertJkdymxDto(jkdymxDto); // 同步新增
            	log.error("refundApply 报文验签失败!");
            	resultMap.put("status", "fail");
            	resultMap.put("message", "refundApply 报文验签失败!");
                return resultMap;
            }
            if(StringUtil.isNotBlank(response.get("respCode")) && "SUCCESS".equals(response.get("respCode"))) {
            	@SuppressWarnings("unchecked")
				Map<String, String> returnCont = JSON.parseObject(response.get("biz_content"), HashMap.class);
            	returnCont.get("refundAmt"); // 退款金额
            	// 保存退款订单数据
				PayinfoDto t_payinfoDto = new PayinfoDto();
				t_payinfoDto.setZfid(StringUtil.generateUUID());
				t_payinfoDto.setLrry(payinfoDto.getLrry());
				t_payinfoDto.setYwid(payinfoDto.getYwid());
				t_payinfoDto.setYwlx(payinfoDto.getYwlx());
				t_payinfoDto.setYwyxx(requestTransactionParams.get("mchReserved"));
				t_payinfoDto.setDdbh(returnCont.get("orderId")); // 商户退款订单号
				t_payinfoDto.setPtddh(returnCont.get("cmbOrderId"));
				t_payinfoDto.setGlddh(payinfoDto.getDdbh()); // 设置关联订单号
				t_payinfoDto.setFkje("-"+payinfoDto.getTkje());
				// 订单发送时间 txnTime,格式为yyyyMMddHHmmss
				String cjsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyyMMddHHmmss").parse(returnCont.get("txnTime")));
				t_payinfoDto.setCjsj(cjsj);
				t_payinfoDto.setDylx(PayTypeEnum.REFUND.getCode());
				t_payinfoDto.setDyjk("refundApply");
				// 判断退款处理状态
				if("P".equals(returnCont.get("refundState"))) {
					// 退款正在处理
					t_payinfoDto.setJg("0");
					payinfoService.insertPayinfoDto(t_payinfoDto);
					resultMap.put("status", "success");
					resultMap.put("jg", "0");
					resultMap.put("message", "退款正在处理中!");
				}else if("S".equals(returnCont.get("refundState"))) {
					// 退款成功
					String tksj = DateUtils.getCustomFomratCurrentDate(null);
					t_payinfoDto.setJysj(tksj);
					t_payinfoDto.setJg("1");
					t_payinfoDto.setJyje("-"+returnCont.get("refundAmt"));
					payinfoService.insertPayinfoDto(t_payinfoDto);
					// 判断业务类型
					//
					if(BusinessTypeEnum.SJ.getCode().equals(payinfoDto.getYwlx())) {
						// 修改送检表支付信息
						SjxxDto sjxxDto = new SjxxDto();
						sjxxDto.setYbbh(payinfoDto.getYbbh());
						SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto, 2);
						sjxxDto.setSjid(t_sjxxDto.getSjid());
						String sfje = selectPaidAmount(t_sjxxDto.getSjid(), BusinessTypeEnum.SJ.getCode());
						sjxxDto.setSfje(sfje);
						// 判断，实付金额大于0时不修改付款标记
						if(new BigDecimal(sfje).compareTo(BigDecimal.ZERO) != 1) {
							sjxxDto.setFkbj("0");
						}else {
							sjxxDto.setFkbj("1");
							sjxxDto.setFkrq(t_sjxxDto.getFkrq());
						}
						sjxxService.modInspPayinfo(sjxxDto,t_payinfoDto.getZfid());
					}else if(BusinessTypeEnum.FJ.getCode().equals(payinfoDto.getYwlx())){
						// 修改复检表支付信息
						FjsqDto fjsqDto = new FjsqDto();
						fjsqDto.setFjid(payinfoDto.getYwid());
						String sfje = selectPaidAmount(payinfoDto.getYwid(), BusinessTypeEnum.FJ.getCode());
						// 判断，实付金额大于0时不修改付款标记
						if(new BigDecimal(sfje).compareTo(BigDecimal.ZERO) != 1) {
							fjsqDto.setFkbj("0");
						}else {
							fjsqDto.setFkbj("1");
						}
						fjsqDto.setSfje(sfje);
						fjsqDto.setYwlx(PayTypeEnum.REFUND.getCode());
						// 同步复检支付信息
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FDIF_MOD.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(fjsqDto));
					}
					resultMap.put("jg", "1");
					resultMap.put("refundno", returnCont.get("cmbOrderId"));
					resultMap.put("status", "success");
					resultMap.put("message", "退款成功!");
				}else if("F".equals(returnCont.get("refundState"))) {
					// 退款失败
					t_payinfoDto.setJg("2");
					payinfoService.insertPayinfoDto(t_payinfoDto);
					resultMap.put("status", "fail");
					resultMap.put("message", "退款失败!");
				}else {
					// 其它状态
					t_payinfoDto.setJg("9");
					t_payinfoDto.setCwxx("退款处理状态：" + returnCont.get("refundState"));
					payinfoService.insertPayinfoDto(t_payinfoDto);
					resultMap.put("status", "fail");
					resultMap.put("message", "退款处理状态异常! "+returnCont.get("refundState"));
				}
				jkdymxDto.setQtxx("REFUND"); // 其他信息
				jkdymxDto.setSfcg("1");
				insertJkdymxDto(jkdymxDto); // 同步新增
				return resultMap;
            }else {
            	String errCode = String.valueOf(response.get("errCode"));
				String respMsg = String.valueOf(response.get("respMsg"));
				log.error("退款申请失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
				throw new BusinessException("msg", "退款申请失败！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
			}
        } catch (BusinessException e) {
        	jkdymxDto.setQtxx(e.getMsg());
			insertJkdymxDto(jkdymxDto); // 同步新增
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e){
			jkdymxDto.setQtxx("退款申请异常！");
			insertJkdymxDto(jkdymxDto); // 同步新增
            log.error("退款申请异常！" + e.toString());
            throw new BusinessException("msg", "退款申请异常！");
        }
	}

	/**
	 * 退款结果查询
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public Map<String, Object> refundResultInquire(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> requestPublicParams = new TreeMap<>();
			// 公共请求参数
			requestPublicParams.put("version", "0.0.1"); // 版本号，固定为0.0.1(必传字段)
			requestPublicParams.put("encoding", "UTF-8"); // 编码方式，固定为UTF-8(必传)
			requestPublicParams.put("signMethod", "01"); // 签名方法，固定为01，表示签名方式为RSA2(必传)
			// 业务要素
			Map<String, String> requestTransactionParams = new HashMap<>();
			requestTransactionParams.put("merId", merid); // 商户号(必传)
			requestTransactionParams.put("orderId", payinfoDto.getDdbh()); // 商户退款订单号
			requestTransactionParams.put("cmbOrderId", payinfoDto.getPtddh()); // 平台退款订单号
			requestTransactionParams.put("userId", userid); // 收银员(必传)
			requestPublicParams.put("biz_content", mapper.writeValueAsString(requestTransactionParams));
			log.info("加签前的报文内容：" + mapper.writeValueAsString(requestPublicParams));

			// 对待加签内容进行排序拼接
			String signContent = SignatureUtil.getSignContent(requestPublicParams);
			// 私钥加签
			requestPublicParams.put("sign", SignatureUtil.rsaSign(signContent, private_key, "UTF-8"));
			String requestStr = mapper.writeValueAsString(requestPublicParams);
			log.info("加签后的报文内容：" + requestStr);

			@SuppressWarnings("unchecked")
			Map<String, String> signResultMap = mapper.readValue(requestStr, Map.class);

			// 组apiSign加密Map
			String timestamp = "" + System.currentTimeMillis() / 1000;
			Map<String, String> apiSign = new TreeMap<>();
			apiSign.put("appid", appid);
			apiSign.put("secret", secret);
			apiSign.put("sign", signResultMap.get("sign"));
			apiSign.put("timestamp", timestamp);

			// MD5加密
			String MD5Content = SignatureUtil.getSignContent(apiSign);
			String apiSignString = MD5Utils.getMD5Content(MD5Content).toLowerCase();

			// 组request头部Map
			Map<String, String> apiHeader = new HashMap<>();
			apiHeader.put("appid", appid);
			apiHeader.put("timestamp", timestamp);
			apiHeader.put("apisign", apiSignString);

			// 发送HTTP post请求
			Map<String, String> response = HttpUtil.postForEntity(restTemplate, Constant.REFUND_QUERY_URL, requestStr, apiHeader);

			// 返回结果验签
			Boolean checkResult = checkSign(mapper.writeValueAsString(response));
			if (!checkResult) {
				log.error("refundResultInquire 报文验签失败!");
				resultMap.put("status", "fail");
				resultMap.put("message", "refundResultInquire 报文验签失败!");
				return resultMap;
			}
			
			if (StringUtil.isNotBlank(response.get("respCode")) && "SUCCESS".equals(response.get("respCode"))) {
				@SuppressWarnings("unchecked")
				Map<String, String> returnCont = JSON.parseObject(response.get("biz_content"), HashMap.class);
				if("S".equals(returnCont.get("tradeState"))) {
					// 退款成功
					payinfoDto.setJg("1");
					payinfoDto.setJyje(returnCont.get("refundAmt"));
					payinfoService.updatePayinfoDto(payinfoDto);
					// 判断业务类型
					//
					if(BusinessTypeEnum.SJ.getCode().equals(payinfoDto.getYwlx())) {
						// 修改送检表支付信息
						SjxxDto sjxxDto = new SjxxDto();
						sjxxDto.setSjid(payinfoDto.getYwid());
						SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto, 2);
						String sfje = selectPaidAmount(payinfoDto.getYwid(), BusinessTypeEnum.SJ.getCode());
						sjxxDto.setSfje(sfje);
						// 判断，实付金额大于0时不修改付款标记
						if(new BigDecimal(sfje).compareTo(BigDecimal.ZERO) != 1) {
							sjxxDto.setFkbj("0");
						}else {
							sjxxDto.setFkbj("1");
							sjxxDto.setFkrq(t_sjxxDto.getFkrq());
						}
						sjxxService.modInspPayinfo(sjxxDto,payinfoDto.getZfid());
					}else if(BusinessTypeEnum.FJ.getCode().equals(payinfoDto.getYwlx())) {
						// 修改复检表支付信息
						FjsqDto fjsqDto = new FjsqDto();
						fjsqDto.setFjid(payinfoDto.getYwid());
						String sfje = selectPaidAmount(payinfoDto.getYwid(), BusinessTypeEnum.SJ.getCode());
						fjsqDto.setSfje(sfje);
						// 判断，实付金额大于0时不修改付款标记
						if(new BigDecimal(sfje).compareTo(BigDecimal.ZERO) != 1) {
							fjsqDto.setFkbj("0");
						}else {
							fjsqDto.setFkbj("1");
						}
						fjsqDto.setYwlx(PayTypeEnum.REFUND.getCode());
						// 同步复检支付信息
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.FDIF_MOD.getCode() + com.alibaba.fastjson.JSONObject.toJSONString(fjsqDto));
					}
					resultMap.put("status", "success");
					resultMap.put("message", "退款成功!");
				}else if("F".equals(returnCont.get("tradeState"))) {
					// 退款失败
					payinfoDto.setJg("2");
					payinfoService.updatePayinfoDto(payinfoDto);
					resultMap.put("status", "fail");
					resultMap.put("message", "退款失败!");
				}else if("P".equals(returnCont.get("tradeState"))){
					// 预退款完成 不做操作
					log.error(payinfoDto.getDdbh() + " 预退款完成!");
					resultMap.put("status", "success");
					resultMap.put("message", "预退款完成!");
				}else {
					// 未知状态
					payinfoDto.setJg("9");
					payinfoDto.setCwxx("退款处理状态：" + returnCont.get("refundState"));
					payinfoService.updatePayinfoDto(payinfoDto);
					resultMap.put("status", "fail");
					resultMap.put("message", "退款处理状态异常! "+ returnCont.get("refundState"));
				}
				return resultMap;
			} else {
				String errCode = response.get("errCode");
				String respMsg = response.get("respMsg");
				log.error(payinfoDto.getDdbh() + " 未查询到退款信息！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
				throw new BusinessException("msg", payinfoDto.getDdbh() + " 未查询到退款信息！" + (StringUtil.isNotBlank(errCode)?errCode:"") +" "+ (StringUtil.isNotBlank(respMsg)?respMsg:""));
			}
		} catch (BusinessException e) {
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e) {
			log.error("退款查询异常！" + e.toString());
	    	throw new BusinessException("msg", "退款查询异常！");
		}
	} 

	/**
	 * 验证签名
	 * @param string
	 * @return
	 * @throws BusinessException 
	 */
    private boolean checkSign(String string) throws BusinessException{
        try {
            //公钥
        	String publicKey = cmb_public_key;
        	//验签
            ObjectMapper objectMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
			Map<String, String> responseBodyMap = objectMapper.readValue(string, Map.class);
            String sign = responseBodyMap.remove("sign");
            String contentStr = SignatureUtil.getSignContent(responseBodyMap);
            return SignatureUtil.rsaCheck(contentStr, sign, publicKey, "UTF-8");
        }catch (Exception e){
        	log.error("验签发生异常！"+e.toString());
        	throw new BusinessException("msg", "验签发生异常！");
        }
    }

    /**
     * 报文组装Map
     * @param str
     * @return
     */
    private Map<String, String> str2Map(String str) {
        Map<String, String> result = new HashMap<>();
        String[] results = str.split("&");
        if (results != null && results.length > 0) {
            for (int var = 0; var < results.length; ++var) {
                String pair = results[var];
                String[] kv = pair.split("=", 2);
                if (kv != null && kv.length == 2) {
                    result.put(kv[0], kv[1]);
                }
            }
        }
        return result;
    }

    /**
     * 新增接口调用信息(同步)
     * @param jkdymxDto
     */
    private void insertJkdymxDto(JkdymxDto jkdymxDto) {
    	jkdymxService.insertJkdymxDto(jkdymxDto); 
    	// 同步新增
    	amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.ADD_TRANSFER.getCode(), com.alibaba.fastjson.JSONObject.toJSONString(jkdymxDto));
	}

	/**
	 * 发送支付消息通知
	 * @param restTemplate 
	 * @param payinfoDto
	 */
    private void sendPayMessage(RestTemplate restTemplate, PayinfoDto payinfoDto) {
    	log.error("sendPayMessage 支付消息通知  zfid:"+payinfoDto.getZfid());
    	if(StringUtil.isBlank(payinfoDto.getLrry())) {
    		log.error("未获取到录入人员信息！ zfid=" + payinfoDto.getZfid());
    		return;
    	}
		//默认杰毅医检公众号
		String wbcxid = payinfoDto.getWbcxid();
		String wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		if (StringUtil.isNotBlank(wbcxid)){
			Object info = redisUtil.hget("WbcxInfo", wbcxid);
			if (info != null){
				//若redis数据库中存在外部程序信息，则设置appkey,apppsecret,agentid,corpid,appid
				com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(info.toString());
				wbcxdm = jsonObject.getString("wbcxdm");
			}
		}else {
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			wbcxid = wbcxDto.getWbcxid();
		}
		payinfoDto.setGzpt(wbcxid);

    	// 判断支付方式
    	String zffs = null;
    	if("ZF".equals(payinfoDto.getZffs()))
    		zffs = "支付宝";
    	if("WX".equals(payinfoDto.getZffs()))
    		zffs = "微信";
    	if("YL".equals(payinfoDto.getZffs()))
    		zffs = "银联";
		// 查询接收人员信息
    	List<WxyhDto> wxyhDtos = wxyhservice.receivePayMsgUser(payinfoDto);
    	if(wxyhDtos != null && wxyhDtos.size() > 0) {
			String remark = xxglService.getMsg("ICOMM_SJ00058");
    		for (int i = 0; i < wxyhDtos.size(); i++) {
	    		// 支付完成通知 
	    		log.error("sendPayMessage 支付完成通知  zfid:"+payinfoDto.getZfid());
				remark = StringUtil.replaceMsg(remark, payinfoDto.getHzxm(), payinfoDto.getYbbh());
	    		// 支付金额
	    		String zfje = String.valueOf(new BigDecimal(payinfoDto.getJyje()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr=dateFormat.format(new Date());
				Map<String, String> messageMap = new HashMap<>();
				messageMap.put("title", "支付成功");
				messageMap.put("keyword1", zfje + "(元)");
				messageMap.put("keyword2", remark);
				messageMap.put("keyword3", zffs);
				messageMap.put("keyword4", payinfoDto.getDdbh());
				messageMap.put("keyword5", dateStr);
				String result =  WeChatUtils.sendWeChatMessageMap(redisUtil, restTemplate, "TEMPLATE_NEWPAY_SUCCESS", wxyhDtos.get(i).getWxid(), wbcxdm,messageMap);
				log.error("sendPayMessage 支付通知结果  zfid:"+payinfoDto.getZfid()+"result:"+result);
	    		/*// 退款完成通知
	    		log.error("sendPayMessage 退款完成通知  zfid:"+payinfoDto.getZfid());
	    		remark = StringUtil.replaceMsg(remark, payinfoDto.getHzxm(), payinfoDto.getYbbh());
	    		// 退款金额
	    		String tkje = String.valueOf(new BigDecimal(payinfoDto.getJyje()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
	    		WeChatUtils.sendWeChatMessage(restTemplate, dbEncrypt.dCode(Constant.TEMPLATE_REFUND_SUCCESS), wxyhDtos.get(i).getWxid(), "退款成功", 
	    				payinfoDto.getDdbh(), tkje + "(元)", null, null, remark, null, ProgramCodeEnum.MDINSPECT.getCode());*/
			}
    	}else {
    		log.error("未获取到通知人员！zfid=" + payinfoDto.getZfid());
    	}
	}

    /**
	 * 发送支付消息通知(钉钉)
	 * @param payinfoDto
	 */
	private void sendDdPayMessage(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		log.error("sendPayMessage 支付消息通知  zfid:"+payinfoDto.getZfid());
    	if(StringUtil.isBlank(payinfoDto.getLrry())) {
    		log.error("未获取到录入人员信息！ zfid=" + payinfoDto.getZfid());
    		return;
    	}
    	
    	// 判断支付方式
    	String zffs = null;
    	if("ZF".equals(payinfoDto.getZffs())) {
            zffs = "支付宝";
        }
    	if("WX".equals(payinfoDto.getZffs())) {
            zffs = "微信";
        }
    	if("YL".equals(payinfoDto.getZffs())) {
            zffs = "银联";
        }
		
    	// 支付金额
		String zfje = String.valueOf(new BigDecimal(payinfoDto.getJyje()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
    	// 发送钉钉消息, 需要本地发送
    	log.error("sendDdPayMessage -- 支付方式:" + zffs+" 录入人员:"+payinfoDto.getLrry()+" 支付金额:"+zfje+" 患者信息:"+payinfoDto.getHzxm()+payinfoDto.getYbbh());	
//    	String token = talkUtil.getToken();
    	String title = xxglService.getMsg("ICOMM_ZF00002");
		String message = xxglService.getMsg("ICOMM_ZF00001", payinfoDto.getYbbh(), payinfoDto.getHzxm(), payinfoDto.getSjhb(), zffs, zfje+"(元)", DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
    	talkUtil.sendWorkMessage(payinfoDto.getLrry(), payinfoDto.getLrry(), title, message);
	}

	/**
	 * 获取当前实付金额
	 * @param ywid
	 * @param ywlx
	 * @return
	 */
	private String selectPaidAmount(String ywid, String ywlx) {
		// TODO Auto-generated method stub
		PayinfoDto payinfoDto = new PayinfoDto();
		payinfoDto.setYwid(ywid);
		payinfoDto.setYwlx(ywlx);
		payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
		return payinfoService.selectPaidAmount(payinfoDto);
	}
	
	/**
	 * 查询送检退款情况，每8h一次(1点，9点，17点)
	 */
	public void refundSelect() {
		// 查询退款中订单(最近15天)
		PayinfoDto payinfoDto = new PayinfoDto();
		payinfoDto.setDylx(PayTypeEnum.REFUND.getCode());
		List<PayinfoDto> payinfoList = payinfoService.getRefundOrders(payinfoDto);
		if(payinfoList != null && payinfoList.size() > 0) {
			for (int i = 0; i < payinfoList.size(); i++) {
				try {
					refundResultInquire(restTemplate, payinfoList.get(i));
				} catch (BusinessException e) {
					log.error(payinfoList.get(i).getZfid() + "定时退款查询异常：" + e.getMsg());
				} catch (Exception e) {
					log.error(payinfoList.get(i).getZfid() + "定时退款查询异常："+ e.toString());
				}
			}
		}
	}

	/**
	 * 根据ywid关闭订单
	 * @param payinfoDto
	 * @return
	 */
	public Map<String,Object> closeOrdersByYwid(PayinfoDto payinfoDto) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if (StringUtil.isNotBlank(payinfoDto.getYwlx())){
			payinfoDto.setYwlx(BusinessTypeEnum.SJ.getCode());
		}
		List<PayinfoDto> payinfoList = payinfoService.selectPayOrders(payinfoDto);
		if(payinfoList != null && payinfoList.size() > 0) {
			for (int i = 0; i < payinfoList.size(); i++) {
				try {
					resultMap = closeOrder(restTemplate, payinfoList.get(i));
					if (resultMap.get("status") != null && "fail".equals(resultMap.get("status"))) {
						// 关闭订单成功
						return resultMap;
					}
				} catch (BusinessException e) {
					resultMap.put("status", "fail");
					resultMap.put("message", payinfoList.get(i).getPtddh() + "关闭订单异常：" + e.getMsg());
				} catch (Exception e) {
					log.error(payinfoList.get(i).getPtddh() + "关闭订单异常："+ e.toString());
					resultMap.put("status", "fail");
					resultMap.put("message", payinfoList.get(i).getPtddh() + "关闭订单异常："+ e.toString());
				}
			}
		}
		resultMap.put("status", "success");
		resultMap.put("message", "无可关闭订单!");
		return resultMap;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.error("---------------PayInfo查询状态线程---------------");
		XtszDto xtszDto = xtszService.selectById("pay.result.flag");
		if (null != xtszDto && StringUtil.isNotBlank(xtszDto.getSzz())){
			if ("1".equals(xtszDto.getSzz())){
				log.error("---------------开启线程PayInfo---------------");
				SelectPayResultThread selectPayResultThread = new SelectPayResultThread(payinfoService, this, xtszService, restTemplate);
				selectPayResultThread.start();
			}
		}
	}
}
