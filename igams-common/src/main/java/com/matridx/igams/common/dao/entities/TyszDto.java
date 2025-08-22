package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="TyszDto")
public class TyszDto extends TyszModel{
	//账套名称
	private String ztmc;
	//资源标题
	private String zybt;
	//一级资源标题
	private String yjzybt;
	//二级资源标题
	private String ejzybt;
	//菜单层次
	private String cdcc;
	//父节点
	private String fjd;
	//分布式前缀
	private String fbsqz;
	//明细json
	private String szmx_json;
	//账套cskz2
	private String ztcskz2;
	//操作代码
	private String czdm;
	//操作名称
	private String czmc;
	//验证标记
	private String verifyFlag;
	public List<String> ejzyids;

	public List<String> getEjzyids() {
		return ejzyids;
	}

	public void setEjzyids(List<String> ejzyids) {
		this.ejzyids = ejzyids;
	}

	public String getVerifyFlag() {
		return verifyFlag;
	}

	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public String getCzdm() {
		return czdm;
	}

	public void setCzdm(String czdm) {
		this.czdm = czdm;
	}

	public String getCzmc() {
		return czmc;
	}

	public void setCzmc(String czmc) {
		this.czmc = czmc;
	}

	public String getFjd() {
		return fjd;
	}

	public void setFjd(String fjd) {
		this.fjd = fjd;
	}

	public String getZtcskz2() {
		return ztcskz2;
	}

	public void setZtcskz2(String ztcskz2) {
		this.ztcskz2 = ztcskz2;
	}

	public String getSzmx_json() {
		return szmx_json;
	}

	public void setSzmx_json(String szmx_json) {
		this.szmx_json = szmx_json;
	}

	public String getFbsqz() {
		return fbsqz;
	}

	public void setFbsqz(String fbsqz) {
		this.fbsqz = fbsqz;
	}

	public String getCdcc() {
		return cdcc;
	}

	public void setCdcc(String cdcc) {
		this.cdcc = cdcc;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getZybt() {
		return zybt;
	}

	public void setZybt(String zybt) {
		this.zybt = zybt;
	}

	public String getYjzybt() {
		return yjzybt;
	}

	public void setYjzybt(String yjzybt) {
		this.yjzybt = yjzybt;
	}

	public String getEjzybt() {
		return ejzybt;
	}

	public void setEjzybt(String ejzybt) {
		this.ejzybt = ejzybt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
