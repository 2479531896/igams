package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="KzbmxDto")
public class KzbmxDto extends KzbmxModel{
	//扩增板号
	private String kzbh;
	//提取试剂批号
	private String tqsjph;
	//扩增试剂批号
	private String kzsjph;

	public String getTqsjph() {
		return tqsjph;
	}

	public void setTqsjph(String tqsjph) {
		this.tqsjph = tqsjph;
	}

	public String getKzsjph() {
		return kzsjph;
	}

	public void setKzsjph(String kzsjph) {
		this.kzsjph = kzsjph;
	}

	public String getKzbh() {
		return kzbh;
	}

	public void setKzbh(String kzbh) {
		this.kzbh = kzbh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
