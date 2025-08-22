package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WbzyczModel")
public class WbzyczModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//资源操作id
	private String zyczid;
	//资源id
	private String zyid; 
	//操作名称
	private String czmc; 
	//操作方法
	private String czff; 
	//跳转地址
	private String tzdz; 
	//显示顺序
	private String xssx;
	//默认操作  0：非默认  1：默认(选择行时默认执行该方法)';
	private String mrcz;
	public String getZyczid() {
		return zyczid;
	}
	public void setZyczid(String zyczid) {
		this.zyczid = zyczid;
	}
	public String getZyid() {
		return zyid;
	}
	public void setZyid(String zyid) {
		this.zyid = zyid;
	}
	public String getCzmc() {
		return czmc;
	}
	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}
	public String getCzff() {
		return czff;
	}
	public void setCzff(String czff) {
		this.czff = czff;
	}
	public String getTzdz() {
		return tzdz;
	}
	public void setTzdz(String tzdz) {
		this.tzdz = tzdz;
	}
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx) {
		this.xssx = xssx;
	}
	public String getMrcz() {
		return mrcz;
	}
	public void setMrcz(String mrcz) {
		this.mrcz = mrcz;
	} 
	
	
}
