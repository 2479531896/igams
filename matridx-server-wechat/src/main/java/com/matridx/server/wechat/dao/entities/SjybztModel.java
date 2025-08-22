package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;
@Alias(value="SjybztModel")
public class SjybztModel extends BaseModel{
	private String sjid;
	private String xh;
	private String zt;
	
	public String getSjid()
	{
		return sjid;
	}

	public void setSjid(String sjid)
	{
		this.sjid = sjid;
	}

	public String getXh()
	{
		return xh;
	}

	public void setXh(String xh)
	{
		this.xh = xh;
	}

	public String getZt()
	{
		return zt;
	}

	public void setZt(String zt)
	{
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
