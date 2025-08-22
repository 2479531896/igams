package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YqxxinfosxDto")
public class YqxxinfosxDto extends YqxxinfosxModel{
	private String csdm;

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
