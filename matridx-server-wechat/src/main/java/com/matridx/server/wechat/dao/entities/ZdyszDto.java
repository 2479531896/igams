package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ZdyszDto")
public class ZdyszDto extends ZdyszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//是否应用名称
	private String sfyymc;
	//自定义设置list
	private String list;
	//方案图片路径
	private String faimglj;
	//方案图片id
	private String fjid;
	//小程序下载图片路径
	private String xzimglj;
	
	public String getXzimglj() {
		return xzimglj;
	}

	public void setXzimglj(String xzimglj) {
		this.xzimglj = xzimglj;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getFaimglj() {
		return faimglj;
	}

	public void setFaimglj(String faimglj) {
		this.faimglj = faimglj;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getSfyymc() {
		return sfyymc;
	}

	public void setSfyymc(String sfyymc) {
		this.sfyymc = sfyymc;
	}
	
}
