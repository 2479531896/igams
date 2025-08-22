package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="KzglDto")
public class KzglDto extends KzglModel{

	//录入人员名称
	private String lrrymc;
	//修改人员名称
	private String xgrymc;
	//检测单位名称
	private String jcdwmc;
	//检测单位限制
	private List<String> jcdwxz;
	//当天扩增个数
	private String count;
	//内部编号
	private String nbbh;

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getXgrymc() {
		return xgrymc;
	}

	public void setXgrymc(String xgrymc) {
		this.xgrymc = xgrymc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
