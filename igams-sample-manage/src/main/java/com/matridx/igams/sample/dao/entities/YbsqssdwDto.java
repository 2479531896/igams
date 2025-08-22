package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YbsqssdwDto")
public class YbsqssdwDto extends YbsqssdwModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//机构名称
	private String jgmc;

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	
	
}
