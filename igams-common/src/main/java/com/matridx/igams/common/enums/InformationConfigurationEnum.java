package com.matridx.igams.common.enums;

/**
 * 消息配置枚举类
 * @author yao
 * @date 2019-11-19
 */
public enum InformationConfigurationEnum {
	
	REGISTER_TYPE("REGISTER_TYPE","注册类型");//注册类型
	
	private final String code;
	private final String value;
	
	InformationConfigurationEnum(String code, String value) {
		this.value = value;
		this.code = code;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCode(){
		return code;
	}
}
