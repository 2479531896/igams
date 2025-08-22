package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DdfbsglModel")
public class DdfbsglModel extends BaseModel{
	//审批实例ID
	private String processinstanceid;
	//服务器名
	private String fwqm;
	//处理结果 1为正常，0为异常
	private String cljg;
	//钉钉分布式管理ID
	private String ddfbsglid;
	//业务名称
	private String ywmc;
	//业务类型
	private String ywlx;
	//结束状态,0:未结束，1:结束
	private String jszt;
	//服务器名称
	private String fwqmc;
	//审批业务类型
	private String spywlx;
	//回调地址
	private String hddz;
	//外部程序id
	private String wbcxid;
	
	

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getHddz() {
		return hddz;
	}

	public void setHddz(String hddz) {
		this.hddz = hddz;
	}

	public String getSpywlx() {
		return spywlx;
	}

	public void setSpywlx(String spywlx) {
		this.spywlx = spywlx;
	}

	public String getFwqmc() {
		return fwqmc;
	}


	public void setFwqmc(String fwqmc) {
		this.fwqmc = fwqmc;
	}


	public String getJszt() {
		return jszt;
	}


	public void setJszt(String jszt) {
		this.jszt = jszt;
	}


	public String getYwlx() {
		return ywlx;
	}


	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}


	public String getYwmc() {
		return ywmc;
	}


	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}


	public String getDdfbsglid() {
		return ddfbsglid;
	}


	public void setDdfbsglid(String ddfbsglid) {
		this.ddfbsglid = ddfbsglid;
	}


	public String getProcessinstanceid() {
		return processinstanceid;
	}


	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}


	public String getFwqm() {
		return fwqm;
	}


	public void setFwqm(String fwqm) {
		this.fwqm = fwqm;
	}


	public String getCljg() {
		return cljg;
	}


	public void setCljg(String cljg) {
		this.cljg = cljg;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
