package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="QgqxglModel")
public class QgqxglModel extends BaseBasicModel{
	//请购取消ID
	private String qgqxid;
	//请购ID
	private String qgid;
	//单据号
	private String djh;
	//申请人
	private String sqr;
	//申请部门
	private String sqbm;
	//申请日期
	private String sqrq;
	//申请理由
	private String sqly;
	//备注
	private String bz;
	//状态   
	private String zt;
	//请购取消ID
	public String getQgqxid() {
		return qgqxid;
	}
	public void setQgqxid(String qgqxid){
		this.qgqxid = qgqxid;
	}
	//请购ID
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid){
		this.qgid = qgid;
	}
	//单据号
	public String getDjh() {
		return djh;
	}
	public void setDjh(String djh){
		this.djh = djh;
	}
	//申请人
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr){
		this.sqr = sqr;
	}
	//申请部门
	public String getSqbm() {
		return sqbm;
	}
	public void setSqbm(String sqbm){
		this.sqbm = sqbm;
	}
	//申请日期
	public String getSqrq() {
		return sqrq;
	}
	public void setSqrq(String sqrq){
		this.sqrq = sqrq;
	}
	//申请理由
	public String getSqly() {
		return sqly;
	}
	public void setSqly(String sqly){
		this.sqly = sqly;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态   
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
