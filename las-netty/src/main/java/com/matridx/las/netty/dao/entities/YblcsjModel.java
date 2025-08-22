package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias("YblcsjModel")
public class YblcsjModel extends BaseModel{
	
	//流程事件id
	private String lcsjid;
	//机器人抓托盘时间
	private String jqrztpsj;
	//建库仪开始时间
	private String jkykssj;
	//建库仪结束时间
	private String jkyjssj;
	//机器人拿出样本时间
	private String jqrncybsj;
	//配置仪开始时间
	private String pzykssj;
	//第一次配液结束时间
	private String dycpyjssj;
	//第一次定量开始时间
	private String dycdlykssj;
	//第一次定量结束时间
	private String dycdljssj;
	//第二次配液开始时间
	private String decpykssj;
	//第二次配液结束时间
	private String decpyjssj;
	//第二次定量开始时间
	private String decdlykssj;
	//第二次定量结束时间
	private String decdlyjssj;
	//放入测序仪时间
	private String frcxysj;
	//原始样本id
	private String ysybid;
	
	public String getLcsjid() {
		return lcsjid;
	}


	public void setLcsjid(String lcsjid) {
		this.lcsjid = lcsjid;
	}


	public String getJqrztpsj() {
		return jqrztpsj;
	}


	public void setJqrztpsj(String jqrztpsj) {
		this.jqrztpsj = jqrztpsj;
	}


	public String getJkykssj() {
		return jkykssj;
	}


	public void setJkykssj(String jkykssj) {
		this.jkykssj = jkykssj;
	}


	public String getJkyjssj() {
		return jkyjssj;
	}


	public void setJkyjssj(String jkyjssj) {
		this.jkyjssj = jkyjssj;
	}


	public String getJqrncybsj() {
		return jqrncybsj;
	}


	public void setJqrncybsj(String jqrncybsj) {
		this.jqrncybsj = jqrncybsj;
	}


	public String getPzykssj() {
		return pzykssj;
	}


	public void setPzykssj(String pzykssj) {
		this.pzykssj = pzykssj;
	}


	public String getDycpyjssj() {
		return dycpyjssj;
	}


	public void setDycpyjssj(String dycpyjssj) {
		this.dycpyjssj = dycpyjssj;
	}


	public String getDycdlykssj() {
		return dycdlykssj;
	}


	public void setDycdlykssj(String dycdlykssj) {
		this.dycdlykssj = dycdlykssj;
	}


	public String getDycdljssj() {
		return dycdljssj;
	}


	public void setDycdljssj(String dycdljssj) {
		this.dycdljssj = dycdljssj;
	}


	public String getDecpykssj() {
		return decpykssj;
	}


	public void setDecpykssj(String decpykssj) {
		this.decpykssj = decpykssj;
	}


	public String getDecpyjssj() {
		return decpyjssj;
	}


	public void setDecpyjssj(String decpyjssj) {
		this.decpyjssj = decpyjssj;
	}


	public String getDecdlykssj() {
		return decdlykssj;
	}


	public void setDecdlykssj(String decdlykssj) {
		this.decdlykssj = decdlykssj;
	}


	public String getDecdlyjssj() {
		return decdlyjssj;
	}


	public void setDecdlyjssj(String decdlyjssj) {
		this.decdlyjssj = decdlyjssj;
	}


	public String getFrcxysj() {
		return frcxysj;
	}


	public void setFrcxysj(String frcxysj) {
		this.frcxysj = frcxysj;
	}
	
	
	public String getYsybid() {
		return ysybid;
	}


	public void setYsybid(String ysybid) {
		this.ysybid = ysybid;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
