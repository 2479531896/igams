package com.matridx.igams.wechat.dao.entities;

import java.util.Map;

public class 	WeChatDetailedReportModel {

	//标本编号
	private String ybbh;
	//员工号
	private String userid;
	//检测类型
	private String detection_type;
	//标本ID
	private String sample_id;
	//结果
	private Map<String, Map<String, WeChatDetailedResultModel>> result;
	//返回状态
	private String status;
	private String xmdm;

	private String chip_uid;


	public String getXmdm() {
		return xmdm;
	}

	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
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
	public Map<String, Map<String, WeChatDetailedResultModel>> getResult() {
		return result;
	}
	public void setResult(Map<String, Map<String, WeChatDetailedResultModel>> result) {
		this.result = result;
	}


	public String getChip_uid() {
		return chip_uid;
	}

	public void setChip_uid(String chip_uid) {
		this.chip_uid = chip_uid;
	}
}
