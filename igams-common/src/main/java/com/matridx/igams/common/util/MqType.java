package com.matridx.igams.common.util;

public class MqType {
	//更新微信用户信息
	public static final String WECHAR_USER_MOD = "wechar.user.mod";
	//订阅
	public static final String WECHAR_SUBSCIBE = "wechar.subscibe";
	//取消订阅
	public static final String WECHAR_UNSUBSCIBE = "wechar.unsubscibe";
	//微信授权
	public static final String WECHAT_AUTHORIZE = "wechat.authorize";
	//文本消息
	public static final String WECHAT_TEXT = "wechar.text";
	//菜单点击
	public static final String MENU_CLICK = "wechar.menu.click";
	//word转换pdf
	public static final String DOC_CHANGE_PDF="mq.tran.basic.ok";
	//注册信息同步
	public static final String REGISTER="matridx.register.result";
	//新增调用接口信息
	public static final String ADD_TRANSFER = "wechat.transfer.add";
	
	// 送检信息同步(微信发送，公司接收)
	public static final String OPERATE_INSPECTION = "matridx.inspection.operate";
	// 微信信息同步(微信发送，公司接收)
	// public static final String OPERATE_WECHAT = "matridx.wechat.operate";
	//物流派单超时提醒
	public static final String OVERTIME_WLPD = "wechat.wlwjd.send.queue";
	//sse发送消息
	public static final String SSE_SENDMSG = "matridx.sse.sendmsg";
	//对接接口发送报告
	public static final String MATCHING_SEND_REPORT = "matridx.matching.sendreport";
}
