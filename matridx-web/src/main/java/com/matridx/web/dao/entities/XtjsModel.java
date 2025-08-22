package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XtjsModel")
public class XtjsModel extends BaseModel{
	//角色id
	private String jsid;
	//角色代码
	private String jsdm;
	//角色名称
	private String jsmc;
	//角色描述
	private String jsms;
	//父角色ID
	private String fjsid;
	//首页类型
	private String sylx;
	//单位限定标记
	private String dwxdbj;
	private String sfzj;

	//角色id
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}
	//角色名称
	public String getJsmc() {
		return jsmc;
	}
	public void setJsmc(String jsmc){
		this.jsmc = jsmc;
	}
	//角色描述
	public String getJsms() {
		return jsms;
	}
	public void setJsms(String jsms){
		this.jsms = jsms;
	}
	//父角色ID
	public String getFjsid() {
		return fjsid;
	}
	public void setFjsid(String fjsid){
		this.fjsid = fjsid;
	}
	//首页类型
	public String getSylx() {
		return sylx;
	}
	public void setSylx(String sylx){
		this.sylx = sylx;
	}
	//角色代码
	public String getJsdm() {
		return jsdm;
	}
	public void setJsdm(String jsdm) {
		this.jsdm = jsdm;
	}
	//单位限定标记
	public String getDwxdbj() {
		return dwxdbj;
	}
	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}

	public String getSfzj() {
		return sfzj;
	}

	public void setSfzj(String sfzj) {
		this.sfzj = sfzj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
