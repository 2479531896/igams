package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzqgqrcglDto")
public class XzqgqrcglDto extends XzqgqrcglModel{

	//货物名称
	private String hwmc;
	//货物标准
	private String hwgg;
	//数量
	private String sl;
	//货物计量单位
	private String hwjldw;
	//请购单号
	private String djh;
	//价格
	private String jg;
	//对账方式代码
	private String dzfsdm;
	//申请人
	private String sqr;
	//申请人名称
	private String sqrmc;
	//申请部门
	private String sqbm;
	//申请部门名称
	private String sqbmmc;
	//机构代码
	private String jgdm;
	//请购id
	private String qgid;
	//请购单号
	private String qgdh;
	//是否入库
	private String sfrk;

	public String getSfrk() {
		return sfrk;
	}

	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}

	public String getQgdh() {
		return qgdh;
	}

	public void setQgdh(String qgdh) {
		this.qgdh = qgdh;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getDzfsdm() {
		return dzfsdm;
	}

	public void setDzfsdm(String dzfsdm) {
		this.dzfsdm = dzfsdm;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHwgg() {
		return hwgg;
	}

	public void setHwgg(String hwgg) {
		this.hwgg = hwgg;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
