package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SylcDto")
public class SylcDto extends SylcModel{
	//类型名称
	private String lxmc;
	//明细Json
	private String lcmx_json;
	//显示名称
	private String xsmc;
	//资源标题
	private String zybt;
	//资源路径
	private String zylj;
	//访问路径
	private String fwlj;
	//操作名称
	private String czmc;
	//对应页面
	private String dyym;
	//跳转参数
	private String tzcs;
	//资源ID
	private String zyid;

	public String getZyid() {
		return zyid;
	}

	public void setZyid(String zyid) {
		this.zyid = zyid;
	}

	public String getTzcs() {
		return tzcs;
	}

	public void setTzcs(String tzcs) {
		this.tzcs = tzcs;
	}

	public String getXsmc() {
		return xsmc;
	}

	public void setXsmc(String xsmc) {
		this.xsmc = xsmc;
	}

	public String getZybt() {
		return zybt;
	}

	public void setZybt(String zybt) {
		this.zybt = zybt;
	}

	public String getZylj() {
		return zylj;
	}

	public void setZylj(String zylj) {
		this.zylj = zylj;
	}

	public String getFwlj() {
		return fwlj;
	}

	public void setFwlj(String fwlj) {
		this.fwlj = fwlj;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}

	public String getDyym() {
		return dyym;
	}

	public void setDyym(String dyym) {
		this.dyym = dyym;
	}

	public String getLcmx_json() {
		return lcmx_json;
	}

	public void setLcmx_json(String lcmx_json) {
		this.lcmx_json = lcmx_json;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
