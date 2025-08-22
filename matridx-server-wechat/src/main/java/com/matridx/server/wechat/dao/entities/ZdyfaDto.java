package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ZdyfaDto")
public class ZdyfaDto extends ZdyfaModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//是否应用名称
	private String sfyymc;
	//自定义设置list
	private String list;
	//方案图片路径
	private String faimglj;
	//方案图片id
	private String fjid;
	//小程序下载图片路径
	private String xzimglj;
	//自定义ID
	private String zdyid;
	//字段名称
	private String zdmc;
	//包含标记
	private String bhbj;
	//左标记
	private String zbj;
	//左间隔
	private String zjg;
	//右标记
	private String ybj;
	//右间隔
	private String yjg;
	//上标记
	private String sbj;
	//上间隔
	private String sjg;
	//下标记
	private String xbj;
	//下间隔
	private String xjg;
	//易位标记
	private String ywbj;
	//字段信息
	private String zdxx;

	public String getZdxx() {
		return zdxx;
	}
	public void setZdxx(String zdxx) {
		this.zdxx = zdxx;
	}
	public String getYwbj() {
		return ywbj;
	}
	public void setYwbj(String ywbj) {
		this.ywbj = ywbj;
	}
	public String getSfyymc() {
		return sfyymc;
	}
	public void setSfyymc(String sfyymc) {
		this.sfyymc = sfyymc;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getFaimglj() {
		return faimglj;
	}
	public void setFaimglj(String faimglj) {
		this.faimglj = faimglj;
	}
	public String getFjid() {
		return fjid;
	}
	public void setFjid(String fjid) {
		this.fjid = fjid;
	}
	public String getXzimglj() {
		return xzimglj;
	}
	public void setXzimglj(String xzimglj) {
		this.xzimglj = xzimglj;
	}
	public String getZdyid() {
		return zdyid;
	}
	public void setZdyid(String zdyid) {
		this.zdyid = zdyid;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public String getBhbj() {
		return bhbj;
	}
	public void setBhbj(String bhbj) {
		this.bhbj = bhbj;
	}
	public String getZbj() {
		return zbj;
	}
	public void setZbj(String zbj) {
		this.zbj = zbj;
	}
	public String getZjg() {
		return zjg;
	}
	public void setZjg(String zjg) {
		this.zjg = zjg;
	}
	public String getYbj() {
		return ybj;
	}
	public void setYbj(String ybj) {
		this.ybj = ybj;
	}
	public String getYjg() {
		return yjg;
	}
	public void setYjg(String yjg) {
		this.yjg = yjg;
	}
	public String getSbj() {
		return sbj;
	}
	public void setSbj(String sbj) {
		this.sbj = sbj;
	}
	public String getSjg() {
		return sjg;
	}
	public void setSjg(String sjg) {
		this.sjg = sjg;
	}
	public String getXbj() {
		return xbj;
	}
	public void setXbj(String xbj) {
		this.xbj = xbj;
	}
	public String getXjg() {
		return xjg;
	}
	public void setXjg(String xjg) {
		this.xjg = xjg;
	}

}
