package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="QmngsdmxqDto")
public class QmngsdmxqDto extends QmngsdmxqModel{
	//基础数据CSID
	private String csid;
	//基础数据的参数代码
	private String csdm;
	//基础数据的参数名称
	private String csmc;
	//扩展参数1
	private String cskz1;
	//扩展参数2
	private String cskz2;
	//记录天数
	private String jldjt;
	//患者id
	private String qmngshzid;
	
	
	
	public String getJldjt() {
		return jldjt;
	}
	public void setJldjt(String jldjt) {
		this.jldjt = jldjt;
	}
	public String getQmngshzid() {
		return qmngshzid;
	}
	public void setQmngshzid(String qmngshzid) {
		this.qmngshzid = qmngshzid;
	}
	public String getCskz2() {
		return cskz2;
	}
	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}
	public String getCskz1()
	{
		return cskz1;
	}
	public void setCskz1(String cskz1)
	{
		this.cskz1 = cskz1;
	}
	public String getCsid() {
		return csid;
	}
	public void setCsid(String csid) {
		this.csid = csid;
	}
	public String getCsdm() {
		return csdm;
	}
	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}
	public String getCsmc() {
		return csmc;
	}
	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
