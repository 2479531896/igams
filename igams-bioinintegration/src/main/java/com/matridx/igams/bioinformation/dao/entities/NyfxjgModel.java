package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NyfxjgModel")
public class NyfxjgModel extends BaseModel{

	private static final long serialVersionUID = 1L;

	private String nyjgid;//耐药结果id
	private String wkcxid;//文库测序id
	private String nyjy;//耐药基因
	private String bbh;//版本号
	private String nr;//内容json
	private String sfbg;//是否报告

	public String getNyjgid() {
		return nyjgid;
	}

	public void setNyjgid(String nyjgid) {
		this.nyjgid = nyjgid;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getNyjy() {
		return nyjy;
	}

	public void setNyjy(String nyjy) {
		this.nyjy = nyjy;
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
