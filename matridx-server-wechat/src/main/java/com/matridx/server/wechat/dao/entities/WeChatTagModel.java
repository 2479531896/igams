package com.matridx.server.wechat.dao.entities;

public class WeChatTagModel {
	//标签ID
	private String id;
	//标签名
	private String name;
	//标签下粉丝数量
	private String count;
	//错误代码
	private String errcode;
	//错误信息
	private String errmsg;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
}
