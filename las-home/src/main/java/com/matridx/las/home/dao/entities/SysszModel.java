package com.matridx.las.home.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SysszModel")
public class SysszModel extends BaseModel{
	//实验室ID
	private String sysid;
	//检测单位
	private String jcdw;
	//负责人
	private String fzr;
	//实验室ID
	public String getSysid() {
		return sysid;
	}
	public void setSysid(String sysid){
		this.sysid = sysid;
	}
	//检测单位
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw){
		this.jcdw = jcdw;
	}
	//负责人
	public String getFzr() {
		return fzr;
	}
	public void setFzr(String fzr){
		this.fzr = fzr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
