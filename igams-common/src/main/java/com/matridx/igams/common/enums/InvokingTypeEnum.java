package com.matridx.igams.common.enums;

/**
 * 接口调用类别枚举类
 * @author yao
 *
 */
public enum InvokingTypeEnum {
	INVOKING_INSPECTION("INVOKING_INSPECTION","特检"),
	INVOKING_XGLR("INVOKING_XGLR","新冠录入"),
	INVOKING_WKMX("INVOKING_WKMX","文库明细"),
	INVOKING_PCRYZ("INVOKING_PCRYZ","PCR验证"),
	RECEIVE_INSPECTINFO("RECEIVE_INSPECTINFO","样本接收");

	private final String code;
	private final String value;
	
	
	InvokingTypeEnum(String code, String value) {
		this.code = code;
		this.value = value;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getValue() {
		return value;
	}
}
