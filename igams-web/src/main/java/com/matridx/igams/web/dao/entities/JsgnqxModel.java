package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JsgnqxModel")
public class JsgnqxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//角色id
	private String jsid;
	//业务id	
	private String ywid;
	//显示字段
	private String xszd;
	//显示顺序
	private String xssx;
	
	public String getJsid()
	{
		return jsid;
	}
	public void setJsid(String jsid)
	{
		this.jsid = jsid;
	}
	public String getYwid()
	{
		return ywid;
	}
	public void setYwid(String ywid)
	{
		this.ywid = ywid;
	}
	public String getXszd()
	{
		return xszd;
	}
	public void setXszd(String xszd)
	{
		this.xszd = xszd;
	}
	public String getXssx()
	{
		return xssx;
	}
	public void setXssx(String xssx)
	{
		this.xssx = xssx;
	}
	
	
}
