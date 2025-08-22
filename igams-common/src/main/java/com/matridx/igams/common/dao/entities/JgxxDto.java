package com.matridx.igams.common.dao.entities;


import org.apache.ibatis.type.Alias;


@Alias(value="JgxxDto")
public class JgxxDto extends JgxxModel{

	private static final long serialVersionUID = 1L;

	//父机构名称
	private String fjgmc;
	//角色id
	private String jsid;
	//分布式标记
	private String prefix;
	//外部程序名称
	private String wbcxmc;

	private String csid;

	private String csmc;

	public String getWbcxmc() {
		return wbcxmc;
	}

	public void setWbcxmc(String wbcxmc) {
		this.wbcxmc = wbcxmc;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getFjgmc() {
		return fjgmc;
	}

	public void setFjgmc(String fjgmc) {
		this.fjgmc = fjgmc;
	}


	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
}
