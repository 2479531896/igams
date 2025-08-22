package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhzcyModel")
public class YhzcyModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String yhzid;
	private String yhid;

	public String getYhzid() {
		return yhzid;
	}

	public void setYhzid(String yhzid) {
		this.yhzid = yhzid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
}
