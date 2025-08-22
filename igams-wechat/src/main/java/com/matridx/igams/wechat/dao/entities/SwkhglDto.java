package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SwkhglDto")
public class SwkhglDto extends SwkhglModel {
	private String entire;
	private String lxmc;
	private String ywflmc;
	private String dzzqmc;
	private String ksrqstart;
	private String ksrqend;
	private String jsrqstart;
	private String _key; //用于vue前端搜索导出 主键id

	private String jsrqend;
	private String SqlParam;
	private String czbs;//操作标识
	//附件IDS
	private List<String> fjids;
	private List<String> lxids;
	private List<String> ywflids;
	private List<String> dzzqids;
	private String fpxzmc;
	private String fzrmc;

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getFpxzmc() {
		return fpxzmc;
	}

	public void setFpxzmc(String fpxzmc) {
		this.fpxzmc = fpxzmc;
	}

	public String getYwflmc() {
		return ywflmc;
	}

	public void setYwflmc(String ywflmc) {
		this.ywflmc = ywflmc;
	}

	public List<String> getYwflids() {
		return ywflids;
	}

	public void setYwflids(List<String> ywflids) {
		this.ywflids = ywflids;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public List<String> getLxids() {
		return lxids;
	}

	public void setLxids(List<String> lxids) {
		this.lxids = lxids;
	}

	public List<String> getDzzqids() {
		return dzzqids;
	}

	public void setDzzqids(List<String> dzzqids) {
		this.dzzqids = dzzqids;
	}

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getKsrqstart() {
		return ksrqstart;
	}

	public void setKsrqstart(String ksrqstart) {
		this.ksrqstart = ksrqstart;
	}

	public String getKsrqend() {
		return ksrqend;
	}

	public void setKsrqend(String ksrqend) {
		this.ksrqend = ksrqend;
	}

	public String getJsrqstart() {
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart) {
		this.jsrqstart = jsrqstart;
	}

	public String getJsrqend() {
		return jsrqend;
	}

	public void setJsrqend(String jsrqend) {
		this.jsrqend = jsrqend;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getDzzqmc() {
		return dzzqmc;
	}

	public void setDzzqmc(String dzzqmc) {
		this.dzzqmc = dzzqmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
