package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "BioWzglDto")
public class BioWzglDto extends BioWzglModel {

	private static final long serialVersionUID = 1L;
	private String wzmc;//物种名称，可能是wzid ，也可能是物种的中文或英文
	private String wkbh;
	private String bbh;
	private String gzd;
	private String flmc;
	private String wkcxid;
	private String rnaflag;
	private String dnaflag;

	public String getRnaflag() {
		return rnaflag;
	}

	public void setRnaflag(String rnaflag) {
		this.rnaflag = rnaflag;
	}

	public String getDnaflag() {
		return dnaflag;
	}

	public void setDnaflag(String dnaflag) {
		this.dnaflag = dnaflag;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getFlmc() {
		return flmc;
	}

	public void setFlmc(String flmc) {
		this.flmc = flmc;
	}

	public String getGzd() {
		return gzd;
	}

	public void setGzd(String gzd) {
		this.gzd = gzd;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getWzmc() {
		return wzmc;
	}

	public void setWzmc(String wzmc) {
		this.wzmc = wzmc;
	}
}
