package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="LbcxszDto")
public class LbcxszDto extends LbcxszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//资源ID
	private String zyid;
	//父节点
	private String fjd;
	//资源标题
	private String zybt;
	//标题缩写
	private String btsx;
	//资源路径
	private String zylj;
	//图标路径
	private String tblj;
	//显示顺序
	private String xssx;
	//菜单层次
	private String cdcc;
	//资源标记  00:审核标记
	private String zybj;
	//显示扩展标记  现用于手机端菜单是否显示  01：为显示
	private String xskzbj;
	//功能名 主要用于统一功能名称 ，当改变功能时只需更改一个地方
	private String gnm;
	

	//序号 页面的顺序
	private String xh_tj;
	//标题名  页面的标题
	private String btm;
	//显示类型 列表，文本   TEXT
	private String xslx;
	//输入ID
	private String srid;
	//输入名
	private String srm;
	//限制范围  数字限制，日期限制 
	private String xzfw;
	//限制内容 ，如果下拉内容，可以以逗号隔开
	private String xznr;
	//关联基础类别
	private String gljclb;
	//高级筛选标记  0：普通 1：高级
	private String gjsxbj;
	//匹配类型  精确查询,模糊查询,IN查询
	private String pplx;
	//扩展参数1
	private String kzcs1;
	//扩展参数2
	private String kzcs2;
	//扩展参数3
	private String kzcs3;
	//关联字段 用于确认页面的字段在哪个字段查询
	private String glzd_tj;
	
	
	//显示字段
	private String xszd;
	//业务名称
	private String ywmc;
	//显示字段名称
	private String xszdmc;
	//排序字段
	private String pxzd;
	//显示顺序
	private String xsxx;
	//SQL字段
	private String sqlzd;
	//显示宽度 用于设置列表里的显示宽度
	private String xskd;
	//显示格式 用于页面显示处理
	private String xsgs;
	//字段说明
	private String zdsm;
	//默认显示  1:为显示  0:隐藏  2：不显示  9：主键
	private String mrxs;
	
	//操作代码zyczb
	private String czdm;
	//对应页面
	private String dyym;
	//操作代码名称
	private String czmc;
	//操作代码czdm
	private String czdm_cz;
	
	//资源id集合
	private List<String> zyids;
	//操作代码集合
	private List<String> czdms;
	
	//修改前资源ID
	private String prezyid;
	//修改前业务ID
	private String preywid;
	
	//资源ID
	public String getZyid() {
		return zyid;
	}
	public void setZyid(String zyid){
		this.zyid = zyid;
	}
	//父节点
	public String getFjd() {
		return fjd;
	}
	public void setFjd(String fjd){
		this.fjd = fjd;
	}
	//资源标题
	public String getZybt() {
		return zybt;
	}
	public void setZybt(String zybt){
		this.zybt = zybt;
	}
	//资源路径
	public String getZylj() {
		return zylj;
	}
	public void setZylj(String zylj){
		this.zylj = zylj;
	}
	//图标路径
	public String getTblj() {
		return tblj;
	}
	public void setTblj(String tblj){
		this.tblj = tblj;
	}
	//显示顺序
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx){
		this.xssx = xssx;
	}
	//菜单层次
	public String getCdcc() {
		return cdcc;
	}
	public void setCdcc(String cdcc){
		this.cdcc = cdcc;
	}
	//资源标记  00:审核标记
	public String getZybj() {
		return zybj;
	}
	public void setZybj(String zybj){
		this.zybj = zybj;
	}
	//显示扩展标记  现用于手机端菜单是否显示  01：为显示
	public String getXskzbj() {
		return xskzbj;
	}
	public void setXskzbj(String xskzbj){
		this.xskzbj = xskzbj;
	}
	//功能名 主要用于统一功能名称 ，当改变功能时只需更改一个地方
	public String getGnm() {
		return gnm;
	}
	public void setGnm(String gnm){
		this.gnm = gnm;
	}
	//标题缩写
	public String getBtsx() {
		return btsx;
	}
	public void setBtsx(String btsx) {
		this.btsx = btsx;
	}
	
	public String getXh_tj() {
		return xh_tj;
	}
	public void setXh_tj(String xh_tj) {
		this.xh_tj = xh_tj;
	}
	public String getBtm() {
		return btm;
	}
	public void setBtm(String btm) {
		this.btm = btm;
	}
	public String getXslx() {
		return xslx;
	}
	public void setXslx(String xslx) {
		this.xslx = xslx;
	}
	public String getSrid() {
		return srid;
	}
	public void setSrid(String srid) {
		this.srid = srid;
	}
	public String getSrm() {
		return srm;
	}
	public void setSrm(String srm) {
		this.srm = srm;
	}
	public String getXzfw() {
		return xzfw;
	}
	public void setXzfw(String xzfw) {
		this.xzfw = xzfw;
	}
	public String getXznr() {
		return xznr;
	}
	public void setXznr(String xznr) {
		this.xznr = xznr;
	}
	public String getGljclb() {
		return gljclb;
	}
	public void setGljclb(String gljclb) {
		this.gljclb = gljclb;
	}
	public String getGjsxbj() {
		return gjsxbj;
	}
	public void setGjsxbj(String gjsxbj) {
		this.gjsxbj = gjsxbj;
	}
	public String getPplx() {
		return pplx;
	}
	public void setPplx(String pplx) {
		this.pplx = pplx;
	}
	public String getKzcs1() {
		return kzcs1;
	}
	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}
	public String getKzcs2() {
		return kzcs2;
	}
	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}
	public String getKzcs3() {
		return kzcs3;
	}
	public void setKzcs3(String kzcs3) {
		this.kzcs3 = kzcs3;
	}
	public String getGlzd_tj() {
		return glzd_tj;
	}
	public void setGlzd_tj(String glzd_tj) {
		this.glzd_tj = glzd_tj;
	}
	public String getXszd() {
		return xszd;
	}
	public void setXszd(String xszd) {
		this.xszd = xszd;
	}
	public String getYwmc() {
		return ywmc;
	}
	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}
	public String getXszdmc() {
		return xszdmc;
	}
	public void setXszdmc(String xszdmc) {
		this.xszdmc = xszdmc;
	}
	public String getPxzd() {
		return pxzd;
	}
	public void setPxzd(String pxzd) {
		this.pxzd = pxzd;
	}
	public String getXsxx() {
		return xsxx;
	}
	public void setXsxx(String xsxx) {
		this.xsxx = xsxx;
	}
	public String getSqlzd() {
		return sqlzd;
	}
	public void setSqlzd(String sqlzd) {
		this.sqlzd = sqlzd;
	}
	public String getXskd() {
		return xskd;
	}
	public void setXskd(String xskd) {
		this.xskd = xskd;
	}
	public String getXsgs() {
		return xsgs;
	}
	public void setXsgs(String xsgs) {
		this.xsgs = xsgs;
	}
	public String getZdsm() {
		return zdsm;
	}
	public void setZdsm(String zdsm) {
		this.zdsm = zdsm;
	}
	public String getMrxs() {
		return mrxs;
	}
	public void setMrxs(String mrxs) {
		this.mrxs = mrxs;
	}
	public String getCzdm() {
		return czdm;
	}
	public void setCzdm(String czdm) {
		this.czdm = czdm;
	}
	public String getDyym() {
		return dyym;
	}
	public void setDyym(String dyym) {
		this.dyym = dyym;
	}
	public String getPrezyid() {
		return prezyid;
	}
	public void setPrezyid(String prezyid) {
		this.prezyid = prezyid;
	}
	public List<String> getZyids() {
		return zyids;
	}
	public void setZyids(List<String> zyids) {
		this.zyids = zyids;
	}
	public List<String> getCzdms() {
		return czdms;
	}
	public void setCzdms(List<String> czdms) {
		this.czdms = czdms;
	}
	public String getCzmc() {
		return czmc;
	}
	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}
	public String getCzdm_cz() {
		return czdm_cz;
	}
	public void setCzdm_cz(String czdm_cz) {
		this.czdm_cz = czdm_cz;
	}
	public String getPreywid() {
		return preywid;
	}
	public void setPreywid(String preywid) {
		this.preywid = preywid;
	}
	
}
