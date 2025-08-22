package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CpxqjhDto")
public class CpxqjhDto extends CpxqjhModel{

	//申请人名称x
	private String sqrmc;
	//钉钉审批人用户id
	private String sprid;
	//钉钉审批人用户姓名
	private String sprxm;
	//钉钉审批人钉钉ID
	private String sprddid;
	//钉钉审批人用户名
	private String spryhm;
	//钉钉审批人角色ID
	private String sprjsid;
	//钉钉审批人角色名称
	private String sprjsmc;
	//申请部门名称
	private String sqbmmc;
	//物料名称
	private String wlmc;
	//物料编码
	private String wlbm;
	//查询条件
	private String entire;
	//需求开始时间
	private String xqrqstart;
	//需求结束时间
	private String xqrqend;
	//操作标识
	private String czbs;
	//生产商
	private String scs;
	//规格
	private String gg;
	//计量单位
	private String jldw;
	private String sl;//数量
	private String scsl;//生产数量
	private String yq;//批次和批量要求
	private String xqjhmxid;
	//需求计划明细
	private String xqjhmx_json;
	//附件ids
	private List<String> fjids;
	//钉钉标记
	private String ddbj;

	//区分不同的审核页面
	private String xsbj;
	//钉钉审批ID
	private String ddslid;
	//库存量
	private String kcl;//库存量
	private String scph;//生产批号
	private String scrq;//生产日期
	private String yxq;//有效期
	private String ckmc;//仓库名称
	private String wlid;//物料id
	private String lx;//区分是产品还是销售
	private String sczt;//生产状态
	private String[] sczts;//生产状态多
	private String flag;
	private String xqsl;//需求数量
	private String rksl;//入库数量

	public void setXqsl(String xqsl) {
		this.xqsl = xqsl;
	}

	public String getRksl() {
		return rksl;
	}

	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String getXqsl() {
		return xqsl;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String[] getSczts() {
		return sczts;
	}

	public void setSczts(String[] sczts) {
		this.sczts = sczts;
	}

	public String getSczt() {
		return sczt;
	}

	public void setSczt(String sczt) {
		this.sczt = sczt;
	}
	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
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

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getXqjhmxid() {
		return xqjhmxid;
	}

	public void setXqjhmxid(String xqjhmxid) {
		this.xqjhmxid = xqjhmxid;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
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

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getScsl() {
		return scsl;
	}

	public void setScsl(String scsl) {
		this.scsl = scsl;
	}

	public String getYq() {
		return yq;
	}

	public void setYq(String yq) {
		this.yq = yq;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getSprid() {
		return sprid;
	}

	public void setSprid(String sprid) {
		this.sprid = sprid;
	}

	public String getSprxm() {
		return sprxm;
	}

	public void setSprxm(String sprxm) {
		this.sprxm = sprxm;
	}

	public String getSprddid() {
		return sprddid;
	}

	public void setSprddid(String sprddid) {
		this.sprddid = sprddid;
	}

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getDdslid() {
		return ddslid;
	}

	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getXsbj() {
		return xsbj;
	}

	public void setXsbj(String xsbj) {
		this.xsbj = xsbj;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getXqjhmx_json() {
		return xqjhmx_json;
	}

	public void setXqjhmx_json(String xqjhmx_json) {
		this.xqjhmx_json = xqjhmx_json;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getXqrqstart() {
		return xqrqstart;
	}

	public void setXqrqstart(String xqrqstart) {
		this.xqrqstart = xqrqstart;
	}

	public String getXqrqend() {
		return xqrqend;
	}

	public void setXqrqend(String xqrqend) {
		this.xqrqend = xqrqend;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
