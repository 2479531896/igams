package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjkjxxDto")
public class SjkjxxDto extends SjkjxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 送检单位名称
	private String sjdwmc;
	// 送检单位标记
	private String sjdwbj;
	
	public String getSjdwmc() {
		return sjdwmc;
	}
	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}
	public String getSjdwbj() {
		return sjdwbj;
	}
	public void setSjdwbj(String sjdwbj) {
		this.sjdwbj = sjdwbj;
	}
}
