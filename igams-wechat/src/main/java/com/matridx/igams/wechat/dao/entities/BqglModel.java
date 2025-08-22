package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="BqglModel")
public class BqglModel extends BaseModel{
	//标签ID
	private String bqid;
	//标签名称
	private String bqm;
	//标签ID
	public String getBqid() {
		return bqid;
	}
	public void setBqid(String bqid){
		this.bqid = bqid;
	}
	//标签名称
	public String getBqm() {
		return bqm;
	}
	public void setBqm(String bqm){
		this.bqm = bqm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
