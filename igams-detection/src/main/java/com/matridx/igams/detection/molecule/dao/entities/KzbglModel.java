package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="KzbglModel")
public class KzbglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//扩增板ID
	private String kzbid;
	//扩增板号
	private String kzbh;
	//实验日期
	private String syrq;
	//扩增试剂批号
	private String kzsjph;
	//提取试剂批号
	private String tqsjph;
	//检测单位
	private String jcdw;
	//状态
	private String zt;

	public String getKzsjph() {
		return kzsjph;
	}

	public void setKzsjph(String kzsjph) {
		this.kzsjph = kzsjph;
	}

	public String getTqsjph() {
		return tqsjph;
	}

	public void setTqsjph(String tqsjph) {
		this.tqsjph = tqsjph;
	}

	public String getKzbid() {
		return kzbid;
	}

	public void setKzbid(String kzbid) {
		this.kzbid = kzbid;
	}

	public String getKzbh() {
		return kzbh;
	}

	public void setKzbh(String kzbh) {
		this.kzbh = kzbh;
	}

	public String getSyrq() {
		return syrq;
	}

	public void setSyrq(String syrq) {
		this.syrq = syrq;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
}
