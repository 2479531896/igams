package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias("JycglDto")
public class JycglDto extends JycglModel {
	private static final long serialVersionUID = 1L;

	private String hwids;//货物ids
	private String hwCount;//检验车货物个数
	private String htnbbh;//合同编号
	private String wlbm;//物料编码
	private String wlmc;//物料名称
	private String gg;//物料规格
	private String jldw;//计量单位
	private String ychh;//原厂货号
	private String sl;//数量(到货数量-初验退回数量)
	private String qyl;//取样量
	private String qyrq;//取样日期
	private String zjthsl;//质检退回数量
	private String lbcskz1;//物料类别
	private String wlid;//物料id
	private String bgdh;//货物报告单号
	private String cythsl;//初验退回数量
	private String maxsl; //可取样最大数量
	private String jysl;//借用数量
	private String rklbdm;//入库类别代码
	private String dhlxdm; //到货类型代码
	private String cplb;

	public String getCplb() {
		return cplb;
	}

	public void setCplb(String cplb) {
		this.cplb = cplb;
	}

	public String getDhlxdm() {
		return dhlxdm;
	}

	public void setDhlxdm(String dhlxdm) {
		this.dhlxdm = dhlxdm;
	}

	public String getRklbdm() {
		return rklbdm;
	}

	public void setRklbdm(String rklbdm) {
		this.rklbdm = rklbdm;
	}

	public String getJysl() {
		return jysl;
	}
	public void setJysl(String jysl) {
		this.jysl = jysl;
	}
	public String getMaxsl() {
		return maxsl;
	}
	public void setMaxsl(String maxsl) {
		this.maxsl = maxsl;
	}
	public String getCythsl() {
		return cythsl;
	}
	public void setCythsl(String cythsl) {
		this.cythsl = cythsl;
	}
	public String getBgdh() {
		return bgdh;
	}
	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}
	public String getHwids() {
		return hwids;
	}
	public void setHwids(String hwids) {
		this.hwids = hwids;
	}
	public String getHwCount() {
		return hwCount;
	}
	public void setHwCount(String hwCount) {
		this.hwCount = hwCount;
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
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getYchh() {
		return ychh;
	}
	public void setYchh(String ychh) {
		this.ychh = ychh;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getQyl() {
		return qyl;
	}
	public void setQyl(String qyl) {
		this.qyl = qyl;
	}
	public String getQyrq() {
		return qyrq;
	}
	public void setQyrq(String qyrq) {
		this.qyrq = qyrq;
	}
	public String getZjthsl() {
		return zjthsl;
	}
	public void setZjthsl(String zjthsl) {
		this.zjthsl = zjthsl;
	}
	public String getLbcskz1() {
		return lbcskz1;
	}
	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid) {
		this.wlid = wlid;
	}
	
	
}
