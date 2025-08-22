package com.matridx.server.wechat.dao.entities;

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
	//付款标记
	private String fkbj;
	//实付金额
	private String sfje;
	//付款日期
	private String fkrq;
	
	public String getFkbj() {
		return fkbj;
	}
	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}
	public String getFkrq() {
		return fkrq;
	}
	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}
	public String getSfje() {
		return sfje;
	}
	public void setSfje(String sfje) {
		this.sfje = sfje;
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
	
}
