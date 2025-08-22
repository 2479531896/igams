package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JszyczbModel")
public class JszyczbModel extends BaseModel{
	//角色id
	private String jsid;
	//资源ID
	private String zyid;
	//操作代码
	private String czdm;
	//角色id
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
