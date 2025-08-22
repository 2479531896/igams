package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LshkModel")
public class LshkModel extends BaseModel{
	//流水号ID
	private String lshid;
	//流水号类型
	private String lshlx;
	//流水号前缀
	private String lshqz;
	//最大流水号
	private String zdlsh;
	//流水号ID
	public String getLshid() {
		return lshid;
	}
	public void setLshid(String lshid){
		this.lshid = lshid;
	}
	//流水号类型
	public String getLshlx() {
		return lshlx;
	}
	public void setLshlx(String lshlx){
		this.lshlx = lshlx;
	}
	//流水号前缀
	public String getLshqz() {
		return lshqz;
	}
	public void setLshqz(String lshqz){
		this.lshqz = lshqz;
	}
	//最大流水号
	public String getZdlsh() {
		return zdlsh;
	}
	public void setZdlsh(String zdlsh){
		this.zdlsh = zdlsh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
