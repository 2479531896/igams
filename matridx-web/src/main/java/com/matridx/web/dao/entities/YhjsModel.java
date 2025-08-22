package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhjsModel")
public class YhjsModel extends BaseModel{
	//用户id
	private String yhid;
	//角色id
	protected String jsid;
	//用户id
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//角色id
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
