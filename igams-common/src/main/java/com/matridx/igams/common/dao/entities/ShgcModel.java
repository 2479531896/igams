package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ShgcModel")
public class ShgcModel extends BaseBasicModel{
	//过程ID
	private String gcid;
	//业务ID
	private String ywid;
	//审核ID
	private String shid;
	//现流程序号
	private String xlcxh;
	//后续流程序号
	private String hxlcxh;
	//申请时间
	private String sqsj;
	//申请人
	private String sqr;
	private String lxid;//业务id
	private String lx;//类型
	private String sqbm;//申请部门
	private String ywdm;//业务代码
	private String ywmc;//业务名称
	private String ywlx;//业务类型
	private String shgwid;//审核岗位id

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getYwdm() {
		return ywdm;
	}

	public void setYwdm(String ywdm) {
		this.ywdm = ywdm;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getShgwid() {
		return shgwid;
	}

	public void setShgwid(String shgwid) {
		this.shgwid = shgwid;
	}

	public String getLxid() {
		return lxid;
	}

	public void setLxid(String lxid) {
		this.lxid = lxid;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}
	//过程ID
	public String getGcid() {
		return gcid;
	}
	public void setGcid(String gcid){
		this.gcid = gcid;
	}
	//业务ID
	public String getYwid() {
		return ywid;
	}
	public void setYwid(String ywid){
		this.ywid = ywid;
	}
	//审核ID
	public String getShid() {
		return shid;
	}
	public void setShid(String shid){
		this.shid = shid;
	}
	//现流程序号
	public String getXlcxh() {
		return xlcxh;
	}
	public void setXlcxh(String xlcxh){
		this.xlcxh = xlcxh;
	}
	//后续流程序号
	public String getHxlcxh() {
		return hxlcxh;
	}
	public void setHxlcxh(String hxlcxh){
		this.hxlcxh = hxlcxh;
	}
	//申请时间
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj){
		this.sqsj = sqsj;
	}
	//申请人
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr){
		this.sqr = sqr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
