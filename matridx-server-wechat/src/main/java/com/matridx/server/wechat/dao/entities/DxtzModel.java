package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DxtzModel")
public class DxtzModel extends BaseModel{
	//手机号
	private String sjh;
	//姓名
	private String xm;
	//手机号
	public String getSjh() {
		return sjh;
	}
	public void setSjh(String sjh){
		this.sjh = sjh;
	}
	//姓名
	public String getXm() {
		return xm;
	}
	public void setXm(String xm){
		this.xm = xm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
