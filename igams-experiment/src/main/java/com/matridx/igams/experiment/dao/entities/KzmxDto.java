package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="KzmxDto")
public class KzmxDto extends KzmxModel{
	//检测单位 关联基础数据
	private String jcdw;
	//逆转录试剂盒
	private String sjph2;
	//宏基因组DNA建库试剂盒
	private String sjph1;
	//扩增名称
	private String kzmc;
	//扩增时间
	private String kzsj;

	//送检内部编码集合(无后缀)
	private String[] nbbhs;
	//序号集合
	private String[] xhs;
	//提取码(复数)
	private String[] tqms;
	//实验管理ID(复数)
	private String[] syglids;
	//状态
	private String zt;

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String[] getXhs() {
		return xhs;
	}

	public void setXhs(String[] xhs) {
		this.xhs = xhs;
	}

	public String[] getTqms() {
		return tqms;
	}

	public void setTqms(String[] tqms) {
		this.tqms = tqms;
	}

	public String[] getSyglids() {
		return syglids;
	}

	public void setSyglids(String[] syglids) {
		this.syglids = syglids;
	}

	public String[] getNbbhs() {
		return nbbhs;
	}

	public void setNbbhs(String[] nbbhs) {
		this.nbbhs = nbbhs;
	}

	public String getKzsj() {
		return kzsj;
	}

	public void setKzsj(String kzsj) {
		this.kzsj = kzsj;
	}

	public String getKzmc() {
		return kzmc;
	}

	public void setKzmc(String kzmc) {
		this.kzmc = kzmc;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getSjph2() {
		return sjph2;
	}

	public void setSjph2(String sjph2) {
		this.sjph2 = sjph2;
	}

	public String getSjph1() {
		return sjph1;
	}

	public void setSjph1(String sjph1) {
		this.sjph1 = sjph1;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
