package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjysxxModel")
public class SjysxxModel extends BaseModel{
	//医生ID
	private String ysid;
	//送检单位
	private String sjdw;
	//科室
	private String ks;
	//送检医生
	private String sjys;
	//医生电话
	private String ysdh;
	//代表名
	private String dbm;
	//医生ID
	public String getYsid() {
		return ysid;
	}
	public void setYsid(String ysid){
		this.ysid = ysid;
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
