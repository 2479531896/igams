package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XtjsDto")
public class XtjsDto extends XtjsModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户ID
	private String yhid;
	//父角色名称
	private String fjsmc;
	//父角色代码
	private String fjsdm;
	//首页类型名称
	private String sylxmc;
	//首页类型地址
	private String sylxdz;

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getFjsmc() {
		return fjsmc;
	}

	public void setFjsmc(String fjsmc) {
		this.fjsmc = fjsmc;
	}

	public String getFjsdm() {
		return fjsdm;
	}

	public void setFjsdm(String fjsdm) {
		this.fjsdm = fjsdm;
	}

	public String getSylxmc() {
		return sylxmc;
	}

	public void setSylxmc(String sylxmc) {
		this.sylxmc = sylxmc;
	}

	public String getSylxdz() {
		return sylxdz;
	}

	public void setSylxdz(String sylxdz) {
		this.sylxdz = sylxdz;
	}

}
