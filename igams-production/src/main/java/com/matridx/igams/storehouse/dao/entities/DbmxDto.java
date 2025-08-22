package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DbmxDto")
public class DbmxDto extends DbmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//调出货位名称
	private String dchwmc;
	//调入货位名称
	private String drhwmc;
	//生产日期
	private String scrq;
	//失效日期
	private String yxq;
	//生产批号
	private String scph;
	//物料编码
	private String wlbm;
	//物料id
	private String wlid;
	//物料名称
	private  String wlmc;
	//库存量
	private String kcl;
	//调拨总数
	private String dbzs;
	//仓库权限类型
	private String ckqxlx;
	//录入人员名称
	private String lrryxm;
	//追朔号
	private String zsh;
	//类别
	private String lb;
	//类别名称
	private String lbmc;
	//生产商
	private String scs;
	//原厂货号
	private String ychh;
	//计量单位
	private String jldw;
	//规格
	private String gg;

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
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

	public String getLrryxm() {
		return lrryxm;
	}

	public void setLrryxm(String lrryxm) {
		this.lrryxm = lrryxm;
	}

	public String getDbzs() {
		return dbzs;
	}
	public void setDbzs(String dbzs) {
		this.dbzs = dbzs;
	}
	public String getCkqxlx() {
		return ckqxlx;
	}
	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}
	public String getDchwmc() {
		return dchwmc;
	}
	public void setDchwmc(String dchwmc) {
		this.dchwmc = dchwmc;
	}
	public String getDrhwmc() {
		return drhwmc;
	}
	public void setDrhwmc(String drhwmc) {
		this.drhwmc = drhwmc;
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
	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid) {
		this.wlid = wlid;
	}
	public String getWlmc() {
		return wlmc;
	}
	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}
	public String getKcl() {
		return kcl;
	}
	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
	
	
}
