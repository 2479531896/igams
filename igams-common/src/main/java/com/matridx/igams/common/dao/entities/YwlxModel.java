package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YwlxModel")
public class YwlxModel extends BaseModel{
	//联系ID
	private String lxid;
	//业务类型   如:送检信息: SJXX
	private String ywlx;
	//业务ID
	private String ywid;
	//接收服务器   发送给微信服务器   发送给电脑
	private String jsfwq;
	//处理标记  0:未处理  1:已处理
	private String clbj;
	//处理时间
	private String clsj;
	//联系ID
	public String getLxid() {
		return lxid;
	}
	public void setLxid(String lxid){
		this.lxid = lxid;
	}
	//业务类型   如:送检信息: SJXX
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx){
		this.ywlx = ywlx;
	}
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//接收服务器   发送给微信服务器   发送给电脑
	public String getJsfwq() {
		return jsfwq;
	}
	public void setJsfwq(String jsfwq){
		this.jsfwq = jsfwq;
	}
	//处理标记  0:未处理  1:已处理
	public String getClbj() {
		return clbj;
	}
	public void setClbj(String clbj){
		this.clbj = clbj;
	}
	//处理时间
	public String getClsj() {
		return clsj;
	}
	public void setClsj(String clsj){
		this.clsj = clsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
