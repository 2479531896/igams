package com.matridx.igams.common.enums;
/**
 * 通知类型枚举类
 * @author xinyf
 * @date 2015-10-23
 */
public enum NoticeTypeEnum {
	WECAHT("WECAHT","微信"),
	MAIL("MAIL","邮箱");
	
	private final String code;
	private final String value;
	
	NoticeTypeEnum(String code, String value) {
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
		for (NoticeTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
