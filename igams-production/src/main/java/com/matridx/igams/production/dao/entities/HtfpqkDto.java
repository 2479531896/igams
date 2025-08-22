package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="HtfpqkDto")
public class HtfpqkDto extends HtfpqkModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//发票种类名称
	private String fpzlmc;
	//操作人
	private String czr;
	//发票总金额
	private String fpzje;
	//附件IDS
	private List<String> fjids;
	//业务类型
	private String ywlx;

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getFpzje() {
		return fpzje;
	}

	public void setFpzje(String fpzje) {
		this.fpzje = fpzje;
	}

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getFpzlmc() {
		return fpzlmc;
	}

	public void setFpzlmc(String fpzlmc) {
		this.fpzlmc = fpzlmc;
	}
	
}
