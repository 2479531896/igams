package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JkdymxDto")
public class JkdymxDto extends JkdymxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//全部(查询条件)
	private String entire;

	private List<String> dydzs;

	public List<String> getDydzs() {
		return dydzs;
	}

	public void setDydzs(List<String> dydzs) {
		this.dydzs = dydzs;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}
}
