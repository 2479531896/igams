package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="NyjyzsglDto")
public class NyjyzsglDto extends NyjyzsglModel{
	private List<String> mcs;

	public List<String> getMcs() {
		return mcs;
	}

	public void setMcs(List<String> mcs) {
		this.mcs = mcs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
