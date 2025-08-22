package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="RkcglDto")
public class RkcglDto extends RkcglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//物料单位
	private String jldw;
	//物料规格
	private String gg;
	//到货单号
	private String dhdh;
	//追溯号
	private String zsh;
	//入库数量：到货数量-初验退回数量-质检退回数量
	private String rksl;
	//取样量
	private String qyl;
	//生产日期
	private String scrq;
	//有效期至
	private String yxq;
	//合同内部编号
	private String htnbbh;
	//入库备注
	private String rkbz;
	//申请部门
	private String sqbm;
	//申请部门名称
	private String sqbmmc;
	//供应商id
	private String gysid;
	//供应商名称
	private String gysmc;
	//数量
	private String sl;
	//生产批号
	private String scph;
	//仓库id
	private String ckid;
	//类别标记
	private String lbbj;
	//类别参数名称
	private String lbcsmc;
	//仓库名称
	private String ckmc;
	//仓库名称
	private String lb;
	//入库类别
	private String rklb;
	//入库类别名称
	private String rklbmc;
	//采购类别
	private String cglx;
	//到货仓库名称
	private String dhckmc;
	//到货仓库id
	private String dhckid;
	//到货备注
	private String dhbz;
	//到货类型名称
	private String dhlxmc;
	//到货类型代码
	private String dhlxdm;
	//到货主表备注
	private String dhzbz;
	//产品注册号
	private String cpzch;

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}
	public String getDhzbz() {
		return dhzbz;
	}

	public void setDhzbz(String dhzbz) {
		this.dhzbz = dhzbz;
	}

	public String getDhlxdm() {
		return dhlxdm;
	}

	public void setDhlxdm(String dhlxdm) {
		this.dhlxdm = dhlxdm;
	}

	public String getDhlxmc() {
		return dhlxmc;
	}

	public void setDhlxmc(String dhlxmc) {
		this.dhlxmc = dhlxmc;
	}

	public String getDhbz() {
		return dhbz;
	}

	public void setDhbz(String dhbz) {
		this.dhbz = dhbz;
	}

	public String getDhckmc() {
		return dhckmc;
	}

	public void setDhckmc(String dhckmc) {
		this.dhckmc = dhckmc;
	}

	public String getDhckid() {
		return dhckid;
	}

	public void setDhckid(String dhckid) {
		this.dhckid = dhckid;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getCglx() {
		return cglx;
	}

	public void setCglx(String cglx) {
		this.cglx = cglx;
	}

	public String getRklb() {
		return rklb;
	}

	public void setRklb(String rklb) {
		this.rklb = rklb;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getCkmc() {
		return ckmc;
	}
	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}
	public String getLbcsmc() {
		return lbcsmc;
	}
	public void setLbcsmc(String lbcsmc) {
		this.lbcsmc = lbcsmc;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getLbbj() {
		return lbbj;
	}
	public void setLbbj(String lbbj) {
		this.lbbj = lbbj;
	}
	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
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
	public String getGysid() {
		return gysid;
	}
	public void setGysid(String gysid) {
		this.gysid = gysid;
	}
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getRkbz() {
		return rkbz;
	}
	public void setRkbz(String rkbz) {
		this.rkbz = rkbz;
	}
	public String getHtnbbh() {
		return htnbbh;
	}
	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	public String getWlmc() {
		return wlmc;
	}
	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
	}
	public String getDhdh() {
		return dhdh;
	}
	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}
	public String getZsh() {
		return zsh;
	}
	public void setZsh(String zsh) {
		this.zsh = zsh;
	}
	public String getRksl() {
		return rksl;
	}
	public void setRksl(String rksl) {
		this.rksl = rksl;
	}
	public String getQyl() {
		return qyl;
	}
	public void setQyl(String qyl) {
		this.qyl = qyl;
	}
	public String getScrq() {
		return scrq;
	}
	public void setScrq(String scrq) {
		this.scrq = scrq;
	}
	public String getYxq() {
		return yxq;
	}
	public void setYxq(String yxq) {
		this.yxq = yxq;
	}
	

}
