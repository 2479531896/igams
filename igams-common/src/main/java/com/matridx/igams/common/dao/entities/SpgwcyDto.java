package com.matridx.igams.common.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SpgwcyDto")
public class SpgwcyDto extends SpgwcyModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户id
	private String yhid;
	//用户名
	protected String yhm;
	//真实姓名
	private String zsxm;
	//密码
	protected String mm;
	//是否锁定
	protected String sfsd;
	//当前角色
	private String dqjs;
	//登录ip
	private String dlip;
	//登录时间
	private String dlsj;
	//错误次数
	private String cwcs;
	
	//角色id
	private String jsid;
	//角色代码
	private String jsdm;
	//角色名称
	private String jsmc;
	//角色描述
	private String jsms;
	//父角色ID
	private String fjsid;
	//首页类型
	private String sylx;
	//单位限定标记
	private String dwxdbj;
	//用户ID集合
	private String yhids;
	//角色ID集合
	private String jsids;
	//微信ID
	private String wxid;
	//岗位名称
	private String gwmc;
	//审批岗位单位限制
	private String dwxz;
	//机构ID
	private String jgid;
	//是否广播
	private String sfgb;
	//角色IDlist
	private List<String> jsidlist;
	//用户IDlist
	private List<String> yhidlist;
	//审核类别
	private String shlb;
	//业务id
	private String ywid;
	private String lcxh;
	private String zszg;//直属主管
	private String gcid;//过程id

	public String getGcid() {
		return gcid;
	}

	public void setGcid(String gcid) {
		this.gcid = gcid;
	}

	public String getLcxh() {
		return lcxh;
	}

	public void setLcxh(String lcxh) {
		this.lcxh = lcxh;
	}

	public String getZszg() {
		return zszg;
	}

	public void setZszg(String zszg) {
		this.zszg = zszg;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getShlb() {
		return shlb;
	}

	public void setShlb(String shlb) {
		this.shlb = shlb;
	}

	public String getSfgb() {
		return sfgb;
	}
	public void setSfgb(String sfgb) {
		this.sfgb = sfgb;
	}
	public List<String> getJsidlist() {
		return jsidlist;
	}
	public void setJsidlist(List<String> jsidlist) {
		this.jsidlist = jsidlist;
	}
	public List<String> getYhidlist() {
		return yhidlist;
	}
	public void setYhidlist(List<String> yhidlist) {
		this.yhidlist = yhidlist;
	}
	public String getGwmc()
	{
		return gwmc;
	}
	public void setGwmc(String gwmc)
	{
		this.gwmc = gwmc;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
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
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getSfsd() {
		return sfsd;
	}
	public void setSfsd(String sfsd) {
		this.sfsd = sfsd;
	}
	public String getDqjs() {
		return dqjs;
	}
	public void setDqjs(String dqjs) {
		this.dqjs = dqjs;
	}
	public String getDlip() {
		return dlip;
	}
	public void setDlip(String dlip) {
		this.dlip = dlip;
	}
	public String getDlsj() {
		return dlsj;
	}
	public void setDlsj(String dlsj) {
		this.dlsj = dlsj;
	}
	public String getCwcs() {
		return cwcs;
	}
	public void setCwcs(String cwcs) {
		this.cwcs = cwcs;
	}
	public String getJsid() {
		return jsid;
	}
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
	public String getJsdm() {
		return jsdm;
	}
	public void setJsdm(String jsdm) {
		this.jsdm = jsdm;
	}
	public String getJsmc() {
		return jsmc;
	}
	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}
	public String getJsms() {
		return jsms;
	}
	public void setJsms(String jsms) {
		this.jsms = jsms;
	}
	public String getFjsid() {
		return fjsid;
	}
	public void setFjsid(String fjsid) {
		this.fjsid = fjsid;
	}
	public String getSylx() {
		return sylx;
	}
	public void setSylx(String sylx) {
		this.sylx = sylx;
	}
	public String getDwxdbj() {
		return dwxdbj;
	}
	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}
	public String getYhids() {
		return yhids;
	}
	public void setYhids(String yhids) {
		this.yhids = yhids;
	}
	public String getJsids() {
		return jsids;
	}
	public void setJsids(String jsids) {
		this.jsids = jsids;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getDwxz() {
		return dwxz;
	}
	public void setDwxz(String dwxz) {
		this.dwxz = dwxz;
	}
	public String getJgid() {
		return jgid;
	}
	public void setJgid(String jgid) {
		this.jgid = jgid;
	}
	
}
