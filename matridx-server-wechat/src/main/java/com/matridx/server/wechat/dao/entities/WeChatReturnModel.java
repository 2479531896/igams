package com.matridx.server.wechat.dao.entities;

public class WeChatReturnModel {
	//错误代码
	private String errcode;
	//错误信息
	private String errmsg;
	
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
