package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HtmxDto")
public class HtmxDto extends HtmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//单据号
	private String djh;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	//请购申请部门
	private String sqbm;
	//请购申请部门名称
	private String sqbmmc;
	//请购数量
	private String qgsl;
	//合同内部编号
	private String htnbbh;
	//物料类别名称
	private String wllbmc;
	//物料子类别名称
	private String wlzlbmc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//原厂货号
	private String ychh;
	//合同ID
	private String htid;
	//查询字段(全部)
	private String entire;
	//合同外部编号
	private String htwbbh;
	private String SqlParam; 	//导出关联标记位//所选择的字段

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getHtwbbh() {
		return htwbbh;
	}

	public void setHtwbbh(String htwbbh) {
		this.htwbbh = htwbbh;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String getWlzlbmc() {
		return wlzlbmc;
	}

	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}

	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}

	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getQgsl() {
		return qgsl;
	}

	public void setQgsl(String qgsl) {
		this.qgsl = qgsl;
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

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
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
}
