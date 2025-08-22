package com.matridx.igams.wechat.dao.entities;

public class WeChatTagUserModel {
	
	//这次获取的粉丝数量
	private String count;
	//粉丝openid列表
	private WeChatTagFansModel data;
	//拉取列表最后一个用户的openid
	private String next_openid;
	//错误代码
	private String errcode;
	//错误信息
	private String errmsg;
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public WeChatTagFansModel getData() {
		return data;
	}
	public void setData(WeChatTagFansModel data) {
		this.data = data;
	}
	public String getNext_openid() {
		return next_openid;
	}
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
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
