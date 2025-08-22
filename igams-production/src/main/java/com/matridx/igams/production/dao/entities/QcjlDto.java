package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="QcjlDto")
public class QcjlDto extends QcjlModel {

	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//操作人名称
	private String czrmc;
	//检查人名称
	private String jcrmc;
	//全部(查询条件)
	private String entire;
	//清场日期开始
	private String qcrqstart;
	//清场日期结束
	private String qcrqend;
	//审核类别
	private String auditType;
	//项目Json
	private String qcxm_json;
	//规格
	private String gg;

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getQcxm_json() {
		return qcxm_json;
	}

	public void setQcxm_json(String qcxm_json) {
		this.qcxm_json = qcxm_json;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getQcrqstart() {
		return qcrqstart;
	}

	public void setQcrqstart(String qcrqstart) {
		this.qcrqstart = qcrqstart;
	}

	public String getQcrqend() {
		return qcrqend;
	}

	public void setQcrqend(String qcrqend) {
		this.qcrqend = qcrqend;
	}

	public String getCzrmc() {
		return czrmc;
	}

	public void setCzrmc(String czrmc) {
		this.czrmc = czrmc;
	}

	public String getJcrmc() {
		return jcrmc;
	}

	public void setJcrmc(String jcrmc) {
		this.jcrmc = jcrmc;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
