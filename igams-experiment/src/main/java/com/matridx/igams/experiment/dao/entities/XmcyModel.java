package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XmcyModel")
public class XmcyModel extends BaseModel{
	//项目ID
	private String xmid;
	//序号
	private String xh;
	//用户ID
	private String yhid;
	//项目ID
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid){
		this.xmid = xmid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//用户ID
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
