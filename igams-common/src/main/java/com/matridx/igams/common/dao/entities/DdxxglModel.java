package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DdxxglModel")
public class DdxxglModel extends BaseModel{
	//钉钉消息id
	private String ddxxid;
	//用户ID
	private String yhid;
	//钉钉消息类型
	private String ddxxlx;
	//微信id
	private String wechatid;
	//小程序跳转路径
	private String cskz1;
	//消息ids
	private String cskz2;

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	//用户ID
	public String getYhid() {
		return yhid;
	}
	public String getWechatid() {
		return wechatid;
	}
	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//钉钉消息类型
	public String getDdxxlx() {
		return ddxxlx;
	}
	public void setDdxxlx(String ddxxlx){
		this.ddxxlx = ddxxlx;
	}
	public String getDdxxid()
	{
		return ddxxid;
	}
	public void setDdxxid(String ddxxid)
	{
		this.ddxxid = ddxxid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
