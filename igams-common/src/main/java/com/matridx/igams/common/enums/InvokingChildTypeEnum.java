package com.matridx.igams.common.enums;

public enum InvokingChildTypeEnum {

	INVOKING_CHILD_REPORT("INVOKING_CHILD_REPORT","报告","INVOKING_INSPECTION"),
	INVOKING_CHILD_XGYY("INVOKING_CHILD_XGYY","预约新增","INVOKING_XGLR"),
	INVOKING_CHILD_XGQX("INVOKING_CHILD_XGQX","预约取消","INVOKING_XGLR"),
	INVOKING_CHILD_PAY("INVOKING_CHILD_PAY","支付","INVOKING_INSPECTION"),
	INVOKING_CHILD_PCR_RESULT("INVOKING_CHILD_PCR_RESULT","PCR结果","INVOKING_WKMX"),
	INVOKING_CHILD_PCRYZ_RESULT("INVOKING_CHILD_PCRYZ_RESULT","PCR验证结果","INVOKING_PCRYZ"),
	RECEIVE_INSPECTINFO_WS("RECEIVE_INSPECTINFO_WS","样本接收WS","RECEIVE_INSPECTINFO"),
	RECEIVE_INSPECTINFO_WS_PLUS("RECEIVE_INSPECTINFO_WS_PLUS","样本接收WS_PLUS","RECEIVE_INSPECTINFO"),
	RECEIVE_INSPECTINFO_WSAPI("RECEIVE_INSPECTINFO_WSAPI","样本接收WSAPI","RECEIVE_INSPECTINFO"),
	RECEIVE_INSPECTINFO_WSAPI_PLUS("RECEIVE_INSPECTINFO_WSAPI_PLUS","样本接收WSAPI_PLUS","RECEIVE_INSPECTINFO"),

	;
	
	private final String code;
	private final String value;
	private final String parentclass;
	
	InvokingChildTypeEnum(String code, String value, String parentclass) {
		this.code = code;
		this.value = value;
		this.parentclass = parentclass;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getParentclass() {
		return parentclass;
	}
}
