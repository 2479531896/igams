package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="GrbjDto")
public class GrbjDto extends GrbjModel{
	private String rqM;//日期月

	public String getRqM() {
		return rqM;
	}

	public void setRqM(String rqM) {
		this.rqM = rqM;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
