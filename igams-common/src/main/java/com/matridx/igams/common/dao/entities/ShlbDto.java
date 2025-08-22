package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ShlbDto")
public class ShlbDto extends ShlbModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//是否允许取消许可
	private String sfyxqxxk;
	private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getSfyxqxxk() {
		return sfyxqxxk;
	}

	public void setSfyxqxxk(String sfyxqxxk) {
		this.sfyxqxxk = sfyxqxxk;
	}

}
