package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbxxzDto")
public class HbxxzDto extends HbxxzModel{

	private String hbmc;
	private String jcdwmc;
	private String[] jcdws;
	//复数ID

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
