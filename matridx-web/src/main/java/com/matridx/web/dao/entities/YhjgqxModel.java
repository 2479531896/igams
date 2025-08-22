package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhjgqxModel")
public class YhjgqxModel extends BaseModel{
	//用户id
	private String yhid;
	//序号
	private String xh;
	//机构ID
	private String jgid;
	//角色ID
	private String jsid;
	//用户id
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//机构ID
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid){
		this.jgid = jgid;
	}
	//角色ID
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
