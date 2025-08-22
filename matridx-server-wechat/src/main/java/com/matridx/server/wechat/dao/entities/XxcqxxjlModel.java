package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XxcqxxjlModel")
public class XxcqxxjlModel extends BaseModel{
	//记录ID
	private String jlid;
	//微信ID
	private String wxid;
	//序号
	private String xh;
	//患者ID
	private String hzid;
	//实际信息
	private String sjxx;
	//抽取信息
	private String cqxx;
	//原始信息
	private String ysxx;
	//是否正确  0:否 1：是
	private String sfzq;
	//记录ID
	public String getJlid() {
		return jlid;
	}
	public void setJlid(String jlid){
		this.jlid = jlid;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//患者ID
	public String getHzid() {
		return hzid;
	}
	public void setHzid(String hzid){
		this.hzid = hzid;
	}
	//实际信息
	public String getSjxx() {
		return sjxx;
	}
	public void setSjxx(String sjxx){
		this.sjxx = sjxx;
	}
	//抽取信息
	public String getCqxx() {
		return cqxx;
	}
	public void setCqxx(String cqxx){
		this.cqxx = cqxx;
	}
	//原始信息
	public String getYsxx() {
		return ysxx;
	}
	public void setYsxx(String ysxx){
		this.ysxx = ysxx;
	}
	//是否正确  0:否 1：是
	public String getSfzq() {
		return sfzq;
	}
	public void setSfzq(String sfzq){
		this.sfzq = sfzq;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
