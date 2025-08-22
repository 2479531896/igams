package com.matridx.server.wechat.dao.entities;

public class WeChatMaterialModel {
	//素材ID
	private String media_id;
	//素材地址
	private String url;
	//错误代码
	private String errcode;
	//错误信息
	private String errmsg;
	
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
