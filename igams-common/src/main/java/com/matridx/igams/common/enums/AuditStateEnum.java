package com.matridx.igams.common.enums;
/**
 * 审核状态枚举类
 * @author xinyf
 * @date 2015-5-20
 */
public enum AuditStateEnum {
	AUDITBACK("AUDITBACK"),//审核退回
	AUDITING("AUDITING"), //审核中
	AUDITED("AUDITED"),	  //审核完成 
	;
	
	private final String code;
	
	AuditStateEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
