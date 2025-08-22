package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjqqjcModel")
public class SjqqjcModel extends BaseModel{
	//送检ID
	private String sjid;
	//已检项目 关联基础数据
	private String yjxm;
	//检测值
	private String jcz;
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//已检项目 关联基础数据
	public String getYjxm() {
		return yjxm;
	}
	public void setYjxm(String yjxm){
		this.yjxm = yjxm;
	}
	//检测值
	public String getJcz() {
		return jcz;
	}
	public void setJcz(String jcz){
		this.jcz = jcz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
