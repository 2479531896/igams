package com.matridx.igams.common.enums;

public enum WorkTypeEnum{
	SUBTASK_TYPE("SUBTASK_TYPE","子任务"),//工作类型一
	TASK_TYPE("TASK_TYPE","总任务"),//工作类型二
	;
	private final String code;
	private final String value;
	WorkTypeEnum(String code,String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public String getValue() {
		return value;
	}
	public static String getValueByCode(String code){
		for (WorkTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
