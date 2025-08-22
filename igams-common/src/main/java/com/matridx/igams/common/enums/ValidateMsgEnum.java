package com.matridx.igams.common.enums;

/**
 * 校验信息枚举类
 * @author fuwb
 * @time 20150407
 */
public enum ValidateMsgEnum {
	REQUIRED("是必填项!"),
	MINLENGTH("最小长度为!"),
	MAXLENGTH("最大长度为!"),
	FIXEDLENGTH("规定长度为!"),
	ZIPCODE("邮编错误!"),
	DECIMAL("数字格式错误!"),
	MOBILEPHONE("手机号码错误!"),
	TELEPHONE("固定电话号码错误!"),
	ORGCODE("组织机构代码错误!"),
	EMAIL("邮箱格式错误!"),
	DATEISO("日期格式错误(yyyy-MM-dd)!"),
	FAXNUMBER("传真号码错误!"),
	WEBSITE("网址格式错误!"),
	ENGSPACE("只允许输入英文或空格!"),
	LETTER("只允许输入英文字母!"),
	NUMLETTER("只允许输入数字和英文!"),
	NOT_CHINESE("只允许输入非中文字符!"),
	NONNEGATIVE_INT("只允许输入非负整数!"),
	POSITIVE_INT("只允许输入正整数!");
	
	private final String message;
	
	ValidateMsgEnum(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
