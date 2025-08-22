package com.matridx.igams.common.enums;
/**
 * 数据库类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum DatabaseTypeEnum {
	ORCALE("ORCALE"),//oracle
	MYSQL("MYSQL"),//mysql
	POSTGRESQL("POSTGRESQL"),//postgresql
	DB2("DB2"),//db2
	;

	private final String code;

	DatabaseTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
