package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DbglDto")
public class DbglDto extends DbglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//调拨明细json
	private String dbmx_json;
	//转入仓库代码
	private String zrckdm;
	//转出仓库代码
	private String zcckdm;
	//录入人员名称
	private String lrrymc;
	//转出仓库类型
	private String zcckqxlx;
	//转入仓库类型
	private String zrckqxlx;
	//经手人名称
	private String jsrmc;
	//转出仓库名称
	private String zcckmc;
	//转ru仓库名称
	private String zrckmc;
	//出库类别
	private String cklbmc;
	//入库类别
	private String rklbmc;
	//entire
	private String entire;
	//货物ids
	private String hwxxids;
	//转出仓库id
	private String zcckid;
	//转入仓库id
	private String zrckid;

	public String getZcckid() {
		return zcckid;
	}

	public void setZcckid(String zcckid) {
		this.zcckid = zcckid;
	}

	public String getZrckid() {
		return zrckid;
	}

	public void setZrckid(String zrckid) {
		this.zrckid = zrckid;
	}

	public String getHwxxids() {
		return hwxxids;
	}

	public void setHwxxids(String hwxxids) {
		this.hwxxids = hwxxids;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getZcckmc() {
		return zcckmc;
	}

	public void setZcckmc(String zcckmc) {
		this.zcckmc = zcckmc;
	}

	public String getZrckmc() {
		return zrckmc;
	}

	public void setZrckmc(String zrckmc) {
		this.zrckmc = zrckmc;
	}

	public String getCklbmc() {
		return cklbmc;
	}

	public void setCklbmc(String cklbmc) {
		this.cklbmc = cklbmc;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getJsrmc() {
		return jsrmc;
	}

	public void setJsrmc(String jsrmc) {
		this.jsrmc = jsrmc;
	}

	public String getZcckqxlx() {
		return zcckqxlx;
	}

	public void setZcckqxlx(String zcckqxlx) {
		this.zcckqxlx = zcckqxlx;
	}

	public String getZrckqxlx() {
		return zrckqxlx;
	}

	public void setZrckqxlx(String zrckqxlx) {
		this.zrckqxlx = zrckqxlx;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getZrckdm() {
		return zrckdm;
	}

	public void setZrckdm(String zrckdm) {
		this.zrckdm = zrckdm;
	}

	public String getZcckdm() {
		return zcckdm;
	}

	public void setZcckdm(String zcckdm) {
		this.zcckdm = zcckdm;
	}

	public String getDbmx_json() {
		return dbmx_json;
	}

	public void setDbmx_json(String dbmx_json) {
		this.dbmx_json = dbmx_json;
	}
	
}
