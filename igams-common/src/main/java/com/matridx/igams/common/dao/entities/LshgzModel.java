package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LshgzModel")
public class LshgzModel extends BaseModel{
	//流水号规则ID
	private String lshgzid;
	//流水号类型
	private String lshlx;
	//流水号规则
	private String lshgz;
	//流水号规则ID
	public String getLshgzid() {
		return lshgzid;
	}
	public void setLshgzid(String lshgzid){
		this.lshgzid = lshgzid;
	}
	//流水号类型
	public String getLshlx() {
		return lshlx;
	}
	public void setLshlx(String lshlx){
		this.lshlx = lshlx;
	}
	//流水号规则
	public String getLshgz() {
		return lshgz;
	}
	public void setLshgz(String lshgz){
		this.lshgz = lshgz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
