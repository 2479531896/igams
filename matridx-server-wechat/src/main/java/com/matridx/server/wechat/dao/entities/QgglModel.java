package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QgglModel")
public class QgglModel extends BaseModel{
	//采购ID
	private String qgid;
	//采购单号
	private String djh;
	//申请人
	private String sqr;
	//申请部门
	private String sqbm;
	//记录编号
	private String jlbh;
	//申请日期
	private String sqrq;
	//项目编码
	private String xmbm;
	//项目大类
	private String xmdl;
	//申请理由
	private String sqly;
	//支付方式 采用基础数据
	private String zffs;
	//付款方 采用基础数据
	private String fkf;
	//备注
	private String bz;
	//状态   
	private String zt;
	//钉钉实例ID
	private String ddslid;
	//关联ID，关联U8中主键ID
	private String glid;
	//请购类别
	private String qglb;
	//是否报销
	private String sfbx;

	public String getSfbx() {
		return sfbx;
	}
	public void setSfbx(String sfbx) {
		this.sfbx = sfbx;
	}
	public String getQglb() {
		return qglb;
	}
	public void setQglb(String qglb) {
		this.qglb = qglb;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getDdslid() {
		return ddslid;
	}
	public void setDdslid(String ddslid) {
		this.ddslid = ddslid;
	}
	//采购ID
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid){
		this.qgid = qgid;
	}
	//采购单号
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
	//记录编号
	public String getJlbh() {
		return jlbh;
	}
	public void setJlbh(String jlbh){
		this.jlbh = jlbh;
	}
	//申请日期
	public String getSqrq() {
		return sqrq;
	}
	public void setSqrq(String sqrq){
		this.sqrq = sqrq;
	}
	//项目编码
	public String getXmbm() {
		return xmbm;
	}
	public void setXmbm(String xmbm){
		this.xmbm = xmbm;
	}
	//项目大类
	public String getXmdl() {
		return xmdl;
	}
	public void setXmdl(String xmdl){
		this.xmdl = xmdl;
	}
	//申请理由
	public String getSqly() {
		return sqly;
	}
	public void setSqly(String sqly){
		this.sqly = sqly;
	}
	//支付方式 采用基础数据
	public String getZffs() {
		return zffs;
	}
	public void setZffs(String zffs){
		this.zffs = zffs;
	}
	//付款方 采用基础数据
	public String getFkf() {
		return fkf;
	}
	public void setFkf(String fkf){
		this.fkf = fkf;
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
