package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SpgwDto")
public class SpgwDto extends SpgwModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//审核ID
	private String shid;
	//流程序号
	private String lcxh;
	//流程类别list
	private List<String> lclblist;
	//岗位IDlist
	private List<String> gwidlist;
	//流程类别
	private String lclb;
	//流程类别名称
	private String lclbmc;
    private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLclbmc() {
		return lclbmc;
	}
	public void setLclbmc(String lclbmc) {
		this.lclbmc = lclbmc;
	}
	public String getLclb() {
		return lclb;
	}
	public void setLclb(String lclb) {
		this.lclb = lclb;
	}
	public List<String> getLclblist() {
		return lclblist;
	}
	public void setLclblist(List<String> lclblist) {
		this.lclblist = lclblist;
	}
	public List<String> getGwidlist() {
		return gwidlist;
	}
	public void setGwidlist(List<String> gwidlist) {
		this.gwidlist = gwidlist;
	}
	public String getShid() {
		return shid;
	}
	public void setShid(String shid) {
		this.shid = shid;
	}
	public String getLcxh() {
		return lcxh;
	}
	public void setLcxh(String lcxh) {
		this.lcxh = lcxh;
	}

}
