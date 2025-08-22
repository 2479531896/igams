package com.matridx.igams.wechat.dao.entities;

import java.util.List;

public class WeChatInspectionReportsModel {

	//标本编号
	private String ybbh;
	//检测类型
	private String detection_type;
	//标本ID
	private String sample_id;
	//返回状态
	private String status;
	//标签列表
	private List<String> pathogen;
	//标签列表
	private List<String> possible;
	//耐药性
	private List<String> drug_resistance;
	//高度关注说明
	private String pathogen_comment;
	//疑似说明
	private String possible_comment;
	//高度关注阳性指标
	private String pathogen_names;
	//疑似指标
	private String possible_names;
	//参考文献
	private String refs;
	

	public String getDetection_type() {
		return detection_type;
	}
	public void setDetection_type(String detection_type) {
		this.detection_type = detection_type;
	}
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
	public String getPathogen_comment() {
		return pathogen_comment;
	}
	public void setPathogen_comment(String pathogen_comment) {
		this.pathogen_comment = pathogen_comment;
	}
	public String getPossible_comment() {
		return possible_comment;
	}
	public void setPossible_comment(String possible_comment) {
		this.possible_comment = possible_comment;
	}
	public String getPathogen_names() {
		return pathogen_names;
	}
	public void setPathogen_names(String pathogen_names) {
		this.pathogen_names = pathogen_names;
	}
	public String getPossible_names() {
		return possible_names;
	}
	public void setPossible_names(String possible_names) {
		this.possible_names = possible_names;
	}
	public String getRefs() {
		return refs;
	}
	public void setRefs(String refs) {
		this.refs = refs;
	}
	public List<String> getDrug_resistance() {
		return drug_resistance;
	}
	public void setDrug_resistance(List<String> drug_resistance) {
		this.drug_resistance = drug_resistance;
	}
	
}
