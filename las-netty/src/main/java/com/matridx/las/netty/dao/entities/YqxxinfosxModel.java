package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YqxxinfosxModel")
public class YqxxinfosxModel extends BaseModel{
	//属性id
	private String sxid;
	//仪器信息id
	private String yqxxid;
	//属性名称
	private String sxmc;
	//属性类型
	private String sxlx;

	//属性id
	public String getSxid() {
		return sxid;
	}
	public void setSxid(String sxid){
		this.sxid = sxid;
	}
	//仪器信息id
	public String getYqxxid() {
		return yqxxid;
	}
	public void setYqxxid(String yqxxid){
		this.yqxxid = yqxxid;
	}
	//属性名称
	public String getSxmc() {
		return sxmc;
	}
	public void setSxmc(String sxmc){
		this.sxmc = sxmc;
	}
	//属性类型
	public String getSxlx() {
		return sxlx;
	}
	public void setSxlx(String sxlx){
		this.sxlx = sxlx;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
