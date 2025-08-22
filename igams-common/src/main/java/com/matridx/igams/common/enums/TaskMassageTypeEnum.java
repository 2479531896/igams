package com.matridx.igams.common.enums;

public enum TaskMassageTypeEnum{
	DEFAULT_MASSAGE("DEFAULT_MASSAGE"),//默认留言类型
	PERSON_MASSAGE("PERSON_MASSAGE"),//个人留言类型
	;
	private final String code;
	TaskMassageTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
