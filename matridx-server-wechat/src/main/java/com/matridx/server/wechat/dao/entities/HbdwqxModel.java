package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HbdwqxModel")
public class HbdwqxModel extends BaseModel{
	//伙伴ID
	private String hbid;
	//序号
	private String xh;
	//检测单位
	private String jcdw;
	//伙伴ID
	public String getHbid() {
		return hbid;
	}
	public void setHbid(String hbid){
		this.hbid = hbid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//检测单位
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw){
		this.jcdw = jcdw;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
