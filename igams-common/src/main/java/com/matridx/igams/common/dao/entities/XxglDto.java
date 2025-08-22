package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XxglDto")
public class XxglDto extends XxglModel{
	private String xxlxmc;
	private String[] xxlxs;
	private String cskz1;

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String[] getXxlxs() {
		return xxlxs;
	}

	public void setXxlxs(String[] xxlxs) {
		this.xxlxs = xxlxs;
		for (int i = 0; i < xxlxs.length; i++) {
			this.xxlxs[i] = this.xxlxs[i].replace("'", "");
		}
	}

	
	public String getXxlxmc() {
		return xxlxmc;
	}

	public void setXxlxmc(String xxlxmc) {
		this.xxlxmc = xxlxmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
