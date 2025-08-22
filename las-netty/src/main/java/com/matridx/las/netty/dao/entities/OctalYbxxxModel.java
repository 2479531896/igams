package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * 八连管中样本对象
 */
@Alias(value = "OctalYbxxxModel")
public class OctalYbxxxModel {
	// nbbh编号
	private String nbbh;
	// 样本的在八连管中的X坐标
	private String blgxzb;
	// 样本的在八连管中的Y坐标
	private String blgyzb;
	// 样本类型，1混合样本，0，单个样本
	private String yblx;
	//试剂样品id
	private String sjypid;
	//八连管编号
	private String bh;

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getBlgxzb() {
		return blgxzb;
	}

	public void setBlgxzb(String blgxzb) {
		this.blgxzb = blgxzb;
	}

	public String getBlgyzb() {
		return blgyzb;
	}

	public void setBlgyzb(String blgyzb) {
		this.blgyzb = blgyzb;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getSjypid() {
		return sjypid;
	}

	public void setSjypid(String sjypid) {
		this.sjypid = sjypid;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
