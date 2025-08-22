package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="BkyyrqModel")
public class BkyyrqModel extends BaseModel{

	//不可预约日期ID
	private String bkyyrqid;
	//不可预约日期
	private String bkyyrq;
	//不可预约时间段08:00~12:00&13:00~14:00
	private String bkyysjd;
	//备注
	private String bz;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getBkyyrqid() {
		return bkyyrqid;
	}

	public void setBkyyrqid(String bkyyrqid) {
		this.bkyyrqid = bkyyrqid;
	}

	public String getBkyyrq() {
		return bkyyrq;
	}

	public void setBkyyrq(String bkyyrq) {
		this.bkyyrq = bkyyrq;
	}

	public String getBkyysjd() {
		return bkyysjd;
	}

	public void setBkyysjd(String bkyysjd) {
		this.bkyysjd = bkyysjd;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
