package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WxyhModel")
public class WxyhModel extends BaseModel{
	//用户ID
	private String yhid;
	//unionid
	private String unionid;
	//微信ID
	private String wxid;
	//微信名
	private String wxm;
	//用户名
	private String yhm;
	//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String yhxb;
	//用户所在城市
	private String yhcs;
	//用户被打上的标签ID列表
	private String bqidlb;
	//用户所在平台地址
	private String gzpt;
	//手机
	private String sj;
	//身份类型
	private String sflx;	
	//扩展参数1
	private String kzcs1;
	//扩展参数2
	private String kzcs2;
	//扩展参数3
	private String kzcs3;
	//系统用户ID
	private String xtyhid;
	
	//系统用户ID
	public String getXtyhid() {
		return xtyhid;
	}
	public void setXtyhid(String xtyhid) {
		this.xtyhid = xtyhid;
	}
	//用户ID
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//unionid
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//微信名
	public String getWxm() {
		return wxm;
	}
	public void setWxm(String wxm){
		this.wxm = wxm;
	}
	//用户名
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm){
		this.yhm = yhm;
	}
	//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String getYhxb() {
		return yhxb;
	}
	public void setYhxb(String yhxb){
		this.yhxb = yhxb;
	}
	//用户所在城市
	public String getYhcs() {
		return yhcs;
	}
	public void setYhcs(String yhcs){
		this.yhcs = yhcs;
	}
	//用户被打上的标签ID列表
	public String getBqidlb() {
		return bqidlb;
	}
	public void setBqidlb(String bqidlb){
		this.bqidlb = bqidlb;
	}
	//用户所在平台地址
	public String getGzpt() {
		return gzpt;
	}
	public void setGzpt(String gzpt){
		this.gzpt = gzpt;
	}

	public String getKzcs1() {
		return kzcs1;
	}
	public void setKzcs1(String kzcs1) {
		this.kzcs1 = kzcs1;
	}
	public String getKzcs2() {
		return kzcs2;
	}
	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}
	public String getKzcs3() {
		return kzcs3;
	}
	public void setKzcs3(String kzcs3) {
		this.kzcs3 = kzcs3;
	}
	public String getSj() {
		return sj;
	}
	public void setSj(String sj) {
		this.sj = sj;
	}
	public String getSflx() {
		return sflx;
	}
	public void setSflx(String sflx) {
		this.sflx = sflx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
