package com.matridx.igams.common.enums;
/**
 * 流水号类别枚举类
 * 
 * @author fuwb
 * @time 20150612
 */
public enum SerialNumTypeEnum {
	SAMPSRC_CODE("SAMPSRC_CODE"),//标本来源编号
	SAMP_CODE("SAMP_CODE"),//标本编号
	MATER_CODE("MATER_CODE"),//物料编码
	;
	
	private final String code;
	
	SerialNumTypeEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
