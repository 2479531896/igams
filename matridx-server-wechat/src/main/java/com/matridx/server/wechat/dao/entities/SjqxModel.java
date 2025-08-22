package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjqxModel")
public class SjqxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//送检权限ID
	private String sjqxid;
	//微信ID 保存的是unionid
	private String wxid;
	//送检单位
	private String sjdw;
	//科室
	private String ks;
	
	public String getSjqxid() {
		return sjqxid;
	}
	public void setSjqxid(String sjqxid) {
		this.sjqxid = sjqxid;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getSjdw() {
		return sjdw;
	}
	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}
	public String getKs() {
		return ks;
	}
	public void setKs(String ks) {
		this.ks = ks;
	}
	
}
