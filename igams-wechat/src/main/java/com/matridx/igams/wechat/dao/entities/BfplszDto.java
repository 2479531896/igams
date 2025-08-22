package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="BfplszDto")
public class BfplszDto extends BfplszModel{
	//单位名称
	private String dwmc;
	//联系人名称
	private String lxrxm;
	//沟通内容
	private String gtnr;
	//客户分类名称
	private String dwflmc;
	//是否认可
	private String sfrk;
	//全部(查询条件)
	private String entire;
	//频次类型代码
	private String pclxdm;

	public String getPclxdm() {
		return pclxdm;
	}

	public void setPclxdm(String pclxdm) {
		this.pclxdm = pclxdm;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getSfrk() {
		return sfrk;
	}

	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}

	public String getDwflmc() {
		return dwflmc;
	}

	public void setDwflmc(String dwflmc) {
		this.dwflmc = dwflmc;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getLxrxm() {
		return lxrxm;
	}

	public void setLxrxm(String lxrxm) {
		this.lxrxm = lxrxm;
	}

	public String getGtnr() {
		return gtnr;
	}

	public void setGtnr(String gtnr) {
		this.gtnr = gtnr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
