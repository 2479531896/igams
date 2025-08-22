package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WbhbyzModel")
public class WbhbyzModel extends BaseModel{
	//代码
	private String code;
	//伙伴ID
	private String hbid;
	//相关是何止
	private String sz;
	//代码
	public String getCode() {
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	//伙伴ID
	public String getHbid() {
		return hbid;
	}
	public void setHbid(String hbid){
		this.hbid = hbid;
	}

	public String getSz() {
		return sz;
	}

	public void setSz(String sz) {
		this.sz = sz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
