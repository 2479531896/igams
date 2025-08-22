package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="FzbbztDto")
public class FzbbztDto extends FzbbztModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//状态名称
	private String ztmc;

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}
}
