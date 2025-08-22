package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XmscxxModel")
public class XmscxxModel extends BaseModel{
	//项目ID
	private String xmid;
	//父项目ID
	private String fxmid;
	//项目ID
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid){
		this.xmid = xmid;
	}
	//父项目ID
	public String getFxmid() {
		return fxmid;
	}
	public void setFxmid(String fxmid){
		this.fxmid = fxmid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
