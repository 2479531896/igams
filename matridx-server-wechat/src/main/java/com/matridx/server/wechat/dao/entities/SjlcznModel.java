package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjlcznModel")
public class SjlcznModel extends BaseModel{
	//送检ID
	private String sjid;
	//指南ID
	private String znid;
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//指南ID
	public String getZnid() {
		return znid;
	}
	public void setZnid(String znid){
		this.znid = znid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
