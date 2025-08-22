package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LjfxxModel")
public class LjfxxModel extends BaseModel{
	//连接ID
	private String ljid;
	//连接地址
	private String ljdz;
	//调用方式 webservice  json restful
	private String dyfs;
	//请求方式
	private String qqfs;
	//连接名称
	private String ljmc;
	
	//连接ID
	public String getLjid() {
		return ljid;
	}
	public void setLjid(String ljid){
		this.ljid = ljid;
	}
	//连接地址
	public String getLjdz() {
		return ljdz;
	}
	public void setLjdz(String ljdz){
		this.ljdz = ljdz;
	}
	//调用方式 webservice  json restful
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs){
		this.dyfs = dyfs;
	}
	//请求方式
	public String getQqfs() {
		return qqfs;
	}
	public void setQqfs(String qqfs) {
		this.qqfs = qqfs;
	}
	//连接名称
	public String getLjmc() {
		return ljmc;
	}
	public void setLjmc(String ljmc) {
		this.ljmc = ljmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
