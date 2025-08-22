package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Fa_Cards_DetailModel")
public class Fa_Cards_DetailModel {
	private String sID;
	private String sCardID;
	private String sCardNum;
	private String sDeptNum;
	private String lOptID;
	private String dDisposeDate;
	private String iDisposePeriod;
	private String dblValue;
	private String dblDecDeprT;
	private String dblBV;
	private String dblCanWork;
	private String dblTransInDeprTCard;
	private String dblTransOutDeprTCard;
	private String dblDecPreValueT;
	private String dblRetDecPreValueT;
	private String dblBuildArea;
	private String lBuildNum;
	private String lMachinNum;
	private String dblMachinW;
	private String dblAddTax;
	private String dblValueAndTax;

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsCardID() {
		return sCardID;
	}

	public void setsCardID(String sCardID) {
		this.sCardID = sCardID;
	}

	public String getsCardNum() {
		return sCardNum;
	}

	public void setsCardNum(String sCardNum) {
		this.sCardNum = sCardNum;
	}

	public String getsDeptNum() {
		return sDeptNum;
	}

	public void setsDeptNum(String sDeptNum) {
		this.sDeptNum = sDeptNum;
	}

	public String getlOptID() {
		return lOptID;
	}

	public void setlOptID(String lOptID) {
		this.lOptID = lOptID;
	}

	public String getdDisposeDate() {
		return dDisposeDate;
	}

	public void setdDisposeDate(String dDisposeDate) {
		this.dDisposeDate = dDisposeDate;
	}

	public String getiDisposePeriod() {
		return iDisposePeriod;
	}

	public void setiDisposePeriod(String iDisposePeriod) {
		this.iDisposePeriod = iDisposePeriod;
	}

	public String getDblValue() {
		return dblValue;
	}

	public void setDblValue(String dblValue) {
		this.dblValue = dblValue;
	}

	public String getDblDecDeprT() {
		return dblDecDeprT;
	}

	public void setDblDecDeprT(String dblDecDeprT) {
		this.dblDecDeprT = dblDecDeprT;
	}

	public String getDblBV() {
		return dblBV;
	}

	public void setDblBV(String dblBV) {
		this.dblBV = dblBV;
	}

	public String getDblCanWork() {
		return dblCanWork;
	}

	public void setDblCanWork(String dblCanWork) {
		this.dblCanWork = dblCanWork;
	}

	public String getDblTransInDeprTCard() {
		return dblTransInDeprTCard;
	}

	public void setDblTransInDeprTCard(String dblTransInDeprTCard) {
		this.dblTransInDeprTCard = dblTransInDeprTCard;
	}

	public String getDblTransOutDeprTCard() {
		return dblTransOutDeprTCard;
	}

	public void setDblTransOutDeprTCard(String dblTransOutDeprTCard) {
		this.dblTransOutDeprTCard = dblTransOutDeprTCard;
	}

	public String getDblDecPreValueT() {
		return dblDecPreValueT;
	}

	public void setDblDecPreValueT(String dblDecPreValueT) {
		this.dblDecPreValueT = dblDecPreValueT;
	}

	public String getDblRetDecPreValueT() {
		return dblRetDecPreValueT;
	}

	public void setDblRetDecPreValueT(String dblRetDecPreValueT) {
		this.dblRetDecPreValueT = dblRetDecPreValueT;
	}

	public String getDblBuildArea() {
		return dblBuildArea;
	}

	public void setDblBuildArea(String dblBuildArea) {
		this.dblBuildArea = dblBuildArea;
	}

	public String getlBuildNum() {
		return lBuildNum;
	}

	public void setlBuildNum(String lBuildNum) {
		this.lBuildNum = lBuildNum;
	}

	public String getlMachinNum() {
		return lMachinNum;
	}

	public void setlMachinNum(String lMachinNum) {
		this.lMachinNum = lMachinNum;
	}

	public String getDblMachinW() {
		return dblMachinW;
	}

	public void setDblMachinW(String dblMachinW) {
		this.dblMachinW = dblMachinW;
	}

	public String getDblAddTax() {
		return dblAddTax;
	}

	public void setDblAddTax(String dblAddTax) {
		this.dblAddTax = dblAddTax;
	}

	public String getDblValueAndTax() {
		return dblValueAndTax;
	}

	public void setDblValueAndTax(String dblValueAndTax) {
		this.dblValueAndTax = dblValueAndTax;
	}
}
