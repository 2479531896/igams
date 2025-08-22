package com.matridx.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JxglDto")
public class JxglDto extends JxglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//模糊查询
	private String val;
	private String lrrymc;
	private String xgrymc;
	private String args;
	private String url;
	//附件ids
	private List<String> fjids;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getXgrymc() {
		return xgrymc;
	}

	public void setXgrymc(String xgrymc) {
		this.xgrymc = xgrymc;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}
