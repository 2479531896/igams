package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="KzbglDto")
public class KzbglDto extends KzbglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//检测单位限制
	private List<String> jcdwxz;
	//检测单位名称
	private String jcdwmc;
	//当前角色
	private String dqjs;
	//序号集合
	private String[] xhs;
	//标本编号集合
	private String[] ybbhs;
	//坐标（行-列）
	private String[] hls;
	//录入人员名称
	private String lrrymc;
	//样本编号
	private String ybbh;
	//实验号list
	private List<String> syhs;

	public List<String> getSyhs() {
		return syhs;
	}

	public void setSyhs(List<String> syhs) {
		this.syhs = syhs;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String[] getHls() {
		return hls;
	}

	public void setHls(String[] hls) {
		this.hls = hls;
	}

	public String[] getXhs() {
		return xhs;
	}

	public void setXhs(String[] xhs) {
		this.xhs = xhs;
	}

	public String[] getYbbhs() {
		return ybbhs;
	}

	public void setYbbhs(String[] ybbhs) {
		this.ybbhs = ybbhs;
	}

	public String getDqjs() {
		return dqjs;
	}

	public void setDqjs(String dqjs) {
		this.dqjs = dqjs;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}
}
