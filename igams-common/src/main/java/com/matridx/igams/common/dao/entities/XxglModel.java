package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XxglModel")
public class XxglModel extends BaseModel{
	//消息ID
	private String xxid;
	//消息类型
	private String xxlx;
	//消息内容
	private String xxnr;
	//钉钉群id
	private String ddqid;
	//消息ID
	public String getDdqid() {
		return ddqid;
	}

	public void setDdqid(String ddqid) {
		this.ddqid = ddqid;
	}
	public String getXxid() {
		return xxid;
	}
	public void setXxid(String xxid){
		this.xxid = xxid;
	}
	//消息类型
	public String getXxlx() {
		return xxlx;
	}
	public void setXxlx(String xxlx){
		this.xxlx = xxlx;
	}
	//消息内容
	public String getXxnr() {
		return xxnr;
	}
	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
