package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JsyyqxModel")
public class JsyyqxModel extends BaseModel{
	//医院id
	private String yyid;
	//角色id
	private String jsid;
	//医院id
	public String getYyid() {
		return yyid;
	}
	public void setYyid(String yyid){
		this.yyid = yyid;
	}
	//角色id
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
