package com.matridx.server.wechat.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.util.RabbitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.server.wechat.dao.entities.AlipayBean;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IAliService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class AliServiceImpl implements IAliService{

	// 阿里服务器的应用ID
	@Value("${matridx.alipay.app_id}")
	private String alipay_app_id;
	// 阿里服务器的商户私钥
	@Value("${matridx.alipay.alipay_private_key}")
	private String alipay_private_key;
	// 阿里服务器的商户公钥
	@Value("${matridx.alipay.alipay_public_key}")
	private String alipay_public_key;
	// 阿里服务器的支付宝网关
	@Value("${matridx.alipay.gatewayUrl}")
	private String alipay_gatewayUrl;
	// 阿里服务器的签名方式
	@Value("${matridx.alipay.alipay_sign_type}")
	private String alipay_sign_type;
	// 页面跳转同步通知页面路径
	@Value("${matridx.alipay.return_url}")
	private String return_url;
	// 服务器异步通知页面路径
	@Value("${matridx.alipay.notify_url}")
	private String notify_url;
	// 服务器商户
	//@Value("${matridx.alipay.uid}")
	//private String uid;
	
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Autowired
	ISjxxService sjxxService;
	
	@Autowired
	IPayinfoService payinfoService;
	
	@Autowired
	ISjhbxxService sjhbxxService;
	
	private Logger log = LoggerFactory.getLogger(AliServiceImpl.class);

	/**
	 * 进行阿里支付
	 * 
	 * @param sjxxDto
	 */
	public String createAliPayOrder(SjxxDto sjxxDto) {
		try {
			//只取第一页的送检检测项目
			SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto);
			
			if(t_sjxxDto==null)
				return null;
			if("1".equals(t_sjxxDto.getFkbj()))
				return null;
			DBEncrypt dbEncrypt = new DBEncrypt();
			
			// 1、获得初始化的AlipayClient
			AlipayClient alipayClient = new DefaultAlipayClient(alipay_gatewayUrl, // 支付宝网关
					dbEncrypt.dCode(alipay_app_id), // appid
					dbEncrypt.dCode(alipay_private_key), // 商户私钥
					"json", "UTF-8", // 字符编码格式
					dbEncrypt.dCode(alipay_public_key), // 支付宝公钥
					alipay_sign_type // 签名方式
			);
			// 2、设置请求参数
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
			// 页面跳转同步通知页面路径
			alipayRequest.setReturnUrl(return_url);
			// 服务器异步通知页面路径
			alipayRequest.setNotifyUrl(notify_url);

			AlipayBean alipayBean = new AlipayBean();
			//生成订单号
			if(StringUtil.isBlank(t_sjxxDto.getDdh())){
				t_sjxxDto.setDdh(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (int) (Math.random() * 90000 + 88888));

				//如果付款金额里没有信息，则按照现有的信息进行更新
				if(StringUtil.isBlank(t_sjxxDto.getFkje())) {
					t_sjxxDto.setFkje(sjxxDto.getFy());
				}
				//保存至微信服务端
				boolean isSuccess = sjxxService.update(t_sjxxDto);
				if(!isSuccess)
					return null;
			}
            alipayBean.setOut_trade_no(t_sjxxDto.getDdh());
			//设置标题
			alipayBean.setSubject("杰毅生物--医疗服务*检测费");
			
			//设置金额
			alipayBean.setTotal_amount(sjxxDto.getFy());
			
			if(StringUtil.isNotBlank(t_sjxxDto.getSfflg()) && !"1".equals(t_sjxxDto.getFkbj())) {
				SjhbxxDto sjhbxxDto = new SjhbxxDto();
				sjhbxxDto.setHbmc(t_sjxxDto.getDb());
				SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
				if(re_sjhbxxDto!=null)
					alipayBean.setTotal_amount(re_sjhbxxDto.getSfbz());
			}else if("1".equals(t_sjxxDto.getFkbj())){
				return "Faild";
			}
			
			String bzString = "用于标本编号：" + t_sjxxDto.getYbbh() + " 姓名：" + t_sjxxDto.getHzxm() + " 的检测项目：" + t_sjxxDto.getJcxmmc();
			//设置备注
			alipayBean.setBody(bzString);
			
			//封装参数 
			alipayRequest.setBizContent(JSON.toJSONString(alipayBean));
			//3、请求支付宝进行付款，并获取支付结果 
			String result = alipayClient.pageExecute(alipayRequest).getBody(); 
			//返回付款信息 
			return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝支付失败，需要把数据保存到数据库中，同时通知管理端
	 */
	@Override
	public String AliPayFaild(HttpServletRequest request) {
		
		//插入信息数据库,同时通知后台
		//检查信息是否正常
		Map<String, String> resultMap = checkInputSign(request);
		//插入信息数据库,同时通知后台
		if (resultMap!=null)
		{
			PayinfoDto payinfoDto = new PayinfoDto();
			
			payinfoDto.setZfid(StringUtil.generateUUID());
			//支付类型为支付宝
			payinfoDto.setDylx("ALI");
			payinfoDto.setDyjk("AliPayFaild");
			// payinfoDto.setHdxx(resultMap.get("matridx_param"));
			//获取订单号
			String ddh = resultMap.get("out_trade_no");
			
			if(StringUtil.isBlank(ddh)) {
				payinfoDto.setJg("0");
				payinfoService.insertPayinfoDto(payinfoDto);
				return null;
			}
			payinfoDto.setJg("0");
			// payinfoDto.setTzlx("Faild");
			
			payinfoService.insertPayinfoDto(payinfoDto);
			
		}
		return null;
	}

	/**
	 * 支付宝支付通知，需要把数据保存到数据库中，同时通知管理端
	 */
	@Override
	public String aliPayNotify(HttpServletRequest request) {
		//检查信息是否正常
		Map<String, String> resultMap = checkInputSign(request);
		//插入信息数据库,同时通知后台
		if (resultMap!=null)
		{
			PayinfoDto payinfoDto = new PayinfoDto();
			
			payinfoDto.setZfid(StringUtil.generateUUID());
			//支付类型为支付宝
			payinfoDto.setDylx("ALI");
			payinfoDto.setDyjk("aliPayNotify");
			// payinfoDto.setHdxx(resultMap.get("matridx_param"));
			
			//获取订单号
			String ddh = resultMap.get("out_trade_no");
			String tradeStatus = resultMap.get("trade_status");
			if(StringUtil.isBlank(ddh)) {
				payinfoDto.setJg("0");
				payinfoService.insertPayinfoDto(payinfoDto);
				return "fail";
			}
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setDdh(ddh);
			//获取系统当前时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = df.format(new Date());
			switch (tradeStatus) // 判断交易结果
			{
				case "TRADE_FINISHED": // 完成
					//付款标记 0：未付款 1：已付款 2：部分付款
					sjxxDto.setFkbj("1");
					sjxxDto.setFkrq(date);
					payinfoDto.setJg("1");
					// payinfoDto.setTzlx("TRADE_FINISHED");
					break;
				case "TRADE_SUCCESS": // 完成
					//付款标记 0：未付款 1：已付款 2：部分付款
					sjxxDto.setFkbj("1");
					sjxxDto.setFkrq(date);
					payinfoDto.setJg("1");
					// payinfoDto.setTzlx("TRADE_SUCCESS");
					break;
				case "WAIT_BUYER_PAY": // 待支付
					//付款标记 0：未付款 1：已付款 2：部分付款
					sjxxDto.setFkbj("0");
					payinfoDto.setJg("0");
					// payinfoDto.setTzlx("WAIT_BUYER_PAY");
					break;
				case "TRADE_CLOSED": // 交易关闭
					//付款标记 0：未付款 1：已付款 2：部分付款
					sjxxDto.setFkbj("0");
					payinfoDto.setJg("0");
					// payinfoDto.setTzlx("TRADE_CLOSED");
					break;
				default:
					break;
			}
			
			payinfoService.insertPayinfoDto(payinfoDto);
			
			sjxxService.updateByDdh(sjxxDto);
			
			//付款成功，发送消息
			if("1".equals(sjxxDto.getFkbj())){
				sjxxDto = sjxxService.getDtoByDdh(sjxxDto);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_RES.getCode() + JSONObject.toJSONString(sjxxDto));
			}
			return "success";
		}
		return "fail";
	}

	/**
	 * 支付宝支付成功，需要把数据保存到数据库中，同时通知管理端
	 */
	@Override
	public String aliPayComplete(HttpServletRequest request,ModelAndView mv) {
		//检查信息是否正常
		Map<String, String> resultMap = checkInputSign(request);
		//插入信息数据库,同时通知后台
		if (resultMap!=null)
		{
			//获取订单号
			String ddh = resultMap.get("out_trade_no");
			
			if(StringUtil.isBlank(ddh)) {
				return null;
			}
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setDdh(ddh);
			SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto);
			
			if(t_sjxxDto !=null) {
				mv.addObject("hzxm",t_sjxxDto.getHzxm());
				mv.addObject("yblxmc",t_sjxxDto.getYblxmc());
				mv.addObject("jcxmmc",t_sjxxDto.getJcxmmc());
			}
		}
		return null;
	}

	/**
	 * 检查信息的sign
	 * @param request
	 * @return
	 */
	private Map<String, String> checkInputSign(HttpServletRequest request) {
		// TODO Auto-generated method stub

		DBEncrypt dbEncrypt = new DBEncrypt();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		StringBuffer paramString = new StringBuffer("");
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();){
			String name = (String) iter.next();
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++){
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			paramString.append(name);
			paramString.append("=");
			paramString.append(valueStr);
			paramString.append(";");
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		boolean flag = true; // 校验公钥正确性防止意外
		try{
			flag = AlipaySignature.rsaCheckV1(params,  dbEncrypt.dCode(alipay_public_key), "UTF-8", "RSA2");
		} catch (AlipayApiException e){
			e.printStackTrace();
			log.error("验证未通过：" + paramString.toString());
			return null;
		}
		params.put("matridx_param", paramString.toString());
		System.out.println("");
		if(flag)
			return params;
		else
			return null;
	}

	/**
	 * 生成订单二维码(链接)
	 */
	@Override
	public String createOrderQRCode() {
		// TODO Auto-generated method stub
		try {
			//获得初始化的AlipayClient
			//沙箱
//			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", 
//					"2021000116672801", // appid
//					"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCcHFFElgLNL6Y4FIvvqgHVljavbT7347WsRb/FQWcc1xdp39kqsyLWqyobIZhX27tYyYsemoV33KgHPdBRQYJpIGWONovic4x8Cti37lSjEeOwI1abfHXp+x0vgW4iHW2zfa7Ldn9x24kzN2IVoIi8qQ6IHdGJ5Q5EfTyLKTnJpmgTpaFD8XfXV/pY9fwSa4HzEkoJBAJ4rKMHYchF9e6FgiKBE4k0ntdP74//zKF/gAAppSTbtw1k6sP8lliOW0VQZhl0Gzs+F/dZlapAFXv+XHUJS11h3wWZdLYNp1hvWXWDBNrZMQ0InCuST0wLReYYB58k8ggrmhzR5ogzbWYJAgMBAAECggEAGxwbzNJUODe/INwIlR0r5mCIrJxHJBtKMFmIfQPnmS2NJli2SOEE+syMqluCLje9aCTP4Qeqh+8syt0kMQg0UndKy5CTaEbyrZPgoMhlsE/p/2uTnbrWa0Stg9NxQcFkjJgV48V3Sll6kQdcJC9Vr5WYLXEoM08w1Bn/ipclLuxOhvBnqqdLTnfjZIFRh3zXq7A7zdeU7KmbguWtf6AtWnD7TqMfDu5XksFA0ll4tDaXh1GpCaINmZ9Ic65KwsduYFHgBr/OYwx2UnZ2bu9EBgayWp5/tKslB3mBF7EDK973djBb1WEC3br8+50boFSD+e5zc/7/Dp25YoOTD9+mFQKBgQDQPfj7dJeoo24TJHEnyLZeOTC/W2Sfb1TiNB+FoyN6kH3P6cEf1W51AQtULQw0Leblw0unN0LCPD5zZUtuLkuvgOYqcnIOJamGhvCN0BhVYok0Lty4EtQpCAyhqlo5usbQPLhzJbvCnMKjQrsn5WWcRIgNLTS3R30Ca+J8fgYZuwKBgQC/6aw6q+bYaHRVApyr9hFh4vTgCjKc6Dhe5iv10D53xGDMsfHacjWonv7JIx1kWdL2aXjQ/0gQ3NmGfI2wbhezsAxJPKSVmqaCTVR/CmRUfTe5TK5AGyzsesQWnEC04B4Nu0T1jT63NM+kR4YfTU3kmENS1y5eJgZicLOBbiCxCwKBgCFpQBCARXD+P8P8fddHQdNeU67wJ/XYtALTIBed9CxPEJCSTbUg1mcZcsrfq2yXzMsiiXnbyO9HITH8l5ym+ue+w68dy6TvcAKwldn7AFx6wc7IkgtKohWvP++UWIOYrxtsEqAO9tAjvKrbIxIkoSfwau/KSAH8C1efHaihw7LPAoGBAKDv5cXiCJmspvnvUdGHbjZHbJFBHAanWzaCEZV3iBtJ7N9bznUhV8xLYWDteKWCCj8oXe/uVNIHUsfMRcoR2QCP/w2ftGndIgudq+FJpq+Pvp4/JtWNtJOvtCc966mDVNCxHfhJ9bEPsZ2TeW9glZTA95xN5Y2Al9QPrQk6gGHXAoGAfSM6ahO6PPRhEpLtU454aNgyF58b9u9yqws8upQdkZK3fGNXDKidCRyQsWLardsIdHV9I1LGxfHWzsdVorj9jocx9RaKFtx9AYBR08sNl0jSjTO5OAcyyu56nGxIaJLBHjXHWkLaE1j/LgWO/zuHyvQAJbNvGMG1eZ6E2em3GSM=", // 商户私钥
//					"json", "UTF-8", // 字符编码格式
//					"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwtGKzHBzS8OXG+Rx15dZqyC8xxC7kdNI2ipaIi4CRWsmbbDeyOWVN+UjCKFLT5i59VfqyykBZORJ6ZwxsqetD49tV0fhxjbebU13fIMj6MEOtZeY6lHLPFyOv4usjAAi+73hDUIQ3lI6jeoXZPVx8Rrygrci9HtrgHJBMuZOm3GvNZnVsj78ArdzQBEpG9Q5SgVBXFFzwJ0xhYXEQGubN2pAXx/2BpNKHx6nStKQHtMYJ81uYwG2Is9YJaWuI+lDJiMvADq9VzteSDawStasG2+XfBz0MoH9CIl5TGVliYrj/XXQaY7MVYKYgfGK3xtetZuJw6ZFd8ROKWXxTapadwIDAQAB", // 支付宝公钥
//					"RSA2"); 
			// 公司
			DBEncrypt dbEncrypt = new DBEncrypt();
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", 
					dbEncrypt.dCode("bfjkM4wFddO+IHhOeV1r0DJVhPyCTTb2m8Ia5X4cJ9k="), // appid
					dbEncrypt.dCode("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"), // 商户私钥
					"json", "UTF-8", // 字符编码格式
					dbEncrypt.dCode("bB/tVjlQkgeF4NVVrnTY2fk2I3CMOmGch9nVgSKatmPYWAj+KzEMb0WeTmsL3FdZRTZP+pvyr4WivXG1Q/AtEoLmX1kuHnJf1RGn7chI5/Bj5Gwnc5Gx6SxSiFAwBrGGTYlWADREvtZJkRiQ7O99pz3icE2sa3L3lvlKWwUOMeD8rpVY4eu3Xv5O6wDrs2rlt0TRp2GS/okVue6UkJvazi2hKNkrVYisszo4OHUOqsNTnCtSi2eze0Yqm2NTxQ9ZeQ2SyoxlrlsBX7yoczuBNtKDUOD68nhcag0bCnYsE9uwdQpDbny08+u3TeuAJpj+VxeTmq3fB7yOZ9MDUsayJ/81KUv8Q5D6KI2GRQgElh6pUB4knX2FrAoWRZ7//LAvsLvjAwbxMN2/3ptS8HxQfYo8c6T/U0KwEYx0CgutdYK/TvIyhg0lXFOyC/Vzqmt5gyRaJazD6CRd2mcduQ72Ff4gO4sh4oilzz/P8dZAnsviRSL1uiVBxeNpfj9aiB5q49GOlYlTjmvUzVxXwpde+w=="), // 支付宝公钥
					"RSA2"); 
			//创建API对应的request类
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
			//设置业务参数，out_trade_no 与 trade_no 必填一个
	        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
	        model.setOutTradeNo("202012170000000001"); // 商户订单号 
	        model.setTotalAmount("0.01"); // 订单金额
	        model.setSubject("测试支付"); // 商品的标题/交易标题/订单标题/订单关键字等。不可使用特殊字符，如 /,=,& 等
//	        model.setStoreId("9527"); // 商户门店编号，如果没有店铺号可不设置
	        model.setQrCodeTimeoutExpress("10m"); // 订单允许的最晚付款时间 
	        request.setBizModel(model);
//	        request.setNotifyUrl(aliNotifyUrl); //支付宝异步通知地址
	        
	        // 通过alipayClient调用API，获得对应的response类
	        AlipayTradePrecreateResponse response = alipayClient.execute(request);
	        log.info(response.getBody());
	        log.info("支付二维码qrCode：{} ", response.getQrCode());
			// 返回二维码路径
			return response.getQrCode();
		} catch (Exception e) {
			log.error(e.toString());
		}
		return null;
	}

}
