package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JkrzxxModel")
public class JkrzxxModel extends BaseModel{
	//认证ID
	private String rzid;
	//认证用户代码
	private String rzyhdm;
	//认证键
	private String rzkey;
	//认证用户名
	private String rzyhm;
	//备注
	private String bz;
	//认证ID
	public String getRzid() {
		return rzid;
	}
	public void setRzid(String rzid){
		this.rzid = rzid;
	}
	//认证用户代码
	public String getRzyhdm() {
		return rzyhdm;
	}
	public void setRzyhdm(String rzyhdm){
		this.rzyhdm = rzyhdm;
	}
	//认证键
	public String getRzkey() {
		return rzkey;
	}
	public void setRzkey(String rzkey){
		this.rzkey = rzkey;
	}
	//认证用户名
	public String getRzyhm() {
		return rzyhm;
	}
	public void setRzyhm(String rzyhm){
		this.rzyhm = rzyhm;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
