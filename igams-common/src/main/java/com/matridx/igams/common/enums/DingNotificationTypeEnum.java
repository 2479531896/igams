package com.matridx.igams.common.enums;

/**
 * 钉钉通知类型枚举类
 * @author pc
 *
 */
public enum DingNotificationTypeEnum{
	USER_TYPE("USER_TYPE","用户"),//用户类型
	ROLE_TYPE("ROLE_TYPE","角色"),//角色类型
	;
	private final String code;
	private final String value;
	DingNotificationTypeEnum(String code, String value){
		this.code = code;
		this.value = value;
	}
	public String getCode(){
		return code;
	}

	public String getValue(){
		return value;
	}
	public static String getValueByCode(String code){
		for (DingNotificationTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}
