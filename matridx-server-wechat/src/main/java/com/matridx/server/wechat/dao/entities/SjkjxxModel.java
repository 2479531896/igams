package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjkjxxModel")
public class SjkjxxModel extends BaseModel{
	//微信ID
	private String wxid;
	//送检单位
	private String sjdw;
	//科室
	private String ks;
	//其他科室
	private String qtks;
	//送检医生
	private String sjys;
	//医生电话
	private String ysdh;
	//代表名
	private String dbm;
	//检测单位
	private String jcdw;
	
	//其他科室
	public String getQtks() {
		return qtks;
	}
	public void setQtks(String qtks) {
		this.qtks = qtks;
	}
	//检测单位
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//送检单位
	public String getSjdw() {
		return sjdw;
	}
	public void setSjdw(String sjdw){
		this.sjdw = sjdw;
	}
	//科室
	public String getKs() {
		return ks;
	}
	public void setKs(String ks){
		this.ks = ks;
	}
	//送检医生
	public String getSjys() {
		return sjys;
	}
	public void setSjys(String sjys){
		this.sjys = sjys;
	}
	//医生电话
	public String getYsdh() {
		return ysdh;
	}
	public void setYsdh(String ysdh){
		this.ysdh = ysdh;
	}
	//代表名
	public String getDbm() {
		return dbm;
	}
	public void setDbm(String dbm){
		this.dbm = dbm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
