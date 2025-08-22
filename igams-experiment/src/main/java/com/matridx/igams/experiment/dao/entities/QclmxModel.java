package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QclmxModel")
public class QclmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//前处理id
	private String qclid;
	//序号
	private String xh;
	//内部编号
	private String nbbh;
	//送检id
	private String sjid;
	//细胞计数
	private String xbjs;
	//细胞指数
	private String xbzs;

	public String getQclid()
	{
		return qclid;
	}
	public void setQclid(String qclid)
	{
		this.qclid = qclid;
	}
	public String getXh()
	{
		return xh;
	}
	public void setXh(String xh)
	{
		this.xh = xh;
	}
	public String getNbbh()
	{
		return nbbh;
	}
	public void setNbbh(String nbbh)
	{
		this.nbbh = nbbh;
	}
	public String getSjid()
	{
		return sjid;
	}
	public void setSjid(String sjid)
	{
		this.sjid = sjid;
	}
	public String getXbjs()
	{
		return xbjs;
	}
	public void setXbjs(String xbjs)
	{
		this.xbjs = xbjs;
	}
	public String getXbzs()
	{
		return xbzs;
	}
	public void setXbzs(String xbzs)
	{
		this.xbzs = xbzs;
	}
	
	
}
