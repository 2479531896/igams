package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YbsqssdwModel")
public class YbsqssdwModel extends BaseModel{
	//申请id
	private String sqid;
	//序号
	private String xh;
	//机构id
	private String jgid;
	//申请id
	public String getSqid() {
		return sqid;
	}
	public void setSqid(String sqid){
		this.sqid = sqid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//机构id
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid){
		this.jgid = jgid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
