package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbsfbzDto")
public class HbsfbzDto extends HbsfbzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//项目名称
	private String csmc;
	//项目id
	private String csid;
	//伙伴名称
	private String hbmc;
	//同步标记，判断新增还是修改,1代表新增
	private String insertFlag;
	//伙伴收费标准ID
	private String hbsfbzid;

	public String getHbsfbzid() {
		return hbsfbzid;
	}

	public void setHbsfbzid(String hbsfbzid) {
		this.hbsfbzid = hbsfbzid;
	}

	public String getInsertFlag() {
		return insertFlag;
	}

	public void setInsertFlag(String insertFlag) {
		this.insertFlag = insertFlag;
	}
	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}
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
	
}
