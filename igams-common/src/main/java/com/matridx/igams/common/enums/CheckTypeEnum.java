package com.matridx.igams.common.enums;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验类型枚举
 * @author goofus
 *
 */
public enum CheckTypeEnum {
	
	/** 是否可以为空 */
	NULLABLE("NULLABLE", "是否可以为空"),
	/** 最大长度 */
	MAXLENGTH("MAXLENGTH", "最大长度"),
	/** 最小长度 */
	MINLENGTH("MINLENGTH", "最小长度"),
	/** 日期格式 */
	DATEFORMAT("DATEFORMAT", "日期格式"),
	/** 数据类型 */
	DATATYPE("DATATYPE", "数据类型"),
	/** 数据转换标识 */
	TRANSFORM("TRANSFORM", "数据转换标识"),
	/** 有效选项 */
	OPTIONS("OPTIONS", "有效选项"),
	/** 是否可重复 */
	EXISTS("EXISTS", "是否可重复"),
	/** 是否可重复(多字段) */
	MULTIEXISTS("MULTIEXISTS", "是否可重复(多字段)"),
	/** DTO起始标识*/
	DTOHEAD("DTOHEAD","DTO起始标识"),
	/** DTO结束标识*/
	DTOFOOT("DTOFOOT","DTO结束标识");
	
	private final String code;
	private final String value;
	
	CheckTypeEnum(String code, String value) {
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
		if(StringUtils.isBlank(code)) return null;
		code = code.toUpperCase();
		for (CheckTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
	public static CheckTypeEnum getThisByCode(String code){
		if(StringUtils.isBlank(code)) return null;
		code = code.toUpperCase();
		for (CheckTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi;
			}
		}
		return null;
	}
	
	public static ArrayList<CheckTypeEnum> list(){
		ArrayList<CheckTypeEnum> list = new ArrayList<>();
		list.add(CheckTypeEnum.NULLABLE);
		list.add(CheckTypeEnum.MAXLENGTH);
		list.add(CheckTypeEnum.MINLENGTH);
		list.add(CheckTypeEnum.DATATYPE);
		list.add(CheckTypeEnum.DATEFORMAT);
		return list;
	}

}
