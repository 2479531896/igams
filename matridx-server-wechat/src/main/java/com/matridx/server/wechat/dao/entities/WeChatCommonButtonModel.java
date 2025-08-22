package com.matridx.server.wechat.dao.entities;

/**
 * 微信菜单的普通按钮
 * @author linwu
 *
 */
public class WeChatCommonButtonModel extends WeChatButtonModel{
	//类型
	private String type;
	//用于click点击后判断用的关键key
	private String key;
	//用于view的连接，view不能用key
	private String url;
	//用于miniprogram的连接
	private String appid;
	//用于miniprogram的连接
	private String pagepath;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPagepath() {
		return pagepath;
	}
	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
