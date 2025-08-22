package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KhzyfpModel")
public class KhzyfpModel extends BaseModel{

	//资源分配ID
	private String zyfpid;
	//单位ID
	private String dwid;
	//用户ID
	private String yhid;

	public String getZyfpid() {
		return zyfpid;
	}

	public void setZyfpid(String zyfpid) {
		this.zyfpid = zyfpid;
	}

	public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
