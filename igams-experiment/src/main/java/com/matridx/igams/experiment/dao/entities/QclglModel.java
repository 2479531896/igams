package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QclglModel")
public class QclglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//前处理ID
	private String qclid;
	//名称
	private String mc;
	//状态
	private String zt;
	//录入人员
	private String lrry;
	//录入时间
	private String  lrsj;
	//修改人员
	private String xgry;
	//修改时间
	private String xgsj;
	//删除人员
	private String scry;
	//删除时间
	private String scsj;
	//删除标记
	private String scbj;
	
	public String getQclid()
	{
		return qclid;
	}
	public void setQclid(String qclid)
	{
		this.qclid = qclid;
	}
	public String getMc()
	{
		return mc;
	}
	public void setMc(String mc)
	{
		this.mc = mc;
	}
	public String getZt()
	{
		return zt;
	}
	public void setZt(String zt)
	{
		this.zt = zt;
	}
	public String getLrry()
	{
		return lrry;
	}
	public void setLrry(String lrry)
	{
		this.lrry = lrry;
	}
	public String getLrsj()
	{
		return lrsj;
	}
	public void setLrsj(String lrsj)
	{
		this.lrsj = lrsj;
	}
	public String getXgry()
	{
		return xgry;
	}
	public void setXgry(String xgry)
	{
		this.xgry = xgry;
	}
	public String getXgsj()
	{
		return xgsj;
	}
	public void setXgsj(String xgsj)
	{
		this.xgsj = xgsj;
	}
	public String getScry()
	{
		return scry;
	}
	public void setScry(String scry)
	{
		this.scry = scry;
	}
	public String getScsj()
	{
		return scsj;
	}
	public void setScsj(String scsj)
	{
		this.scsj = scsj;
	}
	public String getScbj()
	{
		return scbj;
	}
	public void setScbj(String scbj)
	{
		this.scbj = scbj;
	}
	
	
}
