package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XzrkmxModel")
public class XzrkmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//行政入库明细ID
	private String xzrkmxid;
	//行政入库ID
	private String xzrkid;
	//请购明细ID
	private String qgmxid;
	//仓库ID
	private String ckid;
	//库位ID
	private String kwid;
	//入库数量
	private String rksl;
	//备注
	private String bz;
	//序号
	private String xh;
	//货物名称
	private String hwmc;
	//货物规格
	private String hwgg;
	//库存量
	private String kcl;
	//货物计量单位
	private String hwjldw;
	//生产商
	private String scs;
	//货物标准
	private String hwbz;

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getXzrkmxid() {
		return xzrkmxid;
	}

	public void setXzrkmxid(String xzrkmxid) {
		this.xzrkmxid = xzrkmxid;
	}

	public String getXzrkid() {
		return xzrkid;
	}

	public void setXzrkid(String xzrkid) {
		this.xzrkid = xzrkid;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getKwid() {
		return kwid;
	}

	public void setKwid(String kwid) {
		this.kwid = kwid;
	}

	public String getRksl() {
		return rksl;
	}

	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}
}
