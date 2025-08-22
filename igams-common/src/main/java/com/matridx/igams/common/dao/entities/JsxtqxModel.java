package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.Model;

@Alias(value="JsxtqxModel")
public class JsxtqxModel extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3520307144710693474L;
	
	//角色ID
	private String jsid;
	//系统ID
	private String xtid;
	
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
	public String getXtid() {
		return xtid;
	}
	public void setXtid(String xtid) {
		this.xtid = xtid;
	}

}
