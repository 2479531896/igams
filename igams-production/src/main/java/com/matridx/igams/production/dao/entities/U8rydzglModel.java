package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="U8rydzglModel")
public class U8rydzglModel extends BaseModel{
	//机构ID
	private String jgid;
	//账户信息
	private String zhxx;
	//机构ID
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid){
		this.jgid = jgid;
	}
	//账户信息
	public String getZhxx() {
		return zhxx;
	}
	public void setZhxx(String zhxx){
		this.zhxx = zhxx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
