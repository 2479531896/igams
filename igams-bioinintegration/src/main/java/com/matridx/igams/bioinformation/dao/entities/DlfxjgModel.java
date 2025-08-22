package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DlfxjgModel")
public class DlfxjgModel extends BaseModel{

	private static final long serialVersionUID = 1L;

	private String dlfxid;
	private String wkbh;//文库编号
	private String wkcxid;//文科测序ID
	private String bbh;//版本号
	private String nr;//内容
	private String sfbg;//是否报告

	public String getDlfxid() {
		return dlfxid;
	}

	public void setDlfxid(String dlfxid) {
		this.dlfxid = dlfxid;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getSfbg() {
		return sfbg;
	}

	public void setSfbg(String sfbg) {
		this.sfbg = sfbg;
	}
}
