package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjyczdDto")
public class SjyczdDto extends SjyczdModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//是否置顶
	private String sfzd;

	public String getSfzd() {
		return sfzd;
	}

	public void setSfzd(String sfzd) {
		this.sfzd = sfzd;
	}
	

}
