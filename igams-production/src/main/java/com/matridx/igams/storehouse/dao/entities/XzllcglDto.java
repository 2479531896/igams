package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzllcglDto")
public class XzllcglDto extends XzllcglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//行政入库明细ID
	private String xzrkmxid;
	//请购明细ID
	private String qgmxid;
	//仓库ID
	private String ckid;
	//库位ID
	private String kwid;

	//备注
	private String bz;
	//货物名称
	private String hwmc;
	//货物规格
	private String hwgg;
	//库存量
	private String kcl;
	//货物计量单位
	private String hwjldw;
	//货物标准
	private String hwbz;
	//生产商
	private String scs;
	//请购单号
	private String djh;
	//可领数量
	private String klsl;
	//预定数
	private String yds;
	//请领数量
	private String qlsl;
	//请购ID
	private String qgid;
	//库位
	private String kw;

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getQlsl() {
		return qlsl;
	}

	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}

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

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getKlsl() {
		return klsl;
	}

	public void setKlsl(String klsl) {
		this.klsl = klsl;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getXzrkmxid() {
		return xzrkmxid;
	}

	public void setXzrkmxid(String xzrkmxid) {
		this.xzrkmxid = xzrkmxid;
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

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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
