package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjgzbyModel")
public class SjgzbyModel extends BaseModel{
	//送检ID
	private String sjid;
	//病原
	private String by;
	//病原名称
	private String bymc;
	
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//病原
	public String getBy() {
		return by;
	}
	public void setBy(String by){
		this.by = by;
	}

	public String getBymc() {
		return bymc;
	}
	public void setBymc(String bymc) {
		this.bymc = bymc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
