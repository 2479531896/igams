package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * 样本信息，Ep载具上的
 */
@Alias(value = "AgvEpModel")
public class AgvEpModel {
	// nbbh编号
	private String nbbh;
	//试剂样品id
	private String ysybid;
	//x坐标
	private String xzb;
	//y坐标
	private String yzb;
	//是否为空Ep管
	private boolean isBlank;
	//接头号
	private String jth;

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getYsybid() {
		return ysybid;
	}

	public void setYsybid(String ysybid) {
		this.ysybid = ysybid;
	}

	public String getXzb() {
		return xzb;
	}

	public void setXzb(String xzb) {
		this.xzb = xzb;
	}

	public String getYzb() {
		return yzb;
	}

	public void setYzb(String yzb) {
		this.yzb = yzb;
	}

	public boolean isBlank() {
		return isBlank;
	}

	public void setBlank(boolean blank) {
		isBlank = blank;
	}

	public String getJth() {
		return jth;
	}

	public void setJth(String jth) {
		this.jth = jth;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
