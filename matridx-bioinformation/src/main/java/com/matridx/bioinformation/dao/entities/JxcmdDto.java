package com.matridx.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JxcmdDto")
public class JxcmdDto extends JxcmdModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ljmc;
	private String lj;

	public String getLjmc() {
		return ljmc;
	}

	public void setLjmc(String ljmc) {
		this.ljmc = ljmc;
	}

	public String getLj() {
		return lj;
	}

	public void setLj(String lj) {
		this.lj = lj;
	}
}
