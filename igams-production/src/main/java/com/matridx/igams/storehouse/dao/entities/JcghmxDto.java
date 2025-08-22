package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JcghmxDto")
public class JcghmxDto extends JcghmxModel{
	//物料ID
	private String wlid;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//生产批号
	private String scph;
	//生产日期
	private String scrq;
	//可还数量
	private String khsl;
	//失效日期
	private String yxq;
	//仓库ID
	private String ckid;
	//库位编号
	private String kwbh;
	//仓库代码
	private String ckdm;
	//库位代码
	private String kwdm;
	//保质期
	private String bzq;
	//有效期备注
	private String yxqbz;
	//有效期标记
	private String bzqflg;
	//数量
	private String sl;
	//库存量
	private String kcl;
	//仓库名称
	private String ckmc;
	//库位名称
	private String kwmc;
	//追溯号
	private String zsh;
	//已还数量
	private String yhsl;
	//借用数量
	private String jysl;
	//物料质量类别参数拓展1
	private String lbcskz1;
	//设备验收id
	private String sbysid;
	private String xlh;
	private String jymxglid;
	public String getJymxglid() {
		return jymxglid;
	}

	public void setJymxglid(String jymxglid) {
		this.jymxglid = jymxglid;
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

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getYhsl() {
		return yhsl;
	}

	public void setYhsl(String yhsl) {
		this.yhsl = yhsl;
	}

	public String getJysl() {
		return jysl;
	}

	public void setJysl(String jysl) {
		this.jysl = jysl;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getBzqflg() {
		return bzqflg;
	}

	public void setBzqflg(String bzqflg) {
		this.bzqflg = bzqflg;
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

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getKwbh() {
		return kwbh;
	}

	public void setKwbh(String kwbh) {
		this.kwbh = kwbh;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
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

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getKhsl() {
		return khsl;
	}

	public void setKhsl(String khsl) {
		this.khsl = khsl;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
