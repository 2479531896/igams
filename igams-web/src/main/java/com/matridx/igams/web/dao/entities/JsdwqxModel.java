package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JsdwqxModel")
public class JsdwqxModel extends BaseModel{
	//角色ID
	private String jsid;
	//机构ID   控制到学院级别
	private String jgid;
	//角色ID
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}
	//机构ID   控制到学院级别
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
