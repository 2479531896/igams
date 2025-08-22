package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="BmwlDto")
public class BmwlDto extends BmwlModel {
	//机构名称
	private String jgmc;

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
