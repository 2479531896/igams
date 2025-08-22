package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="YxhtDto")
public class YxhtDto extends YxhtModel {
	private String ssywymc;//所属人名称
	private String auditType;//审核类别
	private String khjc;//客户简称
	private String htlxmc;//合同类型名称
	private String hkfsmc;//还款方式名称
	private String htfxcdmc;//合同风险程度名称
	private String xslbmc;//销售类别名称
	private String yzlbmc;//用章类别名称
	private String khmc;//客户名称
	private String sqbmmc;//申请部门名称
	private String oaxsdh;//销售单号
	private String u8xsdh;//u8销售单号
	private String lldh;//领料单号
	private String u8lldh;//u8领料单号
	private String jydh;//借出借用单号
	private String u8jydh;//u8借出单号
	private String ghdh;//归还单号
	private String u8ghdh;//u8归还单号
	private String lrsjstart;
	private String lrsjend;
	private String[] htlxs;//合同类型多
	private String[] khlxs;//客户类型多
	private String[] htqxs;//合同期限多
	private String[] szbjs;//双章标记多
	private String[] xslxs;//销售类型多
	private String[] htfxcds;//合同风险程度多

	private String[] yxhtlxs;
	private String[] htqdzts;//合同签订状态多
	private String[] yzlbs;//用章类别多
	private String entire;
	private String khlxmc;
	private String yzlxmc;
	private String htjemin;
	private String htjemax;
	//附件IDS
	private List<String> fjids;
	private String jgid;
	//审批人角色id
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	private String htrqstart;
	private String htrqend;
	private String zsldmc;
	private String ywyddid;
	private String zsldddid;
	private String ddid;
	private String zsxm;
	private List<String> zslds;
	private String sqlParam;
	private String htjbrmc;//合同经办人
	private String htjbbmmc;//合同经办部门

	private String yxhtlxmc;//营销合同类型名称

	private String yxhtlxdm;

	private String htlxdm;
	private String dqrq;
	private String dqrqflg;
	private String sfjh;
	private String[] sfjhs;
	private String[] htzts;
	private String ht_zt;

	public String getHt_zt() {
		return ht_zt;
	}

	public void setHt_zt(String ht_zt) {
		this.ht_zt = ht_zt;
	}

	public String[] getSfjhs() {
		return sfjhs;
	}

	public void setSfjhs(String[] sfjhs) {
		this.sfjhs = sfjhs;
	}

	public String[] getHtzts() {
		return htzts;
	}

	public void setHtzts(String[] htzts) {
		this.htzts = htzts;
	}

	public String getSfjh() {
		return sfjh;
	}

	public void setSfjh(String sfjh) {
		this.sfjh = sfjh;
	}

