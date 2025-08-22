package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="LscxqxModel")
public class LscxqxModel extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//临时查询id
	private String cxid;
	//系统用户id
	private String yhid;
	//角色ID
	private String jsid;

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getCxid() {
		return cxid;
	}
	public void setCxid(String cxid) {
		this.cxid = cxid;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
}
