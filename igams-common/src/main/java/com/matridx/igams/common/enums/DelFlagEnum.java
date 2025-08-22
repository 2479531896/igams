package com.matridx.igams.common.enums;
/**
 * 删除标记枚举类
 * @author fuwb
 * @time 20150401
 */
public enum DelFlagEnum {
	ABLE("0","正常"),//正常
	DELETE("1","删除"),//删除
	DISABLE("2","停用"),//停用
	MERGE("3","合并"),//合并
	SPLIT("4","拆分");//拆分
	
	private final String code;
	private final String value;
	
	DelFlagEnum(String code, String value) {
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
		for (DelFlagEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
