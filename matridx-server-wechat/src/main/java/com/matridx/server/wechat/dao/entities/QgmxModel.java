package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QgmxModel")
public class QgmxModel extends BaseModel{
	//采购明细ID
	private String qgmxid;
	//采购ID
	private String qgid;
	//序号
	private String xh;
	//物料ID
	private String wlid;
	//数量
	private String sl;
	//价格
	private String jg;
	//期望到货日期
	private String qwrq;
	//备注  审批备注，采购备注是否需要分开
	private String bz;
	//取消采购 审核通过时点击  0：正常  1：取消采购
	private String qxqg;
	//状态   审批状态 如果物料不通过  00 15,80
	private String zt;
	//关联id，关联U8中的主键ID
	private String glid;
	//拒绝审批人员
	private String jjspry;
	//拒绝审批时间
	private String jjspsj;
	//货物名称
	private String hwmc;
	//货物计量单位
	private String hwjldw;
	//供应商
	private String gys;
	//是否入库
	private String sfrk;

	public String getSfrk() {
		return sfrk;
	}
	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}
	public String getHwjldw() {
		return hwjldw;
	}
	public void setHwjldw(String hwjldw) {
		this.hwjldw = hwjldw;
	}
	public String getGys() {
		return gys;
	}
	public void setGys(String gys) {
		this.gys = gys;
	}
	public String getHwmc() {
		return hwmc;
	}
	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}
	public String getJjspry() {
		return jjspry;
	}
	public void setJjspry(String jjspry) {
		this.jjspry = jjspry;
	}
	public String getJjspsj() {
		return jjspsj;
	}
	public void setJjspsj(String jjspsj) {
		this.jjspsj = jjspsj;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public String getQgmxid() {
		return qgmxid;
	}
	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}
	public String getQgid() {
		return qgid;
	}
	public void setQgid(String qgid) {
		this.qgid = qgid;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//物料ID
	public String getWlid() {
		return wlid;
	}
	public void setWlid(String wlid){
		this.wlid = wlid;
	}
	//数量
	public String getSl() {
		return sl;
	}
	public void setSl(String sl){
		this.sl = sl;
	}
	//价格
	public String getJg() {
		return jg;
	}
	public void setJg(String jg){
		this.jg = jg;
	}
	//期望到货日期
	public String getQwrq() {
		return qwrq;
	}
	public void setQwrq(String qwrq){
		this.qwrq = qwrq;
	}
	//备注  审批备注，采购备注是否需要分开
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//取消采购 审核通过时点击  0：正常  1：取消采购
	public String getQxqg() {
		return qxqg;
	}
	public void setQxqg(String qxqg){
		this.qxqg = qxqg;
	}
	//状态   审批状态 如果物料不通过  00 15,80
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
