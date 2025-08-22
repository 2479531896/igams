package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HbqxModel")
public class HbqxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户id
	private String yhid;
	//序号
	private String xh;
	//伙伴id
	private String hbid;
	public String getYhid()
	{
		return yhid;
	}
	public void setYhid(String yhid)
	{
		this.yhid = yhid;
	}
	public String getXh()
	{
		return xh;
	}
	public void setXh(String xh)
	{
		this.xh = xh;
	}
	public String getHbid()
	{
		return hbid;
	}
	public void setHbid(String hbid)
	{
		this.hbid = hbid;
	}
	
	
}
