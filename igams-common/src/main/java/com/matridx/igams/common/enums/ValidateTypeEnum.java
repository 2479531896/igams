package com.matridx.igams.common.enums;
/**
 * 校验类型枚举类
 * @author fuwb
 * @time 20150407
 */
public enum ValidateTypeEnum {
	STRING("string"),//字符串类型
	DECIMAL("decimal"),//小数类型
	ENGSPACE("engspace"),//英文字符(可输入空格)
	ZIPCODE("zipcode"),//邮政编码
	ORGCODE("orgcode"),//组织机构代码
	TELEPHONE("telephone"),//固定电话号码
	MOBILEPHONE("mobilephone"),//手机号码
	FAXNUMBER("faxnumber"),//传真号码
	WEBSITE("website"),//网址校验
	EMAIL("email"),//邮箱校验
	DATEISO("dateISO"),//校验YYYY-MM-DD格式ISO日期
	NONNEGATIVE_INT("nonnegativeInt"),//校验非负整数
	POSITIVE_INT("positiveInt"),//校验正整数
	LETTER("letter"),//英文字母
	NUMLETTER("numletter"),//英文和数字
	NOT_CHINESE("notChinese"),//英文、数字或中划线
	REQUIRED("required"),//是否必填:true、false
	SHOW("show"),//显示true  不显示false
	;
	
	private final String type;
	
	ValidateTypeEnum(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
