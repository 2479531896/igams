package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JsjcdwModel")
public class JsjcdwModel extends BaseModel{

	//角色id
	private String jsid;
	//检测单位
	private String jcdw;
	
	public String getJsid()
	{
		return jsid;
	}


	public void setJsid(String jsid)
	{
		this.jsid = jsid;
	}


	public String getJcdw()
	{
		return jcdw;
	}


	public void setJcdw(String jcdw)
	{
		this.jcdw = jcdw;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
