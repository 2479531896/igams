package com.matridx.las.netty.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="WkglDto")
public class WkglDto extends WkglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//文库日期开始时间
	private String wkrqstart;
	//文库日期结束时间
	private String wkrqend;
	//附件IDS
	private List<String> fjids;

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	
	public String getWkrqstart() {
		return wkrqstart;
	}
	public void setWkrqstart(String wkrqstart) {
		this.wkrqstart = wkrqstart;
	}
	public String getWkrqend() {
		return wkrqend;
	}
	public void setWkrqend(String wkrqend) {
		this.wkrqend = wkrqend;
	}
	
}
