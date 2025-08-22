package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SbpdDto")
public class SbpdDto extends SbpdModel{
	private String sfbmxz;//部门限制
	//盘点人员名称
	private String pdrymc;
	//发起人员名称
	private String fqrymc;
	//部门名称
	private String bmmc;
	//发起时间开始
	private String fqsjstart;
	//发起时间结束
	private String fqsjend;
	//部门[多]
	private String[] bms;
	//状态[多]
	private String[] zts;
	//全部(查询用)
	private String entire;
	//审核通过ids
	private List<String> tgids;
	//盘点明细JSON
	private String pdmx_json;
	//导出
	private String _key;

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

	public String getPdmx_json() {
		return pdmx_json;
	}

	public void setPdmx_json(String pdmx_json) {
		this.pdmx_json = pdmx_json;
	}

	public List<String> getTgids() {
		return tgids;
	}

	public void setTgids(List<String> tgids) {
		this.tgids = tgids;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getFqsjstart() {
		return fqsjstart;
	}

	public void setFqsjstart(String fqsjstart) {
		this.fqsjstart = fqsjstart;
	}

	public String getFqsjend() {
		return fqsjend;
	}

	public void setFqsjend(String fqsjend) {
		this.fqsjend = fqsjend;
	}

	public String[] getBms() {
		return bms;
	}

	public void setBms(String[] bms) {
		this.bms = bms;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String getSfbmxz() {
		return sfbmxz;
	}

	public void setSfbmxz(String sfbmxz) {
		this.sfbmxz = sfbmxz;
	}

	public String getPdrymc() {
		return pdrymc;
	}

	public void setPdrymc(String pdrymc) {
		this.pdrymc = pdrymc;
	}

	public String getFqrymc() {
		return fqrymc;
	}

	public void setFqrymc(String fqrymc) {
		this.fqrymc = fqrymc;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
