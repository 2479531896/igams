package com.matridx.server.wechat.dao.entities;

public class JSTicketReturnModel extends WeChatReturnModel{
	//临时票据
	private String ticket;
	//过期时间
	private String expires_in;
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
}
