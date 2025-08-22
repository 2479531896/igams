package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjyzmxModel")
public class SjyzmxModel extends BaseModel{
	//验证明细ID
	private String yzmxid;
	//验证ID
	private String yzid;
	//序号
	private String xh;
	//检测区分
	private String jcqf;
	//目标基因
	private String mbjy;
	//孔位
	private String kw;
	//CT
	private String  ct;
	//TM1
	private String tm1;
	//峰值1
	private String fz1;
	//TM2
	private String tm2;
	//峰值2
	private String fz2;
	//TM3
	private String tm3;
	//峰值3
	private String fz3;
	//状态
	private String zt;
	//编号
	private String bh;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getYzmxid() {
		return yzmxid;
	}

	public void setYzmxid(String yzmxid) {
		this.yzmxid = yzmxid;
	}

	public String getYzid() {
		return yzid;
	}

	public void setYzid(String yzid) {
		this.yzid = yzid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJcqf() {
		return jcqf;
	}

	public void setJcqf(String jcqf) {
		this.jcqf = jcqf;
	}

	public String getMbjy() {
		return mbjy;
	}

	public void setMbjy(String mbjy) {
		this.mbjy = mbjy;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getTm1() {
		return tm1;
	}

	public void setTm1(String tm1) {
		this.tm1 = tm1;
	}

	public String getFz1() {
		return fz1;
	}

	public void setFz1(String fz1) {
		this.fz1 = fz1;
	}

	public String getTm2() {
		return tm2;
	}

	public void setTm2(String tm2) {
		this.tm2 = tm2;
	}

	public String getFz2() {
		return fz2;
	}

	public void setFz2(String fz2) {
		this.fz2 = fz2;
	}

	public String getTm3() {
		return tm3;
	}

	public void setTm3(String tm3) {
		this.tm3 = tm3;
	}

	public String getFz3() {
		return fz3;
	}

	public void setFz3(String fz3) {
		this.fz3 = fz3;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}
	
	
}
