package com.matridx.server.wechat.dao.entities;

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
	//性别
	private String yhxb;
	//城市
	private String yhcs;
	//标签ID列表
	private String bqidlb;
	//关注平台
	private String gzpt;
	//手机
	private String sj;
	//身份类型
	private String sflx;	
	//扩展参数1
	private String cskz1;
	//扩展参数2
	private String cskz2;
	//扩展参数3
	private String cskz3;
		
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
	//性别
	public String getYhxb() {
		return yhxb;
	}
	public void setYhxb(String yhxb) {
		this.yhxb = yhxb;
	}
	//城市
	public String getYhcs() {
		return yhcs;
	}
	public void setYhcs(String yhcs) {
		this.yhcs = yhcs;
	}
	//标签ID列表
	public String getBqidlb() {
		return bqidlb;
	}
	public void setBqidlb(String bqidlb) {
		this.bqidlb = bqidlb;
	}
	//关注平台
	public String getGzpt() {
		return gzpt;
	}
	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
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


	public String getCskz1()
	{
		return cskz1;
	}
	public void setCskz1(String cskz1)
	{
		this.cskz1 = cskz1;
	}
	public String getCskz2()
	{
		return cskz2;
	}
	public void setCskz2(String cskz2)
	{
		this.cskz2 = cskz2;
	}
	public String getCskz3()
	{
		return cskz3;
	}
	public void setCskz3(String cskz3)
	{
		this.cskz3 = cskz3;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
