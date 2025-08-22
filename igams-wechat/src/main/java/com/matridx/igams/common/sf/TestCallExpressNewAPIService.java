package com.matridx.igams.common.sf;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.matridx.igams.common.sf.CallExpressServiceTools;
import com.matridx.igams.common.sf.EspServiceCode;
import com.matridx.igams.common.sf.HttpClientUtil;

public class TestCallExpressNewAPIService {
	private static final String CLIENT_CODE = "JYSWKgDn2";  //此处替换为您在丰桥平台获取的顾客编码
	private static final String CHECK_WORD = "3iCqk3sADzpIo4MisaNfMWglHVoK8pjo";//此处替换为您在丰桥平台获取的校验码
	
//	private static final String CLIENT_CODE = "JYSWJW";  //此处替换为您在丰桥平台获取的顾客编码
//	private static final String CHECK_WORD = "x303u6lj59tqMKjBtEqqZjHCCgYqH40w";//此处替换为您在丰桥平台获取的校验码
	
//	沙箱环境的地址
	private static final String CALL_URL_BOX = "https://sfapi-sbox.sf-express.com/std/service";
	//生产环境的地址
//	private static final String CALL_URL_PROD = "https://sfapi.sf-express.com/std/service";
    
	public static void main(String[] args) throws UnsupportedEncodingException {
//			EspServiceCode testService = EspServiceCode.EXP_RECE_CREATE_ORDER; //下订单
//			EspServiceCode testService = EspServiceCode.EXP_RECE_SEARCH_ORDER_RESP; //查订单
		//    EspServiceCode testService = EspServiceCode.EXP_RECE_UPDATE_ORDER;//订单取消
	   //	EspServiceCode testService = EspServiceCode.EXP_RECE_FILTER_ORDER_BSP;//订单筛选
	      EspServiceCode testService = EspServiceCode.EXP_RECE_SEARCH_ROUTES;//查路由
		//	EspServiceCode testService = EspServiceCode.EXP_RECE_GET_SUB_MAILNO;//子单号
		//	EspServiceCode testService = EspServiceCode.EXP_RECE_QUERY_SFWAYBILL;//查清单运费
	  //	EspServiceCode testService = EspServiceCode.EXP_RECE_REGISTER_ROUTE;//注册路由	
		//	EspServiceCode testService = EspServiceCode.EXP_RECE_CREATE_REVERSE_ORDER;//退货下单
		//	EspServiceCode testService = EspServiceCode.EXP_RECE_CANCEL_REVERSE_ORDER;//退货消单
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_WANTED_INTERCEPT;//截单转寄
        //	EspServiceCode testService = EspServiceCode.EXP_RECE_QUERY_DELIVERTM;//时效标准及价格查询
		//	EspServiceCode testService = EspServiceCode.COM_RECE_CLOUD_PRINT_WAYBILLS;//云打印面单接口
			

		
		 CallExpressServiceTools client=CallExpressServiceTools.getInstance();    
		
        // set common header
        Map<String, String> params = new HashMap<String, String>();
        
        String timeStamp = String.valueOf(System.currentTimeMillis());
//        timeStamp = "12312334453453";
        String msgData = client.packageMsgData(testService);
        
//        String digest = xx(msgData,timeStamp);
        
        params.put("partnerID", CLIENT_CODE);  // 顾客编码 ，对应丰桥上获取的clientCode
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode",testService.getCode());// 接口服务码
        params.put("timestamp", timeStamp);    
        params.put("msgData", msgData);      
        params.put("msgDigest", client.getMsgDigest(msgData,timeStamp,CHECK_WORD));//digest
//        System.out.println("ballballyou-----------"+client.getMsgDigest(msgData,timeStamp,CHECK_WORD));
        
       // System.out.println(params.get("requestID"));
        long startTime = System.currentTimeMillis();
        
        System.out.println("====调用请求：" + params.get("msgData"));
        String result = HttpClientUtil.post(CALL_URL_BOX, params);
        
        System.out.println("====调用丰桥的接口服务代码：" + String.valueOf(testService.getCode()) + " 接口耗时："+ String.valueOf(System.currentTimeMillis()-startTime)+"====");
        System.out.println("===调用地址 ==="+CALL_URL_BOX);
        System.out.println("===顾客编码 ==="+CLIENT_CODE);
        System.out.println("===返回结果：" +result);
     
	}

//public static String xx(String msgData,String timeStamp) {
//	//
//	//客户校验码    使用顺丰分配的客户校验码
//	String checkWord1 = CHECK_WORD;
//	//时间戳 取报文中的timestamp（调用接口时间戳）
//	String timestamp1 = timeStamp;
//	//业务报文  去报文中的msgData（业务数据报文）
//	String msgData1 = msgData;
//	//将业务报文+时间戳+校验码组合成需加密的字符串(注意顺序)
//	String toVerifyText = msgData1+timestamp1+checkWord1;
//	//因业务报文中可能包含加号、空格等特殊字符，需要urlEnCode处理
//	String msgDigest1=null;
//	try {
//		toVerifyText = URLEncoder.encode(toVerifyText,"UTF-8");
//		//进行Md5加密        
//		MessageDigest  md5 = MessageDigest.getInstance("MD5");
//		md5.update(toVerifyText.getBytes("UTF-8"));
//		byte[] md = md5.digest();
//		//通过BASE64生成数字签名
//		msgDigest1 = new String(new BASE64Encoder().encode(md));
////		return msgDigest1;
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}    
//	//
//	System.out.println("msgDigest1-----"+msgDigest1);
//	return msgDigest1;
//}
}
