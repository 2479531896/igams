package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JcsjModel")
public class JcsjModel extends BaseModel{
	//参数id
	private String csid;
	//基础类别
	private String jclb;
	//父参数id
	private String fcsid;
	//父父参数id
	private String ffcsid;
	//参数代码
	private String csdm;
	//参数名称
	private String csmc;
	//参数排序
	private String cspx;
	//参数扩展1
	private String cskz1;
	//参数扩展2
	private String cskz2;
	//参数扩展3
	private String cskz3;
	//参数扩展4
	private String cskz4;

	//参数扩展5
	private String cskz5;
	//是否默认
	private String sfmr;
	//备注
	private String bz;
	//参数id
	//分布式标识
	private String Prefix;
	//是否广播
	private String sfgb;
	
	public String getSfgb() {
		return sfgb;
	}
	public void setSfgb(String sfgb) {
		this.sfgb = sfgb;
	}
	public String getPrefix() {
		return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid){
		this.csid = csid;
	}
	//基础类别
	public String getJclb() {
		return jclb;
	}
	public void setJclb(String jclb){
		this.jclb = jclb;
	}
	//父参数id
	public String getFcsid() {
		return fcsid;
	}
	public void setFcsid(String fcsid){
		this.fcsid = fcsid;
	}
	//参数代码
	public String getCsdm() {
		return csdm;
	}
	public void setCsdm(String csdm){
		this.csdm = csdm;
	}
	//参数名称
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc){
		this.csmc = csmc;
	}
	//参数排序
	public String getCspx() {
		return cspx;
	}
	public void setCspx(String cspx){
		this.cspx = cspx;
	}
	//参数扩展1
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1){
		this.cskz1 = cskz1;
	}
	//参数扩展2
	public String getCskz2() {
		return cskz2;
	}
	public void setCskz2(String cskz2){
		this.cskz2 = cskz2;
	}
	//参数扩展3
	public String getCskz3() {
		return cskz3;
	}
	public void setCskz3(String cskz3){
		this.cskz3 = cskz3;
	}
	//参数扩展4
	public String getCskz4() {
		return cskz4;
	}
	public void setCskz4(String cskz4){
		this.cskz4 = cskz4;
	}
	//是否默认
	public String getSfmr() {
		return sfmr;
	}
	public void setSfmr(String sfmr){
		this.sfmr = sfmr;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}

	public String getFfcsid() {
		return ffcsid;
	}
	public void setFfcsid(String ffcsid) {
		this.ffcsid = ffcsid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getCskz5() {
		return cskz5;
	}

	public void setCskz5(String cskz5) {
		this.cskz5 = cskz5;
	}
}
