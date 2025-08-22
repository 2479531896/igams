package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjyzmxDto")
public class SjyzmxDto extends SjyzmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sfsc;
	public String getSfsc() {
		return sfsc;
	}
	public void setSfsc(String sfsc) {
		this.sfsc = sfsc;
	}
	
}
