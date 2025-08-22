package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YhssjgDto")
public class YhssjgDto extends YhssjgModel{

	//结构名称
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

	//分布式标记
	private String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
