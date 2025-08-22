package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.Model;

@Alias(value="LbzdszModel")
public class LbzdszModel extends Model{
	//业务ID
	private String ywid;
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
	//加载路径
	private String jz;
	//组件
	private String zj;
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//显示字段
	public String getXszd() {
		return xszd;
	}
	public void setXszd(String xszd){
		this.xszd = xszd;
	}
	//业务名称
	public String getYwmc() {
		return ywmc;
	}
	public void setYwmc(String ywmc){
		this.ywmc = ywmc;
	}
	//显示字段名称
	public String getXszdmc() {
		return xszdmc;
	}
	public void setXszdmc(String xszdmc){
		this.xszdmc = xszdmc;
	}
	//排序字段
	public String getPxzd() {
		return pxzd;
	}
	public void setPxzd(String pxzd){
		this.pxzd = pxzd;
	}
	//显示顺序
	public String getXsxx() {
		return xsxx;
	}
	public void setXsxx(String xsxx){
		this.xsxx = xsxx;
	}
	//SQL字段
	public String getSqlzd() {
		return sqlzd;
	}
	public void setSqlzd(String sqlzd){
		this.sqlzd = sqlzd;
	}
	//显示宽度 用于设置列表里的显示宽度
	public String getXskd() {
		return xskd;
	}
	public void setXskd(String xskd){
		this.xskd = xskd;
	}
	//显示格式 用于页面显示处理
	public String getXsgs() {
		return xsgs;
	}
	public void setXsgs(String xsgs){
		this.xsgs = xsgs;
	}
	//字段说明
	public String getZdsm() {
		return zdsm;
	}
	public void setZdsm(String zdsm){
		this.zdsm = zdsm;
	}
	//默认显示  1:为显示  0:隐藏  2：不显示  9：主键
	public String getMrxs() {
		return mrxs;
	}
	public void setMrxs(String mrxs){
		this.mrxs = mrxs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	public String getZj() {
		return zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}
}
