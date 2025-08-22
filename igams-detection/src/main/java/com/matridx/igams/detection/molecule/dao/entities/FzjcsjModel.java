package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FzjcsjModel")
public class FzjcsjModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//分子检测结果ID
	private String fzjcsjid;
	//分子项目ID
	private String  fzxmid;
	//仪器类型
	private String yqlx;
	//加热模块类型
	private String jrmklx;
	//参比荧光
	private String cbyg;
	//实验文件名
	private String sywjm;
	//试剂类型
	private String sjlx;
	//结束时间
	private String jssj;
	//反应孔
	private String fyk;
	//基因名称
	private String jymc;
	//报告基因
	private String bgjy;
	//CT值
	private String ctz;
	//标本名
	private String ybm;

	public String getYbm() {
		return ybm;
	}

	public void setYbm(String ybm) {
		this.ybm = ybm;
	}

	public String getFzjcsjid() {
		return fzjcsjid;
	}

	public void setFzjcsjid(String fzjcsjid) {
		this.fzjcsjid = fzjcsjid;
	}

	public String getFzxmid() {
		return fzxmid;
	}

	public void setFzxmid(String fzxmid) {
		this.fzxmid = fzxmid;
	}

	public String getYqlx() {
		return yqlx;
	}

	public void setYqlx(String yqlx) {
		this.yqlx = yqlx;
	}

	public String getJrmklx() {
		return jrmklx;
	}

	public void setJrmklx(String jrmklx) {
		this.jrmklx = jrmklx;
	}

	public String getCbyg() {
		return cbyg;
	}

	public void setCbyg(String cbyg) {
		this.cbyg = cbyg;
	}

	public String getSywjm() {
		return sywjm;
	}

	public void setSywjm(String sywjm) {
		this.sywjm = sywjm;
	}

	public String getSjlx() {
		return sjlx;
	}

	public void setSjlx(String sjlx) {
		this.sjlx = sjlx;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getFyk() {
		return fyk;
	}

	public void setFyk(String fyk) {
		this.fyk = fyk;
	}

	public String getJymc() {
		return jymc;
	}

	public void setJymc(String jymc) {
		this.jymc = jymc;
	}

	public String getBgjy() {
		return bgjy;
	}

	public void setBgjy(String bgjy) {
		this.bgjy = bgjy;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}
}
