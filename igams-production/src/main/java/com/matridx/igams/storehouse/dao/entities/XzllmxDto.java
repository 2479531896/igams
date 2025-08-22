package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzllmxDto")
public class XzllmxDto extends XzllmxModel{

	//库存量
	private String kcl;
	//预定数
	private String yds;
	//可领数量
	private String klsl;
	//生产商
	private String scs;
	//请购明细ID
	private String qgmxid;
	//请购ID
	private String qgid;
	//货物名称
	private String hwmc;
	//货物标准
	private String hwbz;
	//货物计量单位
	private String hwjldw;
	//单据号
	private String djh;
	//领料人员
	private String llrymc;
	//领料单号
	private String lldh;
	private String ckzt;

	public String getCkzt() {
		return ckzt;
	}

	public void setCkzt(String ckzt) {
		this.ckzt = ckzt;
	}

	public String getLldh() {
		return lldh;
	}

	public void setLldh(String lldh) {
		this.lldh = lldh;
	}

	public String getLlrymc() {
		return llrymc;
	}

	public void setLlrymc(String llrymc) {
		this.llrymc = llrymc;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	@Override
	public String getHwmc() {
		return hwmc;
	}

	@Override
	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	@Override
	public String getHwjldw() {
		return hwjldw;
	}

	@Override
	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
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

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
