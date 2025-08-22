package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XzqgqrmxModel")
public class XzqgqrmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//确认明细ID
	private String qrmxid;
	//确认ID
	private String qrid;
	//请购明细ID
	private String qgmxid;
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
	//请购id
	private String qgid;

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getQrmxid() {
		return qrmxid;
	}

	public void setQrmxid(String qrmxid) {
		this.qrmxid = qrmxid;
	}

	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
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
}
