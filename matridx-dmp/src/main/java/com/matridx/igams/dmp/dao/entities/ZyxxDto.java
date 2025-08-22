package com.matridx.igams.dmp.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ZyxxDto")
public class ZyxxDto extends ZyxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//提供方ID
	private String tgfid;
	//提供方代码
	private String tgfdm;
	//提供方名称
	private String tgfmc;
	//认证ID
	private String rzid;
	//权限数组数据
	private String competenceArray;
	//数据库ID
	private String sjkid;
	//数据库名
	private String sjkm;
	//数据库用户名
	private String sjkyhm;
	//数据库密码
	private String sjkmm;
	//数据库类型
	private String sjklx;
	//数据库连接串
	private String sjkljc;
	//连接ID
	private String ljid;
	//链接名称
	private String ljmc;
	//链接地址
	private String ljdz;
	//调用方式
	private String dyfs;
	//请求方式
	private String qqfs;
	//选择方式
	private String xzfs;
	//接口参数
	private String jkcs;
	//权限ID
	private String qxid;
	//认证用户代码
	private String rzyhdm;
	//认证键
	private String rzkey;
	//认证用户名
	private String rzyhm;
	
	public String getTgfdm() {
		return tgfdm;
	}
	public void setTgfdm(String tgfdm) {
		this.tgfdm = tgfdm;
	}
	public String getTgfmc() {
		return tgfmc;
	}
	public void setTgfmc(String tgfmc) {
		this.tgfmc = tgfmc;
	}
	public String getRzid() {
		return rzid;
	}
	public void setRzid(String rzid) {
		this.rzid = rzid;
	}
	public String getTgfid() {
		return tgfid;
	}
	public void setTgfid(String tgfid) {
		this.tgfid = tgfid;
	}
	public String getCompetenceArray() {
		return competenceArray;
	}
	public void setCompetenceArray(String competenceArray) {
		this.competenceArray = competenceArray;
	}
	public String getSjkid() {
		return sjkid;
	}
	public void setSjkid(String sjkid) {
		this.sjkid = sjkid;
	}
	public String getSjkm() {
		return sjkm;
	}
	public void setSjkm(String sjkm) {
		this.sjkm = sjkm;
	}
	public String getSjkyhm() {
		return sjkyhm;
	}
	public void setSjkyhm(String sjkyhm) {
		this.sjkyhm = sjkyhm;
	}
	public String getSjkmm() {
		return sjkmm;
	}
	public void setSjkmm(String sjkmm) {
		this.sjkmm = sjkmm;
	}
	public String getSjklx() {
		return sjklx;
	}
	public void setSjklx(String sjklx) {
		this.sjklx = sjklx;
	}
	public String getSjkljc() {
		return sjkljc;
	}
	public void setSjkljc(String sjkljc) {
		this.sjkljc = sjkljc;
	}
	public String getLjid() {
		return ljid;
	}
	public void setLjid(String ljid) {
		this.ljid = ljid;
	}
	public String getLjmc() {
		return ljmc;
	}
	public void setLjmc(String ljmc) {
		this.ljmc = ljmc;
	}
	public String getLjdz() {
		return ljdz;
	}
	public void setLjdz(String ljdz) {
		this.ljdz = ljdz;
	}
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}
	public String getQqfs() {
		return qqfs;
	}
	public void setQqfs(String qqfs) {
		this.qqfs = qqfs;
	}
	public String getXzfs() {
		return xzfs;
	}
	public void setXzfs(String xzfs) {
		this.xzfs = xzfs;
	}
	public String getJkcs() {
		return jkcs;
	}
	public void setJkcs(String jkcs) {
		this.jkcs = jkcs;
	}
	public String getQxid() {
		return qxid;
	}
	public void setQxid(String qxid) {
		this.qxid = qxid;
	}
	public String getRzkey() {
		return rzkey;
	}
	public void setRzkey(String rzkey) {
		this.rzkey = rzkey;
	}
	public String getRzyhdm() {
		return rzyhdm;
	}
	public void setRzyhdm(String rzyhdm) {
		this.rzyhdm = rzyhdm;
	}
	public String getRzyhm() {
		return rzyhm;
	}
	public void setRzyhm(String rzyhm) {
		this.rzyhm = rzyhm;
	}
	
}
