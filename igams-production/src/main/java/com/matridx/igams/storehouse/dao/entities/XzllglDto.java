package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzllglDto")
public class XzllglDto extends XzllglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    //当前角色
	private String dqshzt;
	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//录入人员名称
	private String lrrymc;
	//模糊查询 全部
	private String entire;
	private String sqrqstart;
	private String sqrqend;
	//状态[多]
	private String[] zts;
	//机构id
	private String jgid;
	//机构代码
	private String jgdm;
	//请领数
	private String qlsl;
	//行政入库明细id
	private String xzrkmxid;
	//库存量
	private String kcl;
	//库存量
	private String llmx_json;
	//录入人员姓名
	private String llryxm;
	//钉钉id
	private String ddid;
	//页面标记
	private String loadFlag;
	//发料人员
	private String flry;
	//库位多
	private String[] kws;
		//领料人员名称
	private String llrymc;
	//发料人员名称
	private String flrymc;
	//行政库存ID
	private String xzkcid;
	//预定数
	private String yds;
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
	private String sqlParam;
	//货物名称
	private String hwmc;
	//规格
	private String hwgg;
	//单位
	private String hwjldw;
	//请领数
	private String qls;
	//货物标准
	private String hwbz;
	//安全库存
	private String aqkc;
	//出库数量
	private String cksl;
	//钉钉保存标记
	private String ddbcbj;
	private String[] lllxs;
	private String tssh;
	private String sbysid;

	public String getSbysid() {
		return sbysid;
	}

	public void setSbysid(String sbysid) {
		this.sbysid = sbysid;
	}

	public String getTssh() {
		return tssh;
	}

	public void setTssh(String tssh) {
		this.tssh = tssh;
	}

	public String[] getLllxs() {
		return lllxs;
	}

	public void setLllxs(String[] lllxs) {
		this.lllxs = lllxs;
	}

	public String getDdbcbj() {
		return ddbcbj;
	}

	public void setDdbcbj(String ddbcbj) {
		this.ddbcbj = ddbcbj;
	}

	public String getCksl() {
		return cksl;
	}

	public void setCksl(String cksl) {
		this.cksl = cksl;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getAqkc() {
		return aqkc;
	}

	public void setAqkc(String aqkc) {
		this.aqkc = aqkc;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHwgg() {
		return hwgg;
	}

	public void setHwgg(String hwgg) {
		this.hwgg = hwgg;
	}

	public String getHwjldw() {
		return hwjldw;
	}

	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}

	public String getQls() {
		return qls;
	}

	public void setQls(String qls) {
		this.qls = qls;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
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

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String getQlsl() {
		return qlsl;
	}

	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}

	public String getFlrymc() {
		return flrymc;
	}

	public void setFlrymc(String flrymc) {
		this.flrymc = flrymc;
	}

	public String getLlrymc() {
		return llrymc;
	}

	public void setLlrymc(String llrymc) {
		this.llrymc = llrymc;
	}

	public String[] getKws() {
		return kws;
	}

	public void setKws(String[] kws) {
		this.kws = kws;
	}

	public String getFlry() {
		return flry;
	}

	public void setFlry(String flry) {
		this.flry = flry;
	}

	public String getLoadFlag() {
		return loadFlag;
	}

	public void setLoadFlag(String loadFlag) {
		this.loadFlag = loadFlag;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getLlryxm() {
		return llryxm;
	}

	public void setLlryxm(String llryxm) {
		this.llryxm = llryxm;
	}

	public String getLlmx_json() {
		return llmx_json;
	}

	public void setLlmx_json(String llmx_json) {
		this.llmx_json = llmx_json;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
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

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getDqshzt() {
		return dqshzt;
	}

	public void setDqshzt(String dqshzt) {
		this.dqshzt = dqshzt;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getSqrqstart() {
		return sqrqstart;
	}

	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}

	public String getSqrqend() {
		return sqrqend;
	}

	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}

	public String getXzrkmxid() {
		return xzrkmxid;
	}

	public void setXzrkmxid(String xzrkmxid) {
		this.xzrkmxid = xzrkmxid;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
}
