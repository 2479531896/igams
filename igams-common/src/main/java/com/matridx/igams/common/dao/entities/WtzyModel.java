package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WtzyModel")
public class WtzyModel extends BaseModel{
	//委托编号
	private String wtbh;
	//系统资源
	private String xtzy;
	//委托编号
	public String getWtbh() {
		return wtbh;
	}
	public void setWtbh(String wtbh){
		this.wtbh = wtbh;
	}
	//系统资源
	public String getXtzy() {
		return xtzy;
	}
	public void setXtzy(String xtzy){
		this.xtzy = xtzy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
