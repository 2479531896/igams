package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DdxxglDto")
public class DdxxglDto extends DdxxglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户名
	private String yhm;
	//真实姓名
	private String zsxm;
	//钉钉ID
	private String ddid;
	//通知类型（用户或者角色）
	private String tzlx;
	//系统用户id
	private String xtyhid;
	//系统用户名称
	private String xtyhmc;
	//审批人(钉钉请购审核使用)
	private String spr;
	//抄送人(钉钉请购审核使用)
	private String csr;
	//机构名称
	private String jgmc;
	//审批人s
	private String[] sprs;
	//抄送人s
	private String[] csrs;
	//机构id
	private String jgid;
	//审批ID(钉钉审批流程表中的主键)
	private String spid;
	//钉钉消息类型的参数名称（作为定时任务发送的信息标题）
	private String ddxxmc;
	private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getDdxxmc() {
		return ddxxmc;
	}
	public void setDdxxmc(String ddxxmc) {
		this.ddxxmc = ddxxmc;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	public String[] getSprs() {
		return sprs;
	}
	public void setSprs(String[] sprs) {
		this.sprs = sprs;
	}
	public String[] getCsrs() {
		return csrs;
	}
	public void setCsrs(String[] csrs) {
		this.csrs = csrs;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	public String getSpr() {
		return spr;
	}
	public void setSpr(String spr) {
		this.spr = spr;
	}
	public String getCsr() {
		return csr;
	}
	public void setCsr(String csr) {
		this.csr = csr;
	}
	public String getTzlx()
	{
		return tzlx;
	}
	public void setTzlx(String tzlx)
	{
		this.tzlx = tzlx;
	}
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm) {
		this.yhm = yhm;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getXtyhid()
	{
		return xtyhid;
	}
	public void setXtyhid(String xtyhid)
	{
		this.xtyhid = xtyhid;
	}
	public String getXtyhmc()
	{
		return xtyhmc;
	}
	public void setXtyhmc(String xtyhmc)
	{
		this.xtyhmc = xtyhmc;
	}
	
	
}
