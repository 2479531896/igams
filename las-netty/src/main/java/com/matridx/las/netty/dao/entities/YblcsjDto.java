package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YblcsjDto")
public class YblcsjDto extends YblcsjModel{
	private String jqrztpsjstart;
	private String jqrztpsjend;

	public String getJqrztpsjstart() {
		return jqrztpsjstart;
	}

	public void setJqrztpsjstart(String jqrztpsjstart) {
		this.jqrztpsjstart = jqrztpsjstart;
	}

	public String getJqrztpsjend() {
		return jqrztpsjend;
	}

	public void setJqrztpsjend(String jqrztpsjend) {
		this.jqrztpsjend = jqrztpsjend;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
