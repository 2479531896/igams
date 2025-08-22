package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhbqModel")
public class YhbqModel extends BaseModel{
	//标签ID
	private String bqid;
	//微信ID
	private String wxid;
	//外部程序ID
	private String wbcxid;
	
	//外部程序ID
	public String getWbcxid() {
		return wbcxid;
	}
	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}
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
