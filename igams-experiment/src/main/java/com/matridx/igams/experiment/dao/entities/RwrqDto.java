package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="RwrqDto")
public class RwrqDto extends RwrqModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//阶段名称
	private String jdmc;
	//工作id
	private String gzid;
	//任务名称
	private String rwmc;
	//处理人员姓名
	private String clryxm;
	//项目ID
	private String xmid;
	//计划开始日期(复数)
	private List<String> jhksrqs;
	//计划结束日期(复数)
	private List<String> jhjsrqs;
	//项目阶段ID(复数)
	private List<String> xmjdids;
	//用户id
	private String yhid;
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	//项目阶段
	private List<List<RwrqDto>> dtosJh;
	//项目阶段
	private List<List<RwrqDto>> dtosSj;

	public String getGzid() {
		return gzid;
	}

	public void setGzid(String gzid) {
		this.gzid = gzid;
	}

	public List<List<RwrqDto>> getDtosJh() {
		return dtosJh;
	}

	public void setDtosJh(List<List<RwrqDto>> dtosJh) {
		this.dtosJh = dtosJh;
	}

	public List<List<RwrqDto>> getDtosSj() {
		return dtosSj;
	}

	public void setDtosSj(List<List<RwrqDto>> dtosSj) {
		this.dtosSj = dtosSj;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getClryxm() {
		return clryxm;
	}

	public void setClryxm(String clryxm) {
		this.clryxm = clryxm;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public String getRwmc() {
		return rwmc;
	}

	public void setRwmc(String rwmc) {
		this.rwmc = rwmc;
	}

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public List<String> getJhksrqs() {
		return jhksrqs;
	}

	public void setJhksrqs(List<String> jhksrqs) {
		this.jhksrqs = jhksrqs;
	}

	public List<String> getJhjsrqs() {
		return jhjsrqs;
	}

	public void setJhjsrqs(List<String> jhjsrqs) {
		this.jhjsrqs = jhjsrqs;
	}

	public List<String> getXmjdids() {
		return xmjdids;
	}

	public void setXmjdids(List<String> xmjdids) {
		this.xmjdids = xmjdids;
	}
	
}
