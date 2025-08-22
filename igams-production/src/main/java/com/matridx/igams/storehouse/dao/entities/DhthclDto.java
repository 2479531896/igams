package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "DhthclDto")
public class DhthclDto extends DhthclModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String hwid;
	private String wlbm;// 物料编码
	private String wlmc;// 物料名称
	private String gg;// 规格
	private String jldw; // 计量单位
	private String gysmc;// 供应商名称
	private String dhrq;// 到货日期
	private String zsh;// 追溯号
	private String dhdh;// 到货单号
	private String htnbbh;// 合同内部编号
	private String lrrymc;//录入人员名称
	private String ysqk;//验收情况
	private String dhrqstart;//到货日期查询start
	private String dhrqend;//到货日期查询end
	//导出关联标记位//所选择的字段
	private String SqlParam; 	
	//查询参数[多个条件]
	private String searchParam;
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
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getDhrq() {
		return dhrq;
	}
	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}
	public String getZsh() {
		return zsh;
	}
	public void setZsh(String zsh) {
		this.zsh = zsh;
	}
	public String getDhdh() {
		return dhdh;
	}
	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}
	public String getHtnbbh() {
		return htnbbh;
	}
	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}
	public String getLrrymc() {
		return lrrymc;
	}
	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
	public String getYsqk() {
		return ysqk;
	}
	public void setYsqk(String ysqk) {
		this.ysqk = ysqk;
	}
	public String getDhrqstart() {
		return dhrqstart;
	}
	public void setDhrqstart(String dhrqstart) {
		this.dhrqstart = dhrqstart;
	}
	public String getDhrqend() {
		return dhrqend;
	}
	public void setDhrqend(String dhrqend) {
		this.dhrqend = dhrqend;
	}
	public String getSqlParam() {
		return SqlParam;
	}
	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}
	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid) {
		this.hwid = hwid;
	}
	
}
