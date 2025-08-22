package com.matridx.server.wechat.dao.entities;

import java.util.List;

public class WeChatInspectionReportModel {

	//标本编号
	private String ybbh;
	//标本ID
	private String sample_id;
	//返回状态
	private String status;
	//标签列表
	private List<String> pathogen;
	//标签列表
	private List<String> possible;
	
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}
	public String getSample_id() {
		return sample_id;
	}
	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getPathogen() {
		return pathogen;
	}
	public void setPathogen(List<String> pathogen) {
		this.pathogen = pathogen;
	}
	public List<String> getPossible() {
		return possible;
	}
	public void setPossible(List<String> possible) {
		this.possible = possible;
	}
	
}
