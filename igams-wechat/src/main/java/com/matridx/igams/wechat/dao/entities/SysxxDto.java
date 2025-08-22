package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SysxxDto")
public class SysxxDto extends SysxxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String wfsysmc;
	private String wfsysdm;
	private String wfsysid;

	public String getWfsysmc() {
		return wfsysmc;
	}

	public void setWfsysmc(String wfsysmc) {
		this.wfsysmc = wfsysmc;
	}

	public String getWfsysdm() {
		return wfsysdm;
	}

	public void setWfsysdm(String wfsysdm) {
		this.wfsysdm = wfsysdm;
	}

	public String getWfsysid() {
		return wfsysid;
	}

	public void setWfsysid(String wfsysid) {
		this.wfsysid = wfsysid;
	}
}
