package com.matridx.igams.common.enums;
/**
 * 消息类型枚举类
 * @author xinyf
 * @date 2015-10-23
 */
public enum ProjectTypeEnum {
	PROJECT("PROJECT","项目"),
	FOLDER("FOLDER","文件夹");
	
	private final String code;
	private final String value;
	
	ProjectTypeEnum(String code, String value) {
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
		for (ProjectTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
