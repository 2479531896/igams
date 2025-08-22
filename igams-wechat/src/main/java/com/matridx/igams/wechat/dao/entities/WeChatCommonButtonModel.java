package com.matridx.igams.wechat.dao.entities;

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
