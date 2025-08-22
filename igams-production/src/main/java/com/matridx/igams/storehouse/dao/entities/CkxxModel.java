package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CkxxModel")
public class CkxxModel extends BaseModel{
	//仓库id
	private String ckid;
	//父仓库id
	private String fckid;
	//仓库代码
	private String ckdm;
	//仓库名称
	private String ckmc;
	//仓库类别
	private String cklb;
	//备注
	private String bz;
	//区分
	private String qf;
	//仓库分类
	private String ckqxlx;

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getQf() {
		return qf;
	}


	public void setQf(String qf) {
		this.qf = qf;
	}


	public String getCkid()
	{
		return ckid;
	}


	public void setCkid(String ckid)
	{
		this.ckid = ckid;
	}


	public String getFckid()
	{
		return fckid;
	}


	public void setFckid(String fckid)
	{
		this.fckid = fckid;
	}


	public String getCkdm()
	{
		return ckdm;
	}


	public void setCkdm(String ckdm)
	{
		this.ckdm = ckdm;
	}


	public String getCkmc()
	{
		return ckmc;
	}


	public void setCkmc(String ckmc)
	{
		this.ckmc = ckmc;
	}


	public String getCklb()
	{
		return cklb;
	}


	public void setCklb(String cklb)
	{
		this.cklb = cklb;
	}


	public String getBz()
	{
		return bz;
	}


	public void setBz(String bz)
	{
		this.bz = bz;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
