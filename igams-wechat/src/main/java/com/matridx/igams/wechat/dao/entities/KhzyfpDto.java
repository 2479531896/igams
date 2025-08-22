package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="KhzyfpDto")
public class KhzyfpDto extends KhzyfpModel{
	//用户名称
	private String yhmc;
	//客户分类名称
	private String dwflmc;
	//是否认可
	private String sfrk;
	//单位名称
	private String dwmc;
	//单位限制标记
	private String dwxzbj;
	//联系人姓名
	private String lxrxm;
	//拜访人名称
	private String bfrmc;
	//拜访类型名称
	private String bflxmc;
	//沟通内容
	private String gtnr;
	//拜访时间起始
	private String bfsjqs;
	//钉钉按钮标记
	private String ddanbj;
	//全部(查询条件)
	private String entire;
	//拜访ID
	private String bfid;
	//分配标记
	private String fpbj;
	//资源分配Json
	private String zyfp_json;
	//用户名
	private String yhm;
	//钉钉ID
	private String ddid;
	//客户分级名称
	private String khfjmc;

	private List<String> yhlist;

	public List<String> getYhlist() {
		return yhlist;
	}

	public void setYhlist(List<String> yhlist) {
		this.yhlist = yhlist;
	}

	public String getKhfjmc() {
		return khfjmc;
	}

	public void setKhfjmc(String khfjmc) {
		this.khfjmc = khfjmc;
	}

	private String dwjc;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getZyfp_json() {
		return zyfp_json;
	}

	public void setZyfp_json(String zyfp_json) {
		this.zyfp_json = zyfp_json;
	}

	public String getFpbj() {
		return fpbj;
	}

	public void setFpbj(String fpbj) {
		this.fpbj = fpbj;
	}

	public String getBfid() {
		return bfid;
	}

	public void setBfid(String bfid) {
		this.bfid = bfid;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getDdanbj() {
		return ddanbj;
	}

	public void setDdanbj(String ddanbj) {
		this.ddanbj = ddanbj;
	}

	public String getLxrxm() {
		return lxrxm;
	}

	public void setLxrxm(String lxrxm) {
		this.lxrxm = lxrxm;
	}

	public String getBfrmc() {
		return bfrmc;
	}

	public void setBfrmc(String bfrmc) {
		this.bfrmc = bfrmc;
	}

	public String getBflxmc() {
		return bflxmc;
	}

	public void setBflxmc(String bflxmc) {
		this.bflxmc = bflxmc;
	}

	public String getGtnr() {
		return gtnr;
	}

	public void setGtnr(String gtnr) {
		this.gtnr = gtnr;
	}

	public String getBfsjqs() {
		return bfsjqs;
	}

	public void setBfsjqs(String bfsjqs) {
		this.bfsjqs = bfsjqs;
	}

	public String getDwxzbj() {
		return dwxzbj;
	}

	public void setDwxzbj(String dwxzbj) {
		this.dwxzbj = dwxzbj;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwflmc() {
		return dwflmc;
	}

	public void setDwflmc(String dwflmc) {
		this.dwflmc = dwflmc;
	}

	public String getSfrk() {
		return sfrk;
	}

	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getDwjc() {
		return dwjc;
	}

	public void setDwjc(String dwjc) {
		this.dwjc = dwjc;
	}
}
