package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DsrwszModel")
public class DsrwszModel extends BaseModel{
	//任务ID
	private String rwid;
	//任务名称
	private String rwmc;
	//提醒类型
	private String remindtype;
	//定时信息
	private String dsxx;
	//执行类
	private String zxl;
	//执行方法
	private String zxff;
	//参数字段
	private String cs;

	public String getRemindtype() {
		return remindtype;
	}

	public void setRemindtype(String remindtype) {
		this.remindtype = remindtype;
	}

	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid){
		this.rwid = rwid;
	}
	//任务名称
	public String getRwmc() {
		return rwmc;
	}
	public void setRwmc(String rwmc){
		this.rwmc = rwmc;
	}
	//定时信息
	public String getDsxx() {
		return dsxx;
	}
	public void setDsxx(String dsxx){
		this.dsxx = dsxx;
	}
	//执行类
	public String getZxl() {
		return zxl;
	}
	public void setZxl(String zxl){
		this.zxl = zxl;
	}
	//执行方法
	public String getZxff() {
		return zxff;
	}
	public void setZxff(String zxff){
		this.zxff = zxff;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
