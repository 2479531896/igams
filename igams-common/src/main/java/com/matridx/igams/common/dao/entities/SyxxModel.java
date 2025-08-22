package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SyxxModel")
public class SyxxModel extends BaseModel{
	//水印信息ID
	private String syxxid;
	//文件类别
	private String wjlb;
	//开始页数
	private String ksys;
	//水印图片链接
	private String sytplj;
	//水印字
	private String syz;
	//水印字体大小
	private String syztdx;
	//水印横向间隔
	private String syhxjg;
	//水印纵向间隔
	private String syzxjg;
	//水印透明度
	private String sytmd;
	//水印倾斜角度
	private String syqxjd;
	//图片水印字体大小
	private String tpsyztdx;
	//页数显示
	private String ysxs;
	
	//水印信息ID
	public String getSyxxid() {
		return syxxid;
	}
	public void setSyxxid(String syxxid){
		this.syxxid = syxxid;
	}
	//文件类别
	public String getWjlb() {
		return wjlb;
	}
	public void setWjlb(String wjlb){
		this.wjlb = wjlb;
	}
	//开始页数
	public String getKsys() {
		return ksys;
	}
	public void setKsys(String ksys){
		this.ksys = ksys;
	}
	//水印图片链接
	public String getSytplj() {
		return sytplj;
	}
	public void setSytplj(String sytplj){
		this.sytplj = sytplj;
	}
	//水印字
	public String getSyz() {
		return syz;
	}
	public void setSyz(String syz){
		this.syz = syz;
	}
	//水印字体大小
	public String getSyztdx() {
		return syztdx;
	}
	public void setSyztdx(String syztdx){
		this.syztdx = syztdx;
	}
	//水印横向间隔
	public String getSyhxjg() {
		return syhxjg;
	}
	public void setSyhxjg(String syhxjg){
		this.syhxjg = syhxjg;
	}
	//水印纵向间隔
	public String getSyzxjg() {
		return syzxjg;
	}
	public void setSyzxjg(String syzxjg){
		this.syzxjg = syzxjg;
	}
	//水印透明度
	public String getSytmd() {
		return sytmd;
	}
	public void setSytmd(String sytmd){
		this.sytmd = sytmd;
	}
	//水印倾斜角度
	public String getSyqxjd() {
		return syqxjd;
	}
	public void setSyqxjd(String syqxjd){
		this.syqxjd = syqxjd;
	}
	//图片水印字体大小
	public String getTpsyztdx() {
		return tpsyztdx;
	}
	public void setTpsyztdx(String tpsyztdx){
		this.tpsyztdx = tpsyztdx;
	}

	//页数显示
	public String getYsxs() {
		return ysxs;
	}
	public void setYsxs(String ysxs) {
		this.ysxs = ysxs;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
