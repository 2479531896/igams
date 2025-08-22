package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="PcrsyglDto")
public class PcrsyglDto extends PcrsyglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//接收开始时间
	private String sysjstart;
	//接收结束时间
	private String sysjend;
	//检测单位限制
	private List<String> jcdwxz;
	//检测单位名称
	private String jcdwmc;
	//高级筛选 检测单位
	private String[] jcdws;
	//高级筛选 是否验证
	private String[] isyzs;
	//样本名称
	private String samplename;

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public String[] getIsyzs() {
		return isyzs;
	}

	public void setIsyzs(String[] isyzs) {
		this.isyzs = isyzs;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
	}

	public String getSysjstart() {
		return sysjstart;
	}

	public void setSysjstart(String sysjstart) {
		this.sysjstart = sysjstart;
	}

	public String getSysjend() {
		return sysjend;
	}

	public void setSysjend(String sysjend) {
		this.sysjend = sysjend;
	}
}
