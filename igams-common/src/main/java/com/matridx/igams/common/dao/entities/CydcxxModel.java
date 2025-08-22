package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="CydcxxModel")
public class CydcxxModel extends BaseModel{
	//常用ID
	private String cyid;
	//导出字段
	private String dczd;
	//导出字段名称
	private String dczdmc;
	//显示顺序
	private String xsxx;
	//常用ID
	public String getCyid() {
		return cyid;
	}
	public void setCyid(String cyid){
		this.cyid = cyid;
	}
	//导出字段
	public String getDczd() {
		return dczd;
	}
	public void setDczd(String dczd){
		this.dczd = dczd;
	}
	//导出字段名称
	public String getDczdmc() {
		return dczdmc;
	}
	public void setDczdmc(String dczdmc){
		this.dczdmc = dczdmc;
	}
	//显示顺序
	public String getXsxx() {
		return xsxx;
	}
	public void setXsxx(String xsxx){
		this.xsxx = xsxx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
