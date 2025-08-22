package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="WkmxPcrModel")
public class WkmxPcrModel{
	//第几次报告
	private String djcsy;
	//文库id
	private String wkid;
	
	private String ispcr;
	private String pzyqid;
	//第几次报告
	private String backdata;
	private String jcdw;
	private String jcdwtype;

	//样本结果集合
	private List<WkmxPcrResultModel> result ;
	public String getDjcsy() {
		return djcsy;
	}
	public void setDjcsy(String djcsy) {
		this.djcsy = djcsy;
	}
	public List<WkmxPcrResultModel> getResult() {
		return result;
	}
	public void setResult(List<WkmxPcrResultModel> result) {
		this.result = result;
	}
	public String getWkid() {
		return wkid;
	}
	public void setWkid(String wkid) {
		this.wkid = wkid;
	}
	public String getIspcr() {
		return ispcr;
	}
	public void setIspcr(String ispcr) {
		this.ispcr = ispcr;
	}
	public String getBackdata() {
		return backdata;
	}
	public void setBackdata(String backdata) {
		this.backdata = backdata;
	}
	public String getPzyqid() {
		return pzyqid;
	}
	public void setPzyqid(String pzyqid) {
		this.pzyqid = pzyqid;
	}
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getJcdwtype() {
		return jcdwtype;
	}

	public void setJcdwtype(String jcdwtype) {
		this.jcdwtype = jcdwtype;
	}
}
