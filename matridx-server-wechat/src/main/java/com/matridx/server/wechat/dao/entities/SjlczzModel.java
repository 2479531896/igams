package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjlczzModel")
public class SjlczzModel extends BaseModel{
	//送检ID
	private String sjid;
	//序号
	private String xh;
	//症状  关联基础数据
	private String zz;
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//症状  关联基础数据
	public String getZz() {
		return zz;
	}
	public void setZz(String zz){
		this.zz = zz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
