package com.matridx.igams.common.enums;

/**
 * 支付业务类型枚举类
 * @author Administrator
 */
public enum BusinessTypeEnum {
	SJ("SJ","送检"), // 送检
	FJ("FJ","复检"), // 复检
	XG("XG","fzjcxxServiceImpl"), // 新冠
	;

	private final String code;
	private final String value;
	
	BusinessTypeEnum(String code, String value) {
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
		for (BusinessTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
