package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="FzzkjgDto")
public class FzzkjgDto extends FzzkjgModel{

	//通道
	private String td;

	public String getTd() {
		return td;
	}

	public void setTd(String td) {
		this.td = td;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
