package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjgzbyDto")
public class SjgzbyDto extends SjgzbyModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//基础数据CSID
	private String csid;
	//基础数据的参数代码
	private String csdm;
	//基础数据的参数名称
	private String csmc;
	
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
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}

}
