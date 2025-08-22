package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="QcxmDto")
public class QcxmDto extends QcxmModel {

	//项目名称
	private String xmmc;
	//项目参数扩展1
	private String xmcskz1;

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getXmcskz1() {
		return xmcskz1;
	}

	public void setXmcskz1(String xmcskz1) {
		this.xmcskz1 = xmcskz1;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
