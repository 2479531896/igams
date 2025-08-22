package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="LbzdszDto")
public class LbzdszDto extends LbzdszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String yhid;

	private String jsid;

	private String width;
	
	public String getJsid()
	{
		return jsid;
	}

	public void setJsid(String jsid)
	{
		this.jsid = jsid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}
