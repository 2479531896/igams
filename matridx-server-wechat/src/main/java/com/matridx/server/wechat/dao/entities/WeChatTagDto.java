package com.matridx.server.wechat.dao.entities;

public class WeChatTagDto {
	
	WeChatTagModel tag;
	
	//错误代码
	private String errcode;
	//错误信息
	private String errmsg;

	public WeChatTagModel getTag() {
		return tag;
	}

	public void setTag(WeChatTagModel tag) {
		this.tag = tag;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}
