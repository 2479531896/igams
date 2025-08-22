package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HzxxModel")
public class HzxxModel extends BaseModel{
	//送检ID
	private String hzid;
	//微信ID
	private String wxid;
	//患者姓名
	private String xm;
	//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String xb;
	//年龄
	private String nl;
	//手机
	private String sj;
	//体温
	private String tw;
	//体温检测值
	private String twjcz;

	//证件号
	private String zjh;
	//现居住地
	private String xjzd;
	//是否确认
	private String sfqr;
	//证件类型
	private String zjlx;

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

	//送检ID
	public String getHzid() {
		return hzid;
	}
	public void setHzid(String hzid){
		this.hzid = hzid;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//患者姓名
	public String getXm() {
		return xm;
	}
	public void setXm(String xm){
		this.xm = xm;
	}
	//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String getXb() {
		return xb;
	}
	public void setXb(String xb){
		this.xb = xb;
	}
	//年龄
	public String getNl() {
		return nl;
	}
	public void setNl(String nl){
		this.nl = nl;
	}
	//手机
	public String getSj() {
		return sj;
	}
	public void setSj(String sj){
		this.sj = sj;
	}
	//体温
	public String getTw() {
		return tw;
	}
	public void setTw(String tw){
		this.tw = tw;
	}
	//体温检测值
	public String getTwjcz() {
		return twjcz;
	}
	public void setTwjcz(String twjcz){
		this.twjcz = twjcz;
	}

	public String getXjzd() {
		return xjzd;
	}

	public void setXjzd(String xjzd) {
		this.xjzd = xjzd;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
