package com.matridx.igams.common.enums;
/**
 * 消息类型枚举类
 * @author xinyf
 * @date 2015-10-23
 */
public enum MessageTypeEnum {
	Error("1","错误"),
	WARN("2","警告"),
	CONFIRM("3","确认"),
	INFO("4","提示");
	
	private final String code;
	private final String value;
	
	MessageTypeEnum(String code, String value) {
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
		for (MessageTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
