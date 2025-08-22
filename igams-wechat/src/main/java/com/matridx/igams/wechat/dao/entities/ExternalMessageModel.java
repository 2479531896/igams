package com.matridx.igams.wechat.dao.entities;

import java.util.List;

/**
 * 外部发送消息
 * @author linwu
 *
 */
public class ExternalMessageModel {
	
	//员工号
	private List<String> ids;
	//标题
	private String title;
	//内容
	private String message;
	//消息类型
	private String type;
	//内网地址
	private String inaddress;
	//外网地址
	private String exaddress;
	
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInaddress() {
		return inaddress;
	}
	public void setInaddress(String inaddress) {
		this.inaddress = inaddress;
	}
	public String getExaddress() {
		return exaddress;
	}
	public void setExaddress(String exaddress) {
		this.exaddress = exaddress;
	}
}
