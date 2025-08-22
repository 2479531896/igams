package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="BfdxlxrglDto")
public class BfdxlxrglDto extends BfdxlxrglModel{
	//客户名称
	private String khmc;
	//全部(查询条件)
	private String entire;
	//单位名称
	private String dwmc;
	//单位简称
	private String dwjc;
	//对象编码
	private String dxbm;
	//来源名称
	private String lymc;
	//单位分类名称
	private String dwflmc;
	//省份名称
	private String sfmc;
	//单位分类[多]
	private String[] dwfls;
	//省份[多]
	private String[] sfs;

	private String hbz;

	public String[] getDwfls() {
		return dwfls;
	}

	public void setDwfls(String[] dwfls) {
		this.dwfls = dwfls;
	}

	public String[] getSfs() {
		return sfs;
	}

	public void setSfs(String[] sfs) {
		this.sfs = sfs;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwjc() {
		return dwjc;
	}

	public void setDwjc(String dwjc) {
		this.dwjc = dwjc;
	}

	public String getDxbm() {
		return dxbm;
	}

	public void setDxbm(String dxbm) {
		this.dxbm = dxbm;
	}

	public String getLymc() {
		return lymc;
	}

	public void setLymc(String lymc) {
		this.lymc = lymc;
	}

	public String getDwflmc() {
		return dwflmc;
	}

	public void setDwflmc(String dwflmc) {
		this.dwflmc = dwflmc;
	}

	public String getSfmc() {
		return sfmc;
	}

	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getHbz() {
		return hbz;
	}

	public void setHbz(String hbz) {
		this.hbz = hbz;
	}
}
