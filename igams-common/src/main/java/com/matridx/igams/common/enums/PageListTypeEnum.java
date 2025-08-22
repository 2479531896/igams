package com.matridx.igams.common.enums;
/**
 * 业务类型枚举类
 * 
 * @author Kaijian Lee
 * @time 20180717
 */
public enum PageListTypeEnum {
	
	CREDIT_XYYLB("CREDIT_XYYLB"),//信用一览表	
	
	;

	private final String code;

	PageListTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
