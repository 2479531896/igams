package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="XtszDto")
public class XtszDto extends XtszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//附件ID复数
	private List<String> fjids;
	//临时保存地址
	private String lsbcdz;
	//标题
	private String bt;
	//描述
	private String ms;
	//通知结果
	private String message;
	//设置类别旧值
	private String oldszlb;
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getLsbcdz() {
		return lsbcdz;
	}
	public void setLsbcdz(String lsbcdz) {
		this.lsbcdz = lsbcdz;
	}
	public String getBt() {
		return bt;
	}
	public void setBt(String bt) {
		this.bt = bt;
	}
	public String getMs() {
		return ms;
	}
	public void setMs(String ms) {
		this.ms = ms;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOldszlb() {
		return oldszlb;
	}
	public void setOldszlb(String oldszlb) {
		this.oldszlb = oldszlb;
	}
	
}
