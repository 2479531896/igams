package com.matridx.igams.common.enums;

/**
 * 外部程序编码枚举类
 * @author fuwb
 * @time 20150424
 */
public enum ProgramCodeEnum {
	MATRIDX("Matridx","杰毅生物"),//杰毅生物公众号
	MDINSPECT("MDInspect","杰毅医检"),//杰毅医检公众号
	MINIINSP("MiniInsp","杰毅医检小程序"),//杰毅医检小程序
	DOCASST("DocAsst","医生助手小程序"),//医生助手小程序
	;
	
	private final String code;
	private final String value;
	
	ProgramCodeEnum(String code, String value) {
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
		for (ProgramCodeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}