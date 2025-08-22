package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="FjsqModel")
public class FjsqModel extends BaseBasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//复检id
	private String fjid;
	//状态
	private String zt;
	//原因
	private String yy;
	//类型
	private String lx;
	//检测项目
	private String jcxm;
	//备注
	private String bz;
	//送检id
	private String sjid;
	//报告标记
	private String bgbj;
	//审核类型
	private String shlx;
	//是否付费
	private String sfff;
	//检测单位
	private String jcdw;
	//付款标记
	private String fkbj;
	//实付金额
	private String sfje;
	//检测时间
	private String jcsj;
	//付款日期
	private String fkrq;
	//取样人
	private String qyr;
	//取样时间
	private String qysj;
	//DNA实验日期
	private String dsyrq;
	//RNA实验日期
	private String rsyrq;
	//RNA检测标记
	private String rjcbj;
	//DNA检测标记
	private String djcbj;
	//检测子项目
	private String jczxm;
	private String dzje; //对账金额
	private String yfje; //应付金额
	private String sfdz; //是否对账
	private String yszkid;//应收账款id
	private String dcbj; //导出标记

	private String sfgs;//是否改善

	private String bgrq;//报告日期

	private String wkbm;//文库编码

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getYszkid() {
		return yszkid;
	}

	public void setYszkid(String yszkid) {
		this.yszkid = yszkid;
	}

	public String getDcbj() {
		return dcbj;
	}

	public void setDcbj(String dcbj) {
		this.dcbj = dcbj;
	}

	public String getDzje() {
		return dzje;
	}

	public void setDzje(String dzje) {
		this.dzje = dzje;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getSfdz() {
		return sfdz;
	}

	public void setSfdz(String sfdz) {
		this.sfdz = sfdz;
	}

	public String getJczxm() {
		return jczxm;
	}

	public void setJczxm(String jczxm) {
		this.jczxm = jczxm;
	}

	public String getRjcbj() {
		return rjcbj;
	}

	public void setRjcbj(String rjcbj) {
		this.rjcbj = rjcbj;
	}

	public String getDjcbj() {
		return djcbj;
	}

	public void setDjcbj(String djcbj) {
		this.djcbj = djcbj;
	}

	public String getDsyrq() {
		return dsyrq;
	}

	public void setDsyrq(String dsyrq) {
		this.dsyrq = dsyrq;
	}

	public String getRsyrq() {
		return rsyrq;
	}

	public void setRsyrq(String rsyrq) {
		this.rsyrq = rsyrq;
	}

	public String getQysj() {
		return qysj;
	}

	public void setQysj(String qysj) {
		this.qysj = qysj;
	}

	public String getQyr() {
		return qyr;
	}

	public void setQyr(String qyr) {
		this.qyr = qyr;
	}

	public String getFkbj() {
		return fkbj;
	}
	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}

	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}

	public String getSfje() {
		return sfje;
	}
	public void setSfje(String sfje) {
		this.sfje = sfje;
	}
	public String getFkrq() {
		return fkrq;
	}
	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}
	public String getShlx() {
		return shlx;
	}
	public void setShlx(String shlx) {
		this.shlx = shlx;
	}
	public String getBgbj()
	{
		return bgbj;
	}
	public void setBgbj(String bgbj)
	{
		this.bgbj = bgbj;
	}
	public String getLx()
	{
		return lx;
	}
	public void setLx(String lx)
	{
		this.lx = lx;
	}
	public String getJcxm()
	{
		return jcxm;
	}
	public void setJcxm(String jcxm)
	{
		this.jcxm = jcxm;
	}
	public String getFjid()
	{
		return fjid;
	}
	public void setFjid(String fjid)
	{
		this.fjid = fjid;
	}
	public String getZt()
	{
		return zt;
	}
	public void setZt(String zt)
	{
		this.zt = zt;
	}
	public String getYy()
	{
		return yy;
	}
	public void setYy(String yy)
	{
		this.yy = yy;
	}
	public String getBz()
	{
		return bz;
	}
	public void setBz(String bz)
	{
		this.bz = bz;
	}
	public String getSjid()
	{
		return sjid;
	}
	public void setSjid(String sjid)
	{
		this.sjid = sjid;
	}
	public String getSfff() {
		return sfff;
	}
	public void setSfff(String sfff) {
		this.sfff = sfff;
	}
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getSfgs() {
		return sfgs;
	}

	public void setSfgs(String sfgs) {
		this.sfgs = sfgs;
	}
}
