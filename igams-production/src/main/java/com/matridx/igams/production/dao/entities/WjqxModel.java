package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WjqxModel")
public class WjqxModel extends BaseModel{
	//权限ID
	private String qxid;
	//文件ID
	private String wjid;
	//角色ID
	private String jsid;
	//权限类型  VIEW：查看，无需学习  STUDY：学习
	private String qxlx;
	//权限ID
	public String getQxid() {
		return qxid;
	}
	public void setQxid(String qxid){
		this.qxid = qxid;
	}
	//文件ID
	public String getWjid() {
		return wjid;
	}
	public void setWjid(String wjid){
		this.wjid = wjid;
	}
	//角色ID
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}
	//权限类型
	public String getQxlx() {
		return qxlx;
	}
	public void setQxlx(String qxlx) {
		this.qxlx = qxlx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
