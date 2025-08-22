package com.matridx.server.wechat.dao.entities;

public class WeChatTextModel extends WeChatBaseModel{
	private String Content;
	private String MsgId;
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
