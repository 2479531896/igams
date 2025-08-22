package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Fa_DeptScaleModel")
public class Fa_DeptScaleModel {
	private String sID;
	private String sCardNum;
	private String lOptID;
	private String sDeptNum;
	private String dblScale;
	private String sDeprSubjectNum;
	private String sDeprSubjectName;
	private String sProjectNum;
	private String sProjectName;
	private String cItemclsId;

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsCardNum() {
		return sCardNum;
	}

	public void setsCardNum(String sCardNum) {
		this.sCardNum = sCardNum;
	}

	public String getlOptID() {
		return lOptID;
	}

	public void setlOptID(String lOptID) {
		this.lOptID = lOptID;
	}

	public String getsDeptNum() {
		return sDeptNum;
	}

	public void setsDeptNum(String sDeptNum) {
		this.sDeptNum = sDeptNum;
	}

	public String getDblScale() {
		return dblScale;
	}

	public void setDblScale(String dblScale) {
		this.dblScale = dblScale;
	}

	public String getsDeprSubjectNum() {
		return sDeprSubjectNum;
	}

	public void setsDeprSubjectNum(String sDeprSubjectNum) {
		this.sDeprSubjectNum = sDeprSubjectNum;
	}

	public String getsDeprSubjectName() {
		return sDeprSubjectName;
	}

	public void setsDeprSubjectName(String sDeprSubjectName) {
		this.sDeprSubjectName = sDeprSubjectName;
	}

	public String getsProjectNum() {
		return sProjectNum;
	}

	public void setsProjectNum(String sProjectNum) {
		this.sProjectNum = sProjectNum;
	}

	public String getsProjectName() {
		return sProjectName;
	}

	public void setsProjectName(String sProjectName) {
		this.sProjectName = sProjectName;
	}

	public String getcItemclsId() {
		return cItemclsId;
	}

	public void setcItemclsId(String cItemclsId) {
		this.cItemclsId = cItemclsId;
	}
}
