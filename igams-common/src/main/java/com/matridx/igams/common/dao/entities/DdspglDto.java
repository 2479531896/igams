package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DdspglDto")
public class DdspglDto extends DdspglModel{
	/**
	 * 审批人名字字段
	 */
	private String staffname;
	//关联单号
	private String gldh;
	//审批类型名称
	private String splxmc;
	//回调地址
	private String hddz;
	

	public String getHddz() {
		return hddz;
	}


	public void setHddz(String hddz) {
		this.hddz = hddz;
	}


	public String getSplxmc() {
		return splxmc;
	}


	public void setSplxmc(String splxmc) {
		this.splxmc = splxmc;
	}


	public String getGldh() {
		return gldh;
	}


	public void setGldh(String gldh) {
		this.gldh = gldh;
	}


	public String getStaffname() {
		return staffname;
	}


	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
