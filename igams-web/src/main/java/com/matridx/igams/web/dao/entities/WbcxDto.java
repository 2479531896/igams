package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WbcxDto")
public class WbcxDto extends WbcxModel{

	private String entire;//模糊查询

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
