package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XtshDto")
public class XtshDto extends XtshModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//审核类别名称
	private String shlbmc;
	//回调类
	private String hdl;
	//回调Dto
	private String hdmx;
	//分区类别
	private String fqlb;
	//业务ID，审核过程的ywid
	private String ywid;
	//业务名称，审核过程关联的ywid对应的具体的业务表
	private String ywmc;
	//审核延期时间
	private String yqrs;
	//审核延期小时
	private String yqxs;
	//审核时间
	private String shsj;
	//待审核业务提交时间
	private String tjsj;
	//审批岗位名称
	private String spgwmc;
	//岗位id
	private String gwid;
	//流程序号
	private String lcxh;
	private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLcxh() {
		return lcxh;
	}

	public void setLcxh(String lcxh) {
		this.lcxh = lcxh;
	}

	public String getGwid() {
		return gwid;
	}

	public void setGwid(String gwid) {
		this.gwid = gwid;
	}

	public String getSpgwmc() {
		return spgwmc;
	}

	public void setSpgwmc(String spgwmc) {
		this.spgwmc = spgwmc;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public String getYqrs() {
		return yqrs;
	}

	public void setYqrs(String yqrs) {
		this.yqrs = yqrs;
	}

	public String getYqxs() {
		return yqxs;
	}

	public void setYqxs(String yqxs) {
		this.yqxs = yqxs;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}

	public String getShlbmc() {
		return shlbmc;
	}

	public void setShlbmc(String shlbmc) {
		this.shlbmc = shlbmc;
	}

	public String getHdl() {
		return hdl;
	}

	public void setHdl(String hdl) {
		this.hdl = hdl;
	}

	public String getHdmx() {
		return hdmx;
	}

	public void setHdmx(String hdmx) {
		this.hdmx = hdmx;
	}

	public String getFqlb() {
		return fqlb;
	}

	public void setFqlb(String fqlb) {
		this.fqlb = fqlb;
	}
}
