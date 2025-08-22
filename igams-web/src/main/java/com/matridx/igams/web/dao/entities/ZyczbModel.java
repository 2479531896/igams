package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZyczbModel")
public class ZyczbModel extends BaseModel{
	//资源ID
	private String zyid;
	//操作代码
	private String czdm;
	//对应页面
	private String dyym;
	//资源ID
	public String getZyid() {
		return zyid;
	}
	public void setZyid(String zyid){
		this.zyid = zyid;
	}
	//操作代码
	public String getCzdm() {
		return czdm;
	}
	public void setCzdm(String czdm){
		this.czdm = czdm;
	}
	//对应页面
	public String getDyym() {
		return dyym;
	}
	public void setDyym(String dyym){
		this.dyym = dyym;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
