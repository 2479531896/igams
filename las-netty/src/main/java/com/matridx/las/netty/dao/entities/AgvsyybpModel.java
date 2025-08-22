package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * 托盘卡盒属性
 */
@Alias(value = "AgvsyybpModel")
public class AgvsyybpModel  {
	// nbbh编号
	private String nbbh;
	// 卡盒位置编号
	private String khbh;
	// 卡盒所在托盘编号
	private String tpbh;

	//试剂样品id
	private String ysybid;
	//提纯试剂编号号
	private String tcsjph;
	//建库试剂编号
	private String jkysjph;
	//制备法
	private String zbf;
	//街头号
	private String jth;

	//样本状态  0:待放置   1:已放置，待运行
	private String ybzt;
	public String getYsybid() {
		return ysybid;
	}


	public void setYsybid(String ysybid) {
		this.ysybid = ysybid;
	}


	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getKhbh() {
		return khbh;
	}

	public void setKhbh(String khbh) {
		this.khbh = khbh;
	}

	public String getTpbh() {
		return tpbh;
	}

	public void setTpbh(String tpbh) {
		this.tpbh = tpbh;
	}



	public String getYbzt() {
		return ybzt;
	}

	public void setYbzt(String ybzt) {
		this.ybzt = ybzt;
	}

	public String getTcsjph() {
		return tcsjph;
	}

	public void setTcsjph(String tcsjph) {
		this.tcsjph = tcsjph;
	}

	public String getJkysjph() {
		return jkysjph;
	}

	public void setJkysjph(String jkysjph) {
		this.jkysjph = jkysjph;
	}

	public String getZbf() {
		return zbf;
	}

	public void setZbf(String zbf) {
		this.zbf = zbf;
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
