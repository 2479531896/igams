package com.matridx.igams.common.enums;
/**
 * 审核结果枚举类
 * @author xinyf
 * @date 2015-5-28
 */
public enum AuditResultEnum {
	BACK("0","退回"),
	PASS("1","通过");
	
	private final String code;
	private final String value;
	
	AuditResultEnum(String code, String value) {
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
		for (AuditResultEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
