package com.matridx.igams.common.enums;
/**
 * 外部程序类型枚举类
 * @author xinyf
 * @date 2015-5-20
 */
public enum CharacterEnum {
	DINGDING("DINGDING"),//钉钉
	WECHAT("WECHAT"), //微信
	;

	private final String code;

	CharacterEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
