package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RzqxModel")
public class RzqxModel extends BaseModel{
	//权限ID
	private String qxid;
	//认证ID
	private String rzid;
	//资源ID
	private String zyid;
	//权限ID
	public String getQxid() {
		return qxid;
	}
	public void setQxid(String qxid){
		this.qxid = qxid;
	}
	//认证ID
	public String getRzid() {
		return rzid;
	}
	public void setRzid(String rzid){
		this.rzid = rzid;
	}
	//资源ID
	public String getZyid() {
		return zyid;
	}
	public void setZyid(String zyid){
		this.zyid = zyid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
