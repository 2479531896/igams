package com.matridx.las.home.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.las.home.dao.entities.SysszModel;

@Alias(value="SysszDto")
public class SysszDto extends SysszModel{

	private static final long serialVersionUID = 1L;
	
	// 检测单位名称
	private String jcdwmc;
	// 业务类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;
	// 负责人名称
	private String fzrmc;
	// 附件ID
	private String fjid;
	
	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	
	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

}
