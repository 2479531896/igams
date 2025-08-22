package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="FfjlDto")
public class FfjlDto extends FfjlModel {
	//机构名称
	private String jgmc;
	//发放明细
	private String ffmx_json;

	public String getFfmx_json() {
		return ffmx_json;
	}

	public void setFfmx_json(String ffmx_json) {
		this.ffmx_json = ffmx_json;
	}

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
