package com.matridx.igams.common.enums;

/**
 * 支付类型枚举类
 * @author Administrator
 */
public enum PayTypeEnum {
	PAY("PAY","支付"), // 支付
	REFUND("REFUND","退款"), // 退款
	;

	private final String code;
	private final String value;
	
	PayTypeEnum(String code, String value) {
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
		for (PayTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
