package com.matridx.server.wechat.enums;
/**
 * 送检身份类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum IdentityTypeEnum {
	DOCTOR("DOCTOR","医师"),//医师
	PATIENT("PATIENT","患者"),//患者
	SALES("SALES","销售"),//销售
	;

	private String code;
	private String value;
	
	private IdentityTypeEnum(String code,String value) {
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
		for (IdentityTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
