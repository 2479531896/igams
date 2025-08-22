package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZytgfModel")
public class ZytgfModel extends BaseModel{
	//提供方ID
	private String tgfid;
	//提供方名称
	private String tgfmc;
	//提供方代码
	private String tgfdm;
	//提供方ID
	public String getTgfid() {
		return tgfid;
	}
	public void setTgfid(String tgfid){
		this.tgfid = tgfid;
	}
	//提供方名称
	public String getTgfmc() {
		return tgfmc;
	}
	public void setTgfmc(String tgfmc){
		this.tgfmc = tgfmc;
	}
	//提供方代码
	public String getTgfdm() {
		return tgfdm;
	}
	public void setTgfdm(String tgfdm){
		this.tgfdm = tgfdm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
