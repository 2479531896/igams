package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SylcmxDto")
public class SylcmxDto extends SylcmxModel{
	//资源标题
	private String zybt;
	//操作名称
	private String czmc;
	//模块名称
	private String mkmc;

	public String getMkmc() {
		return mkmc;
	}

	public void setMkmc(String mkmc) {
		this.mkmc = mkmc;
	}

	public String getZybt() {
		return zybt;
	}

	public void setZybt(String zybt) {
		this.zybt = zybt;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
