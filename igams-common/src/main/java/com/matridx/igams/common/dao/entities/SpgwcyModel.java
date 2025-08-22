package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SpgwcyModel")
public class SpgwcyModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//岗位ID
	private String gwid;
	//用户ID
	private String yhid;
	//角色ID
	private String jsid;
	//分布式标记
	private String prefix;
	
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid) {
		this.gwid = gwid;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
}