	public String getDqrq() {
		return dqrq;
	}

	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}

	public String getDqrqflg() {
		return dqrqflg;
	}

	public void setDqrqflg(String dqrqflg) {
		this.dqrqflg = dqrqflg;
	}

	public String getHtlxdm() {
		return htlxdm;
	}

	public void setHtlxdm(String htlxdm) {
		this.htlxdm = htlxdm;
	}

	public String getYxhtlxdm() {
		return yxhtlxdm;
	}

	public void setYxhtlxdm(String yxhtlxdm) {
		this.yxhtlxdm = yxhtlxdm;
	}

	public String[] getYxhtlxs() {
		return yxhtlxs;
	}

	public void setYxhtlxs(String[] yxhtlxs) {
		this.yxhtlxs = yxhtlxs;
	}

	public String getYxhtlxmc() {
		return yxhtlxmc;
	}

	public void setYxhtlxmc(String yxhtlxmc) {
		this.yxhtlxmc = yxhtlxmc;
	}

	public String getHtjbrmc() {
		return htjbrmc;
	}

	public void setHtjbrmc(String htjbrmc) {
		this.htjbrmc = htjbrmc;
	}

	public String getHtjbbmmc() {
		return htjbbmmc;
	}

	public void setHtjbbmmc(String htjbbmmc) {
		this.htjbbmmc = htjbbmmc;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getZsldddid() {
		return zsldddid;
	}

	public void setZsldddid(String zsldddid) {
		this.zsldddid = zsldddid;
	}

	public List<String> getZslds() {
		return zslds;
	}

	public void setZslds(List<String> zslds) {
		this.zslds = zslds;
	}

	public String getYwyddid() {
		return ywyddid;
	}

	public void setYwyddid(String ywyddid) {
		this.ywyddid = ywyddid;
	}

	public String getZsldmc() {
		return zsldmc;
	}

	public void setZsldmc(String zsldmc) {
		this.zsldmc = zsldmc;
	}

	public String getHtrqstart() {
		return htrqstart;
	}

	public void setHtrqstart(String htrqstart) {
		this.htrqstart = htrqstart;
	}

	public String getHtrqend() {
		return htrqend;
	}

	public void setHtrqend(String htrqend) {
		this.htrqend = htrqend;
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

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getHtjemin() {
		return htjemin;
	}

	public void setHtjemin(String htjemin) {
		this.htjemin = htjemin;
	}

	public String getHtjemax() {
		return htjemax;
	}

	public void setHtjemax(String htjemax) {
		this.htjemax = htjemax;
	}

	public String getYzlxmc() {
		return yzlxmc;
	}

	public void setYzlxmc(String yzlxmc) {
		this.yzlxmc = yzlxmc;
	}

	public String getKhlxmc() {
		return khlxmc;
	}

	public void setKhlxmc(String khlxmc) {
		this.khlxmc = khlxmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	public String[] getHtlxs() {
		return htlxs;
	}

	public void setHtlxs(String[] htlxs) {
		this.htlxs = htlxs;
	}

	public String[] getKhlxs() {
		return khlxs;
	}

	public void setKhlxs(String[] khlxs) {
		this.khlxs = khlxs;
	}

	public String[] getHtqxs() {
		return htqxs;
	}

	public void setHtqxs(String[] htqxs) {
		this.htqxs = htqxs;
	}

	public String[] getSzbjs() {
		return szbjs;
	}

	public void setSzbjs(String[] szbjs) {
		this.szbjs = szbjs;
	}

	public String[] getXslxs() {
		return xslxs;
	}

	public void setXslxs(String[] xslxs) {
		this.xslxs = xslxs;
	}

	public String[] getHtfxcds() {
		return htfxcds;
	}

	public void setHtfxcds(String[] htfxcds) {
		this.htfxcds = htfxcds;
	}

	public String[] getHtqdzts() {
		return htqdzts;
	}

	public void setHtqdzts(String[] htqdzts) {
		this.htqdzts = htqdzts;
	}

	public String[] getYzlbs() {
		return yzlbs;
	}

	public void setYzlbs(String[] yzlbs) {
		this.yzlbs = yzlbs;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getOaxsdh() {
		return oaxsdh;
	}

	public void setOaxsdh(String oaxsdh) {
		this.oaxsdh = oaxsdh;
	}

	public String getU8xsdh() {
		return u8xsdh;
	}

	public void setU8xsdh(String u8xsdh) {
		this.u8xsdh = u8xsdh;
	}

	public String getLldh() {
		return lldh;
	}

	public void setLldh(String lldh) {
		this.lldh = lldh;
	}

	public String getU8lldh() {
		return u8lldh;
	}

	public void setU8lldh(String u8lldh) {
		this.u8lldh = u8lldh;
	}

	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}

	public String getU8jydh() {
		return u8jydh;
	}

	public void setU8jydh(String u8jydh) {
		this.u8jydh = u8jydh;
	}

	public String getGhdh() {
		return ghdh;
	}

	public void setGhdh(String ghdh) {
		this.ghdh = ghdh;
	}

	public String getU8ghdh() {
		return u8ghdh;
	}

	public void setU8ghdh(String u8ghdh) {
		this.u8ghdh = u8ghdh;
	}

	public String getSsywymc() {
		return ssywymc;
	}

	public void setSsywymc(String ssywymc) {
		this.ssywymc = ssywymc;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getHtlxmc() {
		return htlxmc;
	}

	public void setHtlxmc(String htlxmc) {
		this.htlxmc = htlxmc;
	}

	public String getHkfsmc() {
		return hkfsmc;
	}

	public void setHkfsmc(String hkfsmc) {
		this.hkfsmc = hkfsmc;
	}

	public String getHtfxcdmc() {
		return htfxcdmc;
	}

	public void setHtfxcdmc(String htfxcdmc) {
		this.htfxcdmc = htfxcdmc;
	}

	public String getXslbmc() {
		return xslbmc;
	}

	public void setXslbmc(String xslbmc) {
		this.xslbmc = xslbmc;
	}

	public String getYzlbmc() {
		return yzlbmc;
	}

	public void setYzlbmc(String yzlbmc) {
		this.yzlbmc = yzlbmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
