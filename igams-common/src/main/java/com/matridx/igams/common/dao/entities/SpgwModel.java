package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SpgwModel")
public class SpgwModel extends BaseModel{
	//岗位ID
	private String gwid;
	//岗位名称
	private String gwmc;
	//是否业务角色
	private String sfywjs;
	//业务角色
	private String ywjs;
	//外部岗位
	private String wbgw;
	//分布式标识
    private String prefix;
    
	//单位限制，用于审核时限制所属部门
	private String dwxz;
	//是否广播
	private String sfgb;

	public String getSfgb() {
		return sfgb;
	}
	public void setSfgb(String sfgb) {
		this.sfgb = sfgb;
	}
	public String getDwxz() {
		return dwxz;
	}
	public void setDwxz(String dwxz) {
		this.dwxz = dwxz;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	//岗位ID
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid){
		this.gwid = gwid;
	}
	//岗位名称
	public String getGwmc() {
		return gwmc;
	}
	public void setGwmc(String gwmc){
		this.gwmc = gwmc;
	}
	//是否业务角色
	public String getSfywjs() {
		return sfywjs;
	}
	public void setSfywjs(String sfywjs){
		this.sfywjs = sfywjs;
	}
	//业务角色
	public String getYwjs() {
		return ywjs;
	}
	public void setYwjs(String ywjs){
		this.ywjs = ywjs;
	}
	//外部岗位
	public String getWbgw() {
		return wbgw;
	}
	public void setWbgw(String wbgw){
		this.wbgw = wbgw;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
