package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="YsglDto")
public class YsglDto extends YsglModel {

	//部门名称
	private String bmmc;
	//录入人员名称
	private String lrrymc;
	//部门[多]
	private String[] bms;
	//年度[多]
	private String[] nds;
	//明细Json
	private String ysmx_json;
	//预算金额
	private String ysje;
	//科目大类名称
	private String kmdlmc;
	//科目分类名称
	private String kmflmc;
	//统计类型
	private String tjlx;
	//日期开始
	private String rqStart;
	//日期结束
	private String rqEnd;
	//是否部门限制
	private String sfbmxz;
	//权限机构ids
	private List<String> qxjgids;
	private long threadId;

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public List<String> getQxjgids() {
		return qxjgids;
	}

	public void setQxjgids(List<String> qxjgids) {
		this.qxjgids = qxjgids;
	}

	public String getSfbmxz() {
		return sfbmxz;
	}

	public void setSfbmxz(String sfbmxz) {
		this.sfbmxz = sfbmxz;
	}

	public String getRqStart() {
		return rqStart;
	}

	public void setRqStart(String rqStart) {
		this.rqStart = rqStart;
	}

	public String getRqEnd() {
		return rqEnd;
	}

	public void setRqEnd(String rqEnd) {
		this.rqEnd = rqEnd;
	}

	public String getTjlx() {
		return tjlx;
	}

	public void setTjlx(String tjlx) {
		this.tjlx = tjlx;
	}

	public String getYsje() {
		return ysje;
	}

	public void setYsje(String ysje) {
		this.ysje = ysje;
	}

	public String getKmdlmc() {
		return kmdlmc;
	}

	public void setKmdlmc(String kmdlmc) {
		this.kmdlmc = kmdlmc;
	}

	public String getKmflmc() {
		return kmflmc;
	}

	public void setKmflmc(String kmflmc) {
		this.kmflmc = kmflmc;
	}

	public String getYsmx_json() {
		return ysmx_json;
	}

	public void setYsmx_json(String ysmx_json) {
		this.ysmx_json = ysmx_json;
	}

	public String[] getBms() {
		return bms;
	}

	public void setBms(String[] bms) {
		this.bms = bms;
	}

	public String[] getNds() {
		return nds;
	}

	public void setNds(String[] nds) {
		this.nds = nds;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
