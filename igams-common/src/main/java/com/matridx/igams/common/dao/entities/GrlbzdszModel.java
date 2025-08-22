package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GrlbzdszModel")
public class GrlbzdszModel extends BaseModel{
	//用户ID
	private String yhid;
	//业务ID
	private String ywid;
	//显示字段
	private String xszd;
	//显示字段名称
	private String xszdmc;
	//显示顺序
	private String xssx;
	//显示宽度
	private String xskd;
	//用户ID
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
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
	//显示字段名称
	public String getXszdmc() {
		return xszdmc;
	}
	public void setXszdmc(String xszdmc){
		this.xszdmc = xszdmc;
	}
	//显示顺序
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx){
		this.xssx = xssx;
	}

	/**
	 * 显示宽度
	 */
	private static final long serialVersionUID = 1L;

	public String getXskd() {
		return xskd;
	}

	public void setXskd(String xskd) {
		this.xskd = xskd;
	}
}
