package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="WtxxModel")
public class WtxxModel extends BaseModel{
	//委托编号
	private String wtbh;
	//委托人
	private String wtr;
	//受托人
	private String str;
	//开始时间
	private String kssj;
	//结束时间
	private String jssj;
	//委托角色
	private String wtjs;
	//委托区分  CLIENT客户端  MANAGE:管理端
	private String wtqf;
	//角色ID
	private String jsid;
	//联系对象ID
	private String lxdxid;
	//委托编号
	public String getWtbh() {
		return wtbh;
	}
	public void setWtbh(String wtbh){
		this.wtbh = wtbh;
	}
	//委托人
	public String getWtr() {
		return wtr;
	}
	public void setWtr(String wtr){
		this.wtr = wtr;
	}
	//受托人
	public String getStr() {
		return str;
	}
	public void setStr(String str){
		this.str = str;
	}
	//开始时间
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj){
		this.kssj = kssj;
	}
	//结束时间
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj){
		this.jssj = jssj;
	}
	//委托角色
	public String getWtjs() {
		return wtjs;
	}
	public void setWtjs(String wtjs){
		this.wtjs = wtjs;
	}
	//委托区分  CLIENT客户端  MANAGE:管理端
	public String getWtqf() {
		return wtqf;
	}
	public void setWtqf(String wtqf){
		this.wtqf = wtqf;
	}
	//角色ID
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid){
		this.jsid = jsid;
	}
	//联系对象ID
	public String getLxdxid() {
		return lxdxid;
	}
	public void setLxdxid(String lxdxid){
		this.lxdxid = lxdxid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
