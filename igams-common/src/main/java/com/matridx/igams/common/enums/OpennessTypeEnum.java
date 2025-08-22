package com.matridx.igams.common.enums;
/**
 * 项目公开类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum OpennessTypeEnum {
	PUBLIC_PROJECT("PUBLIC_PROJECT","公开项目(所有人员可见,只有设置人员可操作)"),//公开的项目
	PRIVATE_PROJECT("PRIVATE_PROJECT","私有项目(仅设置人员可见)"),//私有的项目
	SHARE_PROJECT("SHARE_PROJECT","公有项目(所有人员可见,所有人员可操作)"),//公有的项目
	;

	private final String code;
	private final String value;
	
	OpennessTypeEnum(String code, String value) {
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
		for (OpennessTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
