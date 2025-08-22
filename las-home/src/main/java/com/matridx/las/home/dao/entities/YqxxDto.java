package com.matridx.las.home.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.las.home.dao.entities.YqxxModel;

@Alias(value="YqxxDto")
public class YqxxDto extends YqxxModel{
	
	private static final long serialVersionUID = 1L;

	// 业务类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;
	// 附件ID
	private String fjid;
	
	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
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
}
