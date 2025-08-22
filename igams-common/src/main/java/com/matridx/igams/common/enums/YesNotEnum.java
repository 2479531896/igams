package com.matridx.igams.common.enums;
/**
 * 是否枚举类
 * @author fuwb
 * @time 20150401
 */
public enum YesNotEnum {
	NOT("0","否"),//否
	YES("1","是");//是
	
	private final String code;
	private final String value;
	
	YesNotEnum(String code, String value) {
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
		for (YesNotEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
	public static String getCodeByValue(String value){
		for (YesNotEnum enumi : values()) {
			if(enumi.getValue().equals(value)){
				return enumi.getCode();
			}
		}
		return null;
	}
}
