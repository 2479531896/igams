package com.matridx.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="RwglDto")
public class RwglDto extends RwglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//模糊查询
	private String val;
	private String lrrymc;
	private String operate;
	private String newrwmc;
	private String bb;
	private String args;
	private String czbs;

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getBb() {
		return bb;
	}

	public void setBb(String bb) {
		this.bb = bb;
	}

	public String getNewrwmc() {
		return newrwmc;
	}

	public void setNewrwmc(String newrwmc) {
		this.newrwmc = newrwmc;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
}
