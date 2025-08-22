package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbdwqxDto")
public class HbdwqxDto extends HbdwqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//参数ID
	private String csid;
	//检测单位名称
	private String jcdwmc;
	
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid) {
		this.csid = csid;
	}
	public String getJcdwmc() {
		return jcdwmc;
	}
	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}
	
}
