package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="BtglDto")
public class BtglDto extends BtglModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lrrymc;//录入人员名称
	private String xgrymc;//修改人员名称
	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getXgrymc() {
		return xgrymc;
	}

	public void setXgrymc(String xgrymc) {
		this.xgrymc = xgrymc;
	}
}
