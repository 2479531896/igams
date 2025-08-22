package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HzxxModel")
public class HzxxModel extends BaseModel{

	//患者ID
	private String hzid;
	//证件号
	private String zjh;
	//手机
	private String sj;
	//姓名
	private String xm;
	//性别
	private String xb;
	//年龄
	private String nl;
	//体温
	private String tw;
	//体温
	private String twjcz;
	//是否确认
	private String sfqr;
	//证件类型 1-中国居民身份证，2-护照，50-外国人永久居住证证，54-台湾居住证，55-港澳居住证
	private String zjlx;
	//详细地址
	private String xxdz;
	//省份
	private String sf;
	//城市
	private String cs;
	//区域
	private String qy;

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getQy() {
		return qy;
	}

	public void setQy(String qy) {
		this.qy = qy;
	}

	public String getXxdz() {
		return xxdz;
	}

	public void setXxdz(String xxdz) {
		this.xxdz = xxdz;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getTw() {
		return tw;
	}

	public void setTw(String tw) {
		this.tw = tw;
	}

	public String getTwjcz() {
		return twjcz;
	}

	public void setTwjcz(String twjcz) {
		this.twjcz = twjcz;
	}

	public String getHzid() {
		return hzid;
	}

	public void setHzid(String hzid) {
		this.hzid = hzid;
	}

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
