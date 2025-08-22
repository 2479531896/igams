package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="KzbmxModel")
public class KzbmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//扩增板明细ID
	private String kzbmxid;
	//扩增板ID
	private String kzbid;
	//序号
	private String xh;
	//行数
	private String hs;
	//列数
	private String ls;
	//样本编号
	private String ybbh;
	//分子检测ID
	private String fzjcid;
	//实验号
	private String syh;

	public String getSyh() {
		return syh;
	}

	public void setSyh(String syh) {
		this.syh = syh;
	}

	public String getKzbmxid() {
		return kzbmxid;
	}

	public void setKzbmxid(String kzbmxid) {
		this.kzbmxid = kzbmxid;
	}

	public String getKzbid() {
		return kzbid;
	}

	public void setKzbid(String kzbid) {
		this.kzbid = kzbid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getHs() {
		return hs;
	}

	public void setHs(String hs) {
		this.hs = hs;
	}

	public String getLs() {
		return ls;
	}

	public void setLs(String ls) {
		this.ls = ls;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}
}
