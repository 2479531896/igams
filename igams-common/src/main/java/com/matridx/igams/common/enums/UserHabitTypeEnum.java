package com.matridx.igams.common.enums;

/**
 * 用户习惯别枚举类
 * 
 * @author fuwb
 * @time 20150401
 */
public enum UserHabitTypeEnum {
	USER_HABIT_SJZZSQ("USER_HABIT_SJZZSQ"),//送检纸质报告申请
	;
	private final String code;

	UserHabitTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}