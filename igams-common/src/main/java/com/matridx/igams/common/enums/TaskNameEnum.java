package com.matridx.igams.common.enums;
/**
 * 工作类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum TaskNameEnum {
	FILE_STUDY("文件学习"),//文件学习
	PROJECT_TASK("项目任务"),//项目任务
	EXCEPTION_TASK("异常任务"),//异常任务
	;

	private final String code;

	TaskNameEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
