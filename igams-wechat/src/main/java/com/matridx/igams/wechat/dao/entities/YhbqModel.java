package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhbqModel")
public class YhbqModel extends BaseModel{
	//标签ID
	private String bqid;
	//微信ID
	private String wxid;
	//标签ID
	public String getBqid() {
		return bqid;
	}
	public void setBqid(String bqid){
		this.bqid = bqid;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
