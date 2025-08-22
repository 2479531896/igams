package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZdybtwzModel")
public class ZdybtwzModel extends BaseModel{
	//设置ID
	private String zdyid;
	//方案ID
	private String faid;
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
	
	//设置ID
	public String getZdyid() {
		return zdyid;
	}
	public void setZdyid(String zdyid){
		this.zdyid = zdyid;
	}
	//方案ID
	public String getFaid() {
		return faid;
	}
	public void setFaid(String faid){
		this.faid = faid;
	}
	//字段名称
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc){
		this.zdmc = zdmc;
	}
	//包含标记
	public String getBhbj() {
		return bhbj;
	}
	public void setBhbj(String bhbj){
		this.bhbj = bhbj;
	}
	//左标记
	public String getZbj() {
		return zbj;
	}
	public void setZbj(String zbj){
		this.zbj = zbj;
	}
	//左间隔
	public String getZjg() {
		return zjg;
	}
	public void setZjg(String zjg){
		this.zjg = zjg;
	}
	//右标记
	public String getYbj() {
		return ybj;
	}
	public void setYbj(String ybj){
		this.ybj = ybj;
	}
	//右间隔
	public String getYjg() {
		return yjg;
	}
	public void setYjg(String yjg){
		this.yjg = yjg;
	}
	//上标记
	public String getSbj() {
		return sbj;
	}
	public void setSbj(String sbj){
		this.sbj = sbj;
	}
	//上间隔
	public String getSjg() {
		return sjg;
	}
	public void setSjg(String sjg){
		this.sjg = sjg;
	}
	//下标记
	public String getXbj() {
		return xbj;
	}
	public void setXbj(String xbj){
		this.xbj = xbj;
	}
	//下间隔
	public String getXjg() {
		return xjg;
	}
	public void setXjg(String xjg){
		this.xjg = xjg;
	}
	//易位标记
	public String getYwbj() {
		return ywbj;
	}
	public void setYwbj(String ywbj) {
		this.ywbj = ywbj;
	}
	//字段信息
	public String getZdxx() {
		return zdxx;
	}
	public void setZdxx(String zdxx) {
		this.zdxx = zdxx;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
