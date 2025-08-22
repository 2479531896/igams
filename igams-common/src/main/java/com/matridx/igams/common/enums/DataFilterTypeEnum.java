package com.matridx.igams.common.enums;

/**
 * 数据过滤类型枚举
 * @author goofus
 *
 */
public enum DataFilterTypeEnum {
	
	/** 过滤器类型，附加是否已开始审批 */
	FILTER_TYPE_SHCZJS("FILTER_TYPE_SHCZJS","是否已开始审批");
	
	private final String code;
	private final String value;
	
	DataFilterTypeEnum(String code, String value) {
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
		for (DataFilterTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
