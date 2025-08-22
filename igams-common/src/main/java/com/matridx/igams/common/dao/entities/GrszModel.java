package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GrszModel")
public class GrszModel extends BaseModel{
	//设置ID
	private String szid;
	//用户ID
	private String yhid;
	//设置类别
	private String szlb;
	//设置值
	private String szz;
	//关联信息
	private String glxx;
	//关联信息
	private String dysl;
	//设置ID

	public String getDysl() {
		return dysl;
	}

	public void setDysl(String dysl) {
		this.dysl = dysl;
	}

	public String getSzid() {
		return szid;
	}
	public void setSzid(String szid){
		this.szid = szid;
	}
	//用户ID
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//设置类别
	public String getSzlb() {
		return szlb;
	}
	public void setSzlb(String szlb){
		this.szlb = szlb;
	}
	//设置值
	public String getSzz() {
		return szz;
	}
	public void setSzz(String szz){
		this.szz = szz;
	}
	//关联信息
	public String getGlxx() {
		return glxx;
	}
	public void setGlxx(String glxx){
		this.glxx = glxx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
