package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="GrdcszModel")
public class GrdcszModel extends BaseModel{
	//用户id
	private String yhid;
	//业务ID
	private String ywid;
	//导出子段
	private String dczd;
	//导出子段名称
	private String dczdmc;
	//显示顺序
	private String xsxx;
	//用户id
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
	//导出子段
	public String getDczd() {
		return dczd;
	}
	public void setDczd(String dczd){
		this.dczd = dczd;
	}
	//导出子段名称
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
