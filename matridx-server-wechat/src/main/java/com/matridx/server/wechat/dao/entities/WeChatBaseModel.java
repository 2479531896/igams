package com.matridx.server.wechat.dao.entities;

public class WeChatBaseModel {
	//目标用户
	private String toUserName;
	//源用户
	private String fromUserName;
	//创建时间
	private String createTime;
	//消息类型
	private String msgType;
	//事件类型
	private String event;
	//事件KEY值，第一次关注qrscene_为前缀，后面为二维码的参数值，第二次扫描二维码的参数值
	private String eventKey;
	//二维码的ticket，可用来换取二维码图片
	private String ticket;
	//关注平台(发消息用)
	private String gzpt;
	
	public String getGzpt() {
		return gzpt;
	}
	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
	}
	public String getToUserName() {
		return this.toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
