package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="CsmxxDto")
public class CsmxxDto extends CsmxxModel{

	private static final long serialVersionUID = 1L;

	//采样点名称
	private String cydmc;
	//送检单位名称
	private String sjdwmc;
	//检测单位名称
	private String jcdwmc;
	//用户真实姓名
	private String xm;

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getCydmc() {
		return cydmc;
	}

	public void setCydmc(String cydmc) {
		this.cydmc = cydmc;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}
}
