package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value="CpjgDto")
public class CpjgDto extends CpjgModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//bom类别名称
	private String lbmc;
	//bom类别名称
	private String lbdm;
	//grouping
	private String grouping;
	//失效时间
	private String sxsj;
	//版本开始日期
	private String bbrqstart;
	//版本结束日期
	private String bbrqend;
	//查询全部
	private String entire;
	//bom类别多
	private String[] boms;
	private String cpjgmx_json;
	//当前物料id
	private String nowwlid;

	public String getNowwlid() {
		return nowwlid;
	}

	public void setNowwlid(String nowwlid) {
		this.nowwlid = nowwlid;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public String getSxsj() {
		return sxsj;
	}

	public void setSxsj(String sxsj) {
		this.sxsj = sxsj;
	}

	public String getLbdm() {
		return lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getCpjgmx_json() {
		return cpjgmx_json;
	}

	public void setCpjgmx_json(String cpjgmx_json) {
		this.cpjgmx_json = cpjgmx_json;
	}
	public String[] getBoms() {
		return boms;
	}

	public void setBoms(String[] boms) {
		this.boms = boms;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getBbrqstart() {
		return bbrqstart;
	}

	public void setBbrqstart(String bbrqstart) {
		this.bbrqstart = bbrqstart;
	}

	public String getBbrqend() {
		return bbrqend;
	}

	public void setBbrqend(String bbrqend) {
		this.bbrqend = bbrqend;
	}

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
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
}
