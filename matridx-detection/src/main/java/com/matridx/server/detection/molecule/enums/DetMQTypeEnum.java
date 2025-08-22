package com.matridx.server.detection.molecule.enums;


public enum DetMQTypeEnum {
	APPOINTMENT_DETECTION("matridx.molecule.detection","新冠预约检测同步"), //85发送，86接收
	APPOINTMENT_DETECT("matridx.molecule.detect","新冠预约检测同步"),	//86发送，85接收

	NORMAL_INSPECT_TOWECHAT("matridx.normal.inspect.towechat","普检同步至WECHAT"),
	NORMAL_INSPECT_TOOA("matridx.normal.inspect.tooa","普检同步至OA"),
	;
	private String code;
	private String value;
	
	DetMQTypeEnum(String code, String value) {
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
		for (DetMQTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
