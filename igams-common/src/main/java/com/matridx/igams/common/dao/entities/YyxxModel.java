package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YyxxModel")
public class YyxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//单位id
	private String dwid;
	//单位名称
	private String dwmc;
	//单位简称
	private String dwjc;
	//单位其他名称 逗号隔开
	private String dwqtmc;
	//单位等级 
	private String dwdj;
	//单位类别
	private String dwlb;
	//省份
	private String sf;
	//城市 
	private String cs;
	//参数扩展1
	private String cskz1;
	//参数扩展2
	private String cskz2;
	//参数扩展3
	private String cskz3;
	//参数扩展4
	private String cskz4;
	//统一社会信用代码
	private String shxydm;
	//联系人
	private String lxr;
	//联系电话
	private String lxdh;
	//统计名称
	private String tjmc;
	//检测项目匹配
	private String jcxmpp;
	//rabbit的virtualhost
	private String virtualhost;

	public String getVirtualhost() {
		return virtualhost;
	}

	public void setVirtualhost(String virtualhost) {
		this.virtualhost = virtualhost;
	}
	public String getJcxmpp() {
		return jcxmpp;
	}

	public void setJcxmpp(String jcxmpp) {
		this.jcxmpp = jcxmpp;
	}

	public String getTjmc() {
		return tjmc;
	}

	public void setTjmc(String tjmc) {
		this.tjmc = tjmc;
	}

	public String getShxydm() {
		return shxydm;
	}

	public void setShxydm(String shxydm) {
		this.shxydm = shxydm;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getDwid() {
		return dwid;
	}
	public void setDwid(String dwid) {
		this.dwid = dwid;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getDwjc() {
		return dwjc;
	}
	public void setDwjc(String dwjc) {
		this.dwjc = dwjc;
	}
	public String getDwqtmc() {
		return dwqtmc;
	}
	public void setDwqtmc(String dwqtmc) {
		this.dwqtmc = dwqtmc;
	}
	public String getDwdj() {
		return dwdj;
	}
	public void setDwdj(String dwdj) {
		this.dwdj = dwdj;
	}
	public String getDwlb() {
		return dwlb;
	}
	public void setDwlb(String dwlb) {
		this.dwlb = dwlb;
	}
	public String getSf() {
		return sf;
	}
	public void setSf(String sf) {
		this.sf = sf;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getCskz2() {
		return cskz2;
	}
	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}
	public String getCskz3() {
		return cskz3;
	}
	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getCskz4() {
		return cskz4;
	}

	public void setCskz4(String cskz4) {
		this.cskz4 = cskz4;
	}
}
