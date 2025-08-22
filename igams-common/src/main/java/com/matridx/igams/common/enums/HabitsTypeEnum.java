package com.matridx.igams.common.enums;
/**
 * 人员操作习惯类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum HabitsTypeEnum {
	USER_HABITS("USER_HABITS","人员选择习惯"),//人员选择习惯
	ROLE_HABITS("ROLE_HABITS","角色选择习惯"),//角色选择习惯
	;

	private final String code;
	private final String value;
	
	HabitsTypeEnum(String code, String value) {
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
		for (HabitsTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
