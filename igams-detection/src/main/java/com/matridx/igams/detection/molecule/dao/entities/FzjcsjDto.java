package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="FzjcsjDto")
public class FzjcsjDto extends FzjcsjModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fzjcid;

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}
}
