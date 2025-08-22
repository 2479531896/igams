package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

@Alias(value="DhxxModel")
public class DhxxModel extends BaseBasicModel{
	//到货ID
	private String dhid;
	//供应商ID
	private String gysid;
	//到货单号
	private String dhdh;
	//到货日期
	private String dhrq;
	//备注
	private String bz;
	//状态 00：未提交  10：审核中
	private String zt;
	//订单完成标记 0:未完成  1：完成 全部已处理
	private String wcbj;
	//到货ID
	private String htid;
	//部门
	private String bm;
	//仓库ID
	private String ckid;
	//采购类型
	private String cglx;
	//入库类别
	private String rklb;
	//关联id
	private String glid;
	//确认人员
	private String qrry;
	//确认时间
	private String qrsj;
	//确认标记
	private String qrbj;
	//请验单号
	private String qydh;
	//到货类型
	private String dhlx;
	//生产指令id
	private String sczlid;
	private String clbj;//处理标记

	public String getClbj() {
		return clbj;
	}

	public void setClbj(String clbj) {
		this.clbj = clbj;
	}

	public String getSczlid() {
		return sczlid;
	}

	public void setSczlid(String sczlid) {
		this.sczlid = sczlid;
	}

	public String getDhlx() {
		return dhlx;
	}
	public void setDhlx(String dhlx) {
		this.dhlx = dhlx;
	}
	public String getQydh() {
		return qydh;
	}
	public void setQydh(String qydh) {
		this.qydh = qydh;
	}
	public String getQrry() {
		return qrry;
	}
	public void setQrry(String qrry) {
		this.qrry = qrry;
	}
	public String getQrsj() {
		return qrsj;
	}
	public void setQrsj(String qrsj) {
		this.qrsj = qrsj;
	}
	public String getQrbj() {
		return qrbj;
	}
	public void setQrbj(String qrbj) {
		this.qrbj = qrbj;
	}
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getCglx() {
		return cglx;
	}
	public void setCglx(String cglx) {
		this.cglx = cglx;
	}
	public String getRklb() {
		return rklb;
	}
	public void setRklb(String rklb) {
		this.rklb = rklb;
	}
	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid) {
		this.htid = htid;
	}
	//到货ID
	public String getDhid() {
		return dhid;
	}
	public void setDhid(String dhid){
		this.dhid = dhid;
	}
	//供应商ID
	public String getGysid() {
		return gysid;
	}
	public void setGysid(String gysid){
		this.gysid = gysid;
	}
	//到货单号
	public String getDhdh() {
		return dhdh;
	}
	public void setDhdh(String dhdh){
		this.dhdh = dhdh;
	}
	//到货日期
	public String getDhrq() {
		return dhrq;
	}
	public void setDhrq(String dhrq){
		this.dhrq = dhrq;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//状态 00：未提交  10：审核中
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//订单完成标记 0:未完成  1：完成 全部已处理
	public String getWcbj() {
		return wcbj;
	}
	public void setWcbj(String wcbj){
		this.wcbj = wcbj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
