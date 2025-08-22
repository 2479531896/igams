package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="ZdyfaModel")
public class ZdyfaModel extends BaseModel{
	//方案ID
	private String faid;
	//微信ID
	private String wxid;
	//方案名称
	private String famc;
	//方案是否应用
	private String fasfyy;
	//方案ID
	public String getFaid() {
		return faid;
	}
	public void setFaid(String faid){
		this.faid = faid;
	}
	//微信ID
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid){
		this.wxid = wxid;
	}
	//方案名称
	public String getFamc() {
		return famc;
	}
	public void setFamc(String famc){
		this.famc = famc;
	}
	//方案是否应用
	public String getFasfyy() {
		return fasfyy;
	}
	public void setFasfyy(String fasfyy){
		this.fasfyy = fasfyy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
