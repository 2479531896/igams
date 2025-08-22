package com.matridx.igams.common.enums;
/**
 * 数据库类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum RequestMethodEnum {
	POST("POST"),//post
	GET("GET"),//get
	;

	private final String code;

	RequestMethodEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
