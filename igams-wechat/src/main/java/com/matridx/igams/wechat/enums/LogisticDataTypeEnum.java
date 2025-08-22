package com.matridx.igams.wechat.enums;

/**
 * 基础数据类别枚举类
 * 
 * @author fuwb
 * @time 20150401
 */
public enum LogisticDataTypeEnum {
	NOTIFY_DISTRIBUTE("NOTIFY_DISTRIBUTE"),//派单通知类别
	NOTIFY_ARRIVE("NOTIFY_ARRIVE"),//送达通知类别
	NOTIFY_OVERTIME("NOTIFY_OVERTIME"),//超时提醒通知类别
	;
	//TRANSVERSE_PROJECT_TYPE("TRANSVERSE_PROJECT_TYPE"); // 横向项目类型
	private String code;

	LogisticDataTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}