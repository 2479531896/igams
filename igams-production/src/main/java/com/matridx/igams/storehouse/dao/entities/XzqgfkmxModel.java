package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XzqgfkmxModel")
public class XzqgfkmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//付款明细ID
	private String fkmxid;
	//付款ID
	private String fkid;
	//序号
	private String xh;
	//货物名称
	private String hwmc;
	//货物规格
	private String hwgg;
	//数量
	private String sl;
	//价格
	private String jg;
	//货物计量单位
	private String hwjldw;
	//确认ID
	private String qrid;
	//确认明细ID
	private String qrmxid;
	//请购ID
	private String qgid;
	//请购明细ID
	private String qgmxid;
	//付款标记
	private String fkbj;

	public String getFkbj() {
		return fkbj;
	}

	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}

	public String getFkmxid() {
		return fkmxid;
	}

	public void setFkmxid(String fkmxid) {
		this.fkmxid = fkmxid;
	}

	public String getFkid() {
		return fkid;
	}

	public void setFkid(String fkid) {
		this.fkid = fkid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
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

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}

	public String getQrmxid() {
		return qrmxid;
	}

	public void setQrmxid(String qrmxid) {
		this.qrmxid = qrmxid;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}
}
