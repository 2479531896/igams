package com.matridx.server.wechat.dao.entities;

public class WeChatSendDetailModel {
	//头部
	//private String first;
	//关键词1：检测码
	//private String keyword1;
	//关键词2：报告生成时间
	//private String keyword2;
	//备注
	//private String remark;
	//内容
	private String value;
	//颜色
	private String color;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
