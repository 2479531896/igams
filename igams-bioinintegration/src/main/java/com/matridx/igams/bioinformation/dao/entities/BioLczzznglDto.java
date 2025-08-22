package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value = "BioLczzznglDto")
public class BioLczzznglDto extends BioLczzznglModel {
	private String id;
	private String detail;
	private String species;
	private String last_update;
	private String yblxmc;
	private String nlzmc;
	private String xbmc;

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getNlzmc() {
		return nlzmc;
	}

	public void setNlzmc(String nlzmc) {
		this.nlzmc = nlzmc;
	}

	public String getXbmc() {
		return xbmc;
	}

	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getLast_update() {
		return last_update;
	}

	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	private static final long serialVersionUID = 1L;


}
