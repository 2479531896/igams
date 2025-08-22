package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DbmxModel")
public class DbmxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//调拨明细id
	private String dbmxid;
	//调拨id
	private String dbid;
	//调出货物id
	private String dchwid;
	//调入货物id
	private String drhwid;
	//调拨数量
	private String dbsl;
	//明细关联id
	private String mxglid;
	//其他出库明细关联id
	private String qtckmxglid;
	//其他入库明细关联id
	private String qtrkmxglid;
	//调出货位
	private String dchw;
	//调入货位
	private String drhw;
	
	
	public String getDchw() {
		return dchw;
	}
	public void setDchw(String dchw) {
		this.dchw = dchw;
	}
	public String getDrhw() {
		return drhw;
	}
	public void setDrhw(String drhw) {
		this.drhw = drhw;
	}
	public String getDbmxid() {
		return dbmxid;
	}
	public void setDbmxid(String dbmxid) {
		this.dbmxid = dbmxid;
	}
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	public String getDchwid() {
		return dchwid;
	}
	public void setDchwid(String dchwid) {
		this.dchwid = dchwid;
	}
	public String getDrhwid() {
		return drhwid;
	}
	public void setDrhwid(String drhwid) {
		this.drhwid = drhwid;
	}
	public String getDbsl() {
		return dbsl;
	}
	public void setDbsl(String dbsl) {
		this.dbsl = dbsl;
	}
	public String getMxglid() {
		return mxglid;
	}
	public void setMxglid(String mxglid) {
		this.mxglid = mxglid;
	}
	public String getQtckmxglid() {
		return qtckmxglid;
	}
	public void setQtckmxglid(String qtckmxglid) {
		this.qtckmxglid = qtckmxglid;
	}
	public String getQtrkmxglid() {
		return qtrkmxglid;
	}
	public void setQtrkmxglid(String qtrkmxglid) {
		this.qtrkmxglid = qtrkmxglid;
	}
	 
	
}
