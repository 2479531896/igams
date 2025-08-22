package com.matridx.igams.common.enums;

/**
 * 项目状态枚举类
 * @author fuwb
 * @time 20150424
 */
public enum OperateEnum {
	SUBMIT("SUBMIT","提交"),//提交
	;

	private final String code;
	private final String value;

	OperateEnum(String code, String value) {
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
		for (OperateEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}