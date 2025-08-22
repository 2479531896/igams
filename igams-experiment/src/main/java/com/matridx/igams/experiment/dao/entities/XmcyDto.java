package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XmcyDto")
public class XmcyDto extends XmcyModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户ID
	private String yhid;
	//用户真实姓名
	private String zsxm;
	private String yhm;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	
}
