package com.matridx.igams.wechat.dao.entities;

public class WechatInspectionResistModel {

	//耐药基因注释id
	private String id;
	//基因家族名称
	private String name;
	//注释内容
	private String comment;
	//更新日期
	private String last_update;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	
}
