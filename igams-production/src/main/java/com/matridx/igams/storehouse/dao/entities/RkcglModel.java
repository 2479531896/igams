package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RkcglModel")
public class RkcglModel extends BaseModel{
	//人员ID
	private String ryid;
	//货物ID
	private String hwid;
	//人员ID
	public String getRyid() {
		return ryid;
	}
	public void setRyid(String ryid){
		this.ryid = ryid;
	}
	//货物ID
	public String getHwid() {
		return hwid;
	}
	public void setHwid(String hwid){
		this.hwid = hwid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
