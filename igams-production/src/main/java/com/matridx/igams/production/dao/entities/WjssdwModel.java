package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WjssdwModel")
public class WjssdwModel extends BaseModel{
	//文件ID
	private String wjid;
	//序号
	private String xh;
	//机构id
	private String jgid;
	//文件ID
	public String getWjid() {
		return wjid;
	}
	public void setWjid(String wjid){
		this.wjid = wjid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//机构id
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid){
		this.jgid = jgid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
