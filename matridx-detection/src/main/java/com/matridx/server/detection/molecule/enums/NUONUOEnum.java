package com.matridx.server.detection.molecule.enums;


public enum NUONUOEnum {
	//沙箱环境
	SX_APPKEY("SD87707046","sx_key"),
	SX_APPSECRET("SD80F4618F574051","sx_secret"),
	SX_TAXNUM("339901999999142","sx_taxnum"),
	SX_INVOICE_TAXNUM("339901999999825","sx_invoice_taxnum"),
	SX_INVOICE_APPSECRET("SDAAD33BF76B400B","sx_invoice_secret"),
	SX_INVOICE_APPKEY("SD25327046","sx_invoice_key"),
	SX_URL("https://sandbox.nuonuocs.cn/open/v1/services","sx_pay_url"),
	//正式环境
	APPKEY("87707046","key"),
	APPSECRET("FA80F4618F574051","secret"),
	TAXNUM("91330110MA2GYD7T83","taxnum"),
	URL("https://sdk.nuonuo.com/open/v1/services","pay_url"),
	INVOICE_TAXNUM("91330110MA2GYD7T83","invoice_taxnum"),
	INVOICE_APPSECRET("0BAAD33BF76B400B","invoice_secret"),
	INVOICE_APPKEY("25327046","invoice_key"),
	//公用
	PAY_METHOD("nuonuo.polymerization.paymentToOrders","pay_method"),
	INVOICE_METHOD("nuonuo.ElectronInvoice.requestBillingNew","invoice_method"),
	INVOICE_INFO_METHOD("nuonuo.ElectronInvoice.queryInvoiceResult","invoice_info_method"),
	ORDER_METHOD("nuonuo.polymerization.queryPaymentOrder","order_method"),
	REFUND_QUERY("nuonuo.AggregatePay.refundquery","refundquery"),
	INVOICE_LINK("nuonuo.polymerization.getInvoiceLinks","invoiceLink"),

	RETURN_URL("/wechat/nuonuoPay","return_url"),
	CALLBACK_URL("/wechat/payCallbackUrl","callback_url"),
//	ACCESS_TOKEN("fe6a2cfc1fd5c45cee16240zkwip0cbs","access_token"),
	;
	private String code;
	private String value;

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
