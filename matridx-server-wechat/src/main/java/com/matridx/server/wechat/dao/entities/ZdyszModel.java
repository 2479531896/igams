package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZdyszModel")
public class ZdyszModel extends BaseModel{
	//设置ID
	private String zdyid;
	//微信ID
	private String wxid;
	//自定义姓名
	private String zdyxm;
	//自定义性别
	private String zdyxb;
	//自定义年龄
	private String zdynl;
	//自定义手机
	private String zdysj;
	//方案名称
	private String famc;
	//是否应用
	private String sfyy;
	
	public String getSfyy() {
		return sfyy;
	}
	public void setSfyy(String sfyy) {
		this.sfyy = sfyy;
	}
	public String getFamc() {
		return famc;
	}
	public void setFamc(String famc) {
		this.famc = famc;
	}
	//设置ID
	public String getZdyid() {
		return zdyid;
	}
	public void setZdyid(String zdyid){
		this.zdyid = zdyid;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//自定义姓名
	public String getZdyxm() {
		return zdyxm;
	}
	public void setZdyxm(String zdyxm){
		this.zdyxm = zdyxm;
	}
	//自定义性别
	public String getZdyxb() {
		return zdyxb;
	}
	public void setZdyxb(String zdyxb){
		this.zdyxb = zdyxb;
	}
	//自定义年龄
	public String getZdynl() {
		return zdynl;
	}
	public void setZdynl(String zdynl){
		this.zdynl = zdynl;
	}
	//自定义手机
	public String getZdysj() {
		return zdysj;
	}
	public void setZdysj(String zdysj){
		this.zdysj = zdysj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
