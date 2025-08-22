package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DbglModel")
public class DbglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//调拨id
	private String dbid;
	//调拨单号
	private String dbdh;
	//转出仓库
	private String zcck;
	//转入仓库
	private String zrck;
	//出库类别
	private String cklb;
	//入库类别
	private String rklb;
	//调拨日期
	private String dbrq;
	//经手人
	private String jsr;
	//关联id
	private String glid;
	//其他出库关联id
	private String qtckglid;
	//其他入库关联id
	private String qtrkglid;
	//备注
	private String bz;
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	public String getDbdh() {
		return dbdh;
	}
	public void setDbdh(String dbdh) {
		this.dbdh = dbdh;
	}
	public String getZcck() {
		return zcck;
	}
	public void setZcck(String zcck) {
		this.zcck = zcck;
	}
	public String getZrck() {
		return zrck;
	}
	public void setZrck(String zrck) {
		this.zrck = zrck;
	}
	public String getCklb() {
		return cklb;
	}
	public void setCklb(String cklb) {
		this.cklb = cklb;
	}
	public String getRklb() {
		return rklb;
	}
	public void setRklb(String rklb) {
		this.rklb = rklb;
	}
	public String getDbrq() {
		return dbrq;
	}
	public void setDbrq(String dbrq) {
		this.dbrq = dbrq;
	}
	public String getJsr() {
		return jsr;
	}
	public void setJsr(String jsr) {
		this.jsr = jsr;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getQtckglid() {
		return qtckglid;
	}
	public void setQtckglid(String qtckglid) {
		this.qtckglid = qtckglid;
	}
	public String getQtrkglid() {
		return qtrkglid;
	}
	public void setQtrkglid(String qtrkglid) {
		this.qtrkglid = qtrkglid;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
}
