package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="DsrwqxModel")
public class DsrwqxModel extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//定时任务id
	private String rwid;
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

	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid) {
		this.rwid = rwid;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
}
