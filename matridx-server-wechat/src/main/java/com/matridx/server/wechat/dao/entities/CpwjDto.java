package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CpwjDto")
public class CpwjDto extends CpwjModel {
	private String lrrymc;//录入人员名称
	private String entire;//全部
	//附件IDS
	private List<String> fjids;

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
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
