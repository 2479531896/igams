package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Fa_ObjectsModel")
public class Fa_ObjectsModel {
	private String iObjectNum;
	private String lMaxID;
	private String sLockedBy;

	public String getiObjectNum() {
		return iObjectNum;
	}

	public void setiObjectNum(String iObjectNum) {
		this.iObjectNum = iObjectNum;
	}

	public String getlMaxID() {
		return lMaxID;
	}

	public void setlMaxID(String lMaxID) {
		this.lMaxID = lMaxID;
	}

	public String getsLockedBy() {
		return sLockedBy;
	}

	public void setsLockedBy(String sLockedBy) {
		this.sLockedBy = sLockedBy;
	}
}
