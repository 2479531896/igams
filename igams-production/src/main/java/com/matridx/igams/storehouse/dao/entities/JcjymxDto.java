package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JcjymxDto")
public class JcjymxDto extends JcjymxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//预定数
	private String yds;
	//库存量
	private String kcl;
	//仓库货物id
	private String ckhwid;
	//仓库库存量
	private String ckkcl;
	//仓库预定数
	private String ckyds;
	//仓库id
	private String ckid;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//仓库代码
	private String ckdm;
	//库位代码
	private String kwdm;
	//生产日期
	private String scrq;
	//有效期
	private String yxq;
	//有效期标志
	private String yxqbz;
	//保质期
	private String bzq;
	//库存关联id
	private String kcglid;
	//生产批号
	private  String scph;
	//预计归还日期
	private String yjghrq;
	//可还数量
	private String khsl;
	//货物ID
	private String hwid;
	//规格
	private String gg;
	//单位
	private String jldw;
	//库位编号
	private String kwbh;
	//物料id
	private String wlid;
	//大类名称
	private String dlmc;
	//大类代码
	private String dldm;
	//编码名称
	private String bmmc;
	//编码代码
	private String bmdm;
	//物料质量类别代码
	private String lbdm;
	//物料质量类别参数拓展1
	private String lbcskz1;
	//借出数量
	private String jcsl;
	//状态
	private String zt;
	//追溯号
	private String zsh;
	//已还数量
	private String yhsl;
	//客户名称
	private String khmc;
	//客户ID
	private String khid;
	//借用日期
	private String jyrq;
	//设备验收id
	private String sbysid;
	//设备状态
	private String sbzt;
	private String ckmc;
	private String xlh;

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getSbzt() {
		return sbzt;
	}

	public void setSbzt(String sbzt) {
		this.sbzt = sbzt;
	}


	public String getJyrq() {
		return jyrq;
	}

	public void setJyrq(String jyrq) {
		this.jyrq = jyrq;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getYhsl() {
		return yhsl;
	}

	public void setYhsl(String yhsl) {
		this.yhsl = yhsl;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getJcsl() {
		return jcsl;
	}

	public void setJcsl(String jcsl) {
		this.jcsl = jcsl;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getLbdm() {
		return lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getDlmc() {
		return dlmc;
	}

	public void setDlmc(String dlmc) {
		this.dlmc = dlmc;
	}

	public String getDldm() {
		return dldm;
	}

	public void setDldm(String dldm) {
		this.dldm = dldm;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getBmdm() {
		return bmdm;
	}

	public void setBmdm(String bmdm) {
		this.bmdm = bmdm;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getKwbh() {
		return kwbh;
	}

	public void setKwbh(String kwbh) {
		this.kwbh = kwbh;
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

	public String getKhsl() {
		return khsl;
	}

	public void setKhsl(String khsl) {
		this.khsl = khsl;
	}

	@Override
	public String getHwid() {
		return hwid;
	}

	@Override
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getYjghrq() {
		return yjghrq;
	}

	public void setYjghrq(String yjghrq) {
		this.yjghrq = yjghrq;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getKcglid() {
		return kcglid;
	}

	public void setKcglid(String kcglid) {
		this.kcglid = kcglid;
	}

	public String getBzq() {
		return bzq;
	}

	public void setBzq(String bzq) {
		this.bzq = bzq;
	}

	public String getYxqbz() {
		return yxqbz;
	}

	public void setYxqbz(String yxqbz) {
		this.yxqbz = yxqbz;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getKwdm() {
		return kwdm;
	}

	public void setKwdm(String kwdm) {
		this.kwdm = kwdm;
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

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getCkyds() {
		return ckyds;
	}

	public void setCkyds(String ckyds) {
		this.ckyds = ckyds;
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

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getCkkcl() {
		return ckkcl;
	}

	public void setCkkcl(String ckkcl) {
		this.ckkcl = ckkcl;
	}
}
