package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XzllmxModel")
public class XzllmxModel extends BaseModel{
	//行政领料明细id
	private String xzllmxid;
	//行政领料id
	private String xzllid;
	//行政入库明细ID
	private String xzrkmxid;
	//序号
	private String xh;
	//货物名称
	private String hwmc;
	//货物规格
	private String hwgg;
	//货物计量单位
	private String hwjldw;
	//请领数
	private String qlsl;
	//备注
	private String bz;
	//货物标准
	private String hwbz;
	//生产商
	private String scs;
	//库位
	private String kw;
	//出库数量
	private String cksl;
	private String sbysid;

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getCksl() {
		return cksl;
	}

	public void setCksl(String cksl) {
		this.cksl = cksl;
	}

	//行政库存ID
	private String xzkcid;

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getXzllmxid() {
		return xzllmxid;
	}

	public void setXzllmxid(String xzllmxid) {
		this.xzllmxid = xzllmxid;
	}

	public String getXzllid() {
		return xzllid;
	}

	public void setXzllid(String xzllid) {
		this.xzllid = xzllid;
	}

	public String getXzrkmxid() {
		return xzrkmxid;
	}

	public void setXzrkmxid(String xzrkmxid) {
		this.xzrkmxid = xzrkmxid;
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

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	public String getQlsl() {
		return qlsl;
	}

	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
