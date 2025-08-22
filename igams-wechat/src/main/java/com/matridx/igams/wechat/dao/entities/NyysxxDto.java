package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="NyysxxDto")
public class NyysxxDto extends NyysxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tbjy;

	private String tbjg;

	private String nyx;

	private String tbsd;

	private String tbpl;

	private String bjtb;

	private String wkbh;
	private String sfhb;//是否汇报

	public String getSfhb() {
		return sfhb;
	}

	public void setSfhb(String sfhb) {
		this.sfhb = sfhb;
	}

	public String getTbjy() {
		return tbjy;
	}

	public void setTbjy(String tbjy) {
		this.tbjy = tbjy;
	}

	public String getTbjg() {
		return tbjg;
	}

	public void setTbjg(String tbjg) {
		this.tbjg = tbjg;
	}

	public String getNyx() {
		return nyx;
	}

	public void setNyx(String nyx) {
		this.nyx = nyx;
	}

	public String getTbsd() {
		return tbsd;
	}

	public void setTbsd(String tbsd) {
		this.tbsd = tbsd;
	}

	public String getTbpl() {
		return tbpl;
	}

	public void setTbpl(String tbpl) {
		this.tbpl = tbpl;
	}

	public String getBjtb() {
		return bjtb;
	}

	public void setBjtb(String bjtb) {
		this.bjtb = bjtb;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}
}
