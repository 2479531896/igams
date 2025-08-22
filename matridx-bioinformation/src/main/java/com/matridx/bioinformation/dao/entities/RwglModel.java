package com.matridx.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RwglModel")
public class RwglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rwid;
	private String rwmc;
	private String rwzt;
	private String jxid;
	private String jxmc;
	private String sj;
	private String uri;
	private String image;
	private String docker;
	private String inspect;
	private String logs;

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	public String getInspect() {
		return inspect;
	}

	public void setInspect(String inspect) {
		this.inspect = inspect;
	}

	public String getDocker() {
		return docker;
	}

	public void setDocker(String docker) {
		this.docker = docker;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getJxmc() {
		return jxmc;
	}

	public void setJxmc(String jxmc) {
		this.jxmc = jxmc;
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getRwid() {
		return rwid;
	}

	public void setRwid(String rwid) {
		this.rwid = rwid;
	}

	public String getRwmc() {
		return rwmc;
	}

	public void setRwmc(String rwmc) {
		this.rwmc = rwmc;
	}

	public String getRwzt() {
		return rwzt;
	}

	public void setRwzt(String rwzt) {
		this.rwzt = rwzt;
	}

	public String getJxid() {
		return jxid;
	}

	public void setJxid(String jxid) {
		this.jxid = jxid;
	}
}
