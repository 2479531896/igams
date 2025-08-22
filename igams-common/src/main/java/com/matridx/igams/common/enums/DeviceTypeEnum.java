package com.matridx.igams.common.enums;

public enum DeviceTypeEnum {
	BX("bx","冰箱"),//冰箱
	CT("ct","抽屉"),//抽屉
	HZ("hz","盒子");//盒子
	
	private final String code;
	private final String value;
	
	DeviceTypeEnum(String code, String value) {
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
		for (DeviceTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
