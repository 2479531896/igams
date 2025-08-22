package com.matridx.igams.detection.molecule.enums;


public enum NUONUOEnum {
	//沙箱环境
	SX_APPKEY("SD87707046","sx_key"),
	SX_APPSECRET("SD80F4618F574051","sx_secret"),
	SX_TAXNUM("339901999999142","sx_taxnum"),
	SX_URL("https://sandbox.nuonuocs.cn/open/v1/services","sx_pay_url"),
	//正式环境
	APPKEY("87707046","key"),
	APPSECRET("FA80F4618F574051","secret"),
	TAXNUM("91330110MA2GYD7T83","taxnum"),
	URL("https://sdk.nuonuo.com/open/v1/services","pay_url"),
	//公用
	PAY_METHOD("nuonuo.polymerization.paymentToOrders","pay_method"),
	ORDER_METHOD("nuonuo.polymerization.queryPaymentOrder","order_method"),
	REFUND_INQUIRY_QUERY("nuonuo.AggregatePay.Refundinquiryquery","refund_inquiry_query"),

	RETURN_URL("/detection/detection/pagedataNuonuoPay","return_url"),
	CALLBACK_URL("/detection/detection/payCallbackUrl","callback_url"),
//	ACCESS_TOKEN("fe6a2cfc1fd5c45cee16240zkwip0cbs","access_token"),
	;
	private final String code;
	private final String value;

	NUONUOEnum(String code, String value) {
		this.value = value;
		this.code = code;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCode(){
		return code;
	}
	
	public static String getValueByCode(String code){
		for (NUONUOEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
