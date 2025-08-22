package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="LyrkxxDto")
public class LyrkxxDto extends LyrkxxModel{
	//库存量
	private String kcl;
	//领料单号
	private String lldh;
	//留样库存id
	private String lykcid;
	private String lysjstart;
	private String lysjend;
	//产品编号
	private String ychh;
	//规格
	private String gg;
	//单位
	private String jldw;
	//生产批号
	private String scph;
	//记录编号
	private String jlbh;
	//保存条件
	private String bctj;
	//有效期
	private String yxq;
	//生产日期
	private String scrq;
	//物料名称
	private String wlmc;
	//物料编码
	private String wlbm;
	//留样人员
	private String lyry;
	private String entire;
	//物料id
	private String wlid;
	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLyry() {
		return lyry;
	}

	public void setLyry(String lyry) {
		this.lyry = lyry;
	}

	public String getLysjstart() {
		return lysjstart;
	}

	public void setLysjstart(String lysjstart) {
		this.lysjstart = lysjstart;
	}

	public String getLysjend() {
		return lysjend;
	}

	public void setLysjend(String lysjend) {
		this.lysjend = lysjend;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
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

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
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

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getLykcid() {
		return lykcid;
	}

	public void setLykcid(String lykcid) {
		this.lykcid = lykcid;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getLldh() {
		return lldh;
	}

	public void setLldh(String lldh) {
		this.lldh = lldh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
