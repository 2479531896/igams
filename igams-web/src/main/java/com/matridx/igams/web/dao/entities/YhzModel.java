package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhzModel")
public class YhzModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yhzid;
	private String yhzmc;
	private String zsx;

	public String getZsx() {
		return zsx;
	}

	public void setZsx(String zsx) {
		this.zsx = zsx;
	}

	public String getYhzid() {
		return yhzid;
	}

	public void setYhzid(String yhzid) {
		this.yhzid = yhzid;
	}

	public String getYhzmc() {
		return yhzmc;
	}

	public void setYhzmc(String yhzmc) {
		this.yhzmc = yhzmc;
	}
}
