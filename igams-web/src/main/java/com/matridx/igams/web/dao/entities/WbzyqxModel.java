package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WbzyqxModel")
public class WbzyqxModel extends BaseModel{
	//角色id
	private String jsid;
	//资源ID
	private String zyid;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
