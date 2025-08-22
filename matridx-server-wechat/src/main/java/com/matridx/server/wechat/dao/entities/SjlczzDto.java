package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjlczzDto")
public class SjlczzDto extends SjlczzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//临床症状名称
	private String csmc;

	//临床症状CSID
	private String csid;
	//基础数据的临床症状代码
	private String csdm;
	
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid) {
		this.csid = csid;
	}
	public String getCsdm() {
		return csdm;
	}
	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}
}
