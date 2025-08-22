package com.matridx.crf.web.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="NdzxxjlDto")
public class NdzxxjlDto extends NdzxxjlModel{

	private static final long serialVersionUID = 1L;
	private List<NdzdmxqDto> dmxqs;
	private List<NdzshDto> shs;
	private List<NdzyzzbDto> yzzbs;
	private List<NdzxcgDto> xcgs;
	private String kjywjjtzl;

	public String getKjywjjtzl() {
		return kjywjjtzl;
	}

	public void setKjywjjtzl(String kjywjjtzl) {
		this.kjywjjtzl = kjywjjtzl;
	}

	public List<NdzdmxqDto> getDmxqs() {
		return dmxqs;
	}
	public void setDmxqs(List<NdzdmxqDto> dmxqs) {
		this.dmxqs = dmxqs;
	}
	public List<NdzshDto> getShs() {
		return shs;
	}
	public void setShs(List<NdzshDto> shs) {
		this.shs = shs;
	}
	public List<NdzyzzbDto> getYzzbs() {
		return yzzbs;
	}
	public void setYzzbs(List<NdzyzzbDto> yzzbs) {
		this.yzzbs = yzzbs;
	}
	public List<NdzxcgDto> getXcgs() {
		return xcgs;
	}
	public void setXcgs(List<NdzxcgDto> xcgs) {
		this.xcgs = xcgs;
	}

}
