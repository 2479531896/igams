package com.matridx.igams.common.enums;
/**
 * 调用方式枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum TransferTypeEnum {
	WEBSERVICE_AXIS2("webservice(Axis2)"),
	HTTP_CLIENT("HttpClient"),
	WEBSERVICE_RPC("webservice(RPC)"),
	HTTP_URLCONNECTION("HttpURLConnection"),
	;

	private final String code;

	TransferTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
