package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NgsglModel")
public class NgsglModel extends BaseModel{
	//NGSID
	private String ngsid;
	//检测单位
	private String jcdw;
	//检测单位名称
	private String jcdwmc;
	//检测ID
	private String jcid;
	//制备法
	private String zbf;
	//制备编码 D 或者R 需程序判断制备法
	private String zbbm;
	//内部编码
	private String nbbm;
	//提纯试剂批号
	private String tcsjph;
	//建库试剂批号
	private String jksjph;
	//接头号
	private String jth;
	//开始时间
	private String kssj;
	//结束时间
	private String jssj;
	//正常结束标记  0:未正常结束 1：正常结束
	private String zcjsbj;
	//备注
	private String bz;
	//设备ID
	private String sbid;
	//操作权限
	private String czqx;
	//创建时间
	private String cjsj;
	//试剂号
	private String sjh;
	//是否成功
	private String sfcg;
	//网关地址
	private String wgdz;
	//日志
	private String rz;
	//实验类型
	private String sylx;

	public String getSylx() {
		return sylx;
	}

	public void setSylx(String sylx) {
		this.sylx = sylx;
	}

	public String getRz() {
		return rz;
	}
	public void setRz(String rz) {
		this.rz = rz;
	}
	public String getWgdz() {
		return wgdz;
	}
	public void setWgdz(String wgdz) {
		this.wgdz = wgdz;
	}
	public String getSfcg() {
		return sfcg;
	}
	public void setSfcg(String sfcg) {
		this.sfcg = sfcg;
	}
	public String getSjh() {
		return sjh;
	}
	public void setSjh(String sjh) {
		this.sjh = sjh;
	}
	public String getSbid() {
		return sbid;
	}
	public void setSbid(String sbid) {
		this.sbid = sbid;
	}
	public String getCzqx() {
		return czqx;
	}
	public void setCzqx(String czqx) {
		this.czqx = czqx;
	}
	public String getCjsj() {
		return cjsj;
	}
	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
	//NGSID
	public String getNgsid() {
		return ngsid;
	}
	public void setNgsid(String ngsid){
		this.ngsid = ngsid;
	}
	//检测单位
	public String getJcdw() {
		return jcdw;
	}
	public void setJcdw(String jcdw){
		this.jcdw = jcdw;
	}
	//检测单位名称
	public String getJcdwmc() {
		return jcdwmc;
	}
	public void setJcdwmc(String jcdwmc){
		this.jcdwmc = jcdwmc;
	}
	//检测ID
	public String getJcid() {
		return jcid;
	}
	public void setJcid(String jcid){
		this.jcid = jcid;
	}
	//制备法
	public String getZbf() {
		return zbf;
	}
	public void setZbf(String zbf){
		this.zbf = zbf;
	}
	//制备编码 D 或者R 需程序判断制备法
	public String getZbbm() {
		return zbbm;
	}
	public void setZbbm(String zbbm){
		this.zbbm = zbbm;
	}
	//内部编码
	public String getNbbm() {
		return nbbm;
	}
	public void setNbbm(String nbbm){
		this.nbbm = nbbm;
	}
	//提纯试剂批号
	public String getTcsjph() {
		return tcsjph;
	}
	public void setTcsjph(String tcsjph){
		this.tcsjph = tcsjph;
	}
	//建库试剂批号
	public String getJksjph() {
		return jksjph;
	}
	public void setJksjph(String jksjph){
		this.jksjph = jksjph;
	}
	//接头号
	public String getJth() {
		return jth;
	}
	public void setJth(String jth){
		this.jth = jth;
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
	//正常结束标记  0:未正常结束 1：正常结束
	public String getZcjsbj() {
		return zcjsbj;
	}
	public void setZcjsbj(String zcjsbj){
		this.zcjsbj = zcjsbj;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
