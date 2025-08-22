package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="WbtjglDto")
public class WbtjglDto extends WbtjglModel{
	private String jcxmmc;//检测项目名称
	private String jczxmmc;//检测子项目名称
	private String jcdwmc;//检测单位名称
	private String hbmc;//伙伴名称
	private String dwmc;//单位名称
	private String entire;//模糊查询
	private String rqstart;//日期开始时间
	private String rqend;//日期结束时间
	private String sjqfmc;//送检区分名称
	private List<String> sjqfids;//送检区分ids
	private List<String> jcdwids;//检测单位ids
	private List<String> jcxmids;//检测项目ids
	private List<String> jczxmids;//检测子项目ids
	private List<String> sfsfs;//是否收费s

	public String getSjqfmc() {
		return sjqfmc;
	}

	public void setSjqfmc(String sjqfmc) {
		this.sjqfmc = sjqfmc;
	}

	public List<String> getSjqfids() {
		return sjqfids;
	}

	public void setSjqfids(List<String> sjqfids) {
		this.sjqfids = sjqfids;
	}

	public List<String> getSfsfs() {
		return sfsfs;
	}

	public void setSfsfs(List<String> sfsfs) {
		this.sfsfs = sfsfs;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getRqstart() {
		return rqstart;
	}

	public void setRqstart(String rqstart) {
		this.rqstart = rqstart;
	}

	public String getRqend() {
		return rqend;
	}

	public void setRqend(String rqend) {
		this.rqend = rqend;
	}

	public List<String> getJcdwids() {
		return jcdwids;
	}

	public void setJcdwids(List<String> jcdwids) {
		this.jcdwids = jcdwids;
	}

	public List<String> getJcxmids() {
		return jcxmids;
	}

	public void setJcxmids(List<String> jcxmids) {
		this.jcxmids = jcxmids;
	}

	public List<String> getJczxmids() {
		return jczxmids;
	}

	public void setJczxmids(List<String> jczxmids) {
		this.jczxmids = jczxmids;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
