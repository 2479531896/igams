package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Fa_CardsModel")
public class Fa_CardsModel {
	private String sCardID;
	private String sCardNum;
	private String sAssetNum;
	private String sAssetName;
	private String lOptID;
	private String iOptType;
	private String iNewType;
	private String sZWVoucherType;
	private String sZWVoucherNum;
	private String sDeptNum;
	private String sTypeNum;
	private String sOrgID;
	private String sOrgAddID;
	private String sOrgDisposeID;
	private String sDisposeReason;
	private String sStatusID;
	private String sDeprMethodID;
	private String sCurrency;
	private String sDeprSubjectNum;
	private String sDeprSubjectName;
	private String sProjectNum;
	private String sProjectName;
	private String sStyle;
	private String dblBuildArea;
	private String lBuildNum;
	private String lMachinNum;
	private String dblMachinW;
	private String sSite;
	private String lLife;
	private String dStartdate;
	private String dInputDate;
	private String dTransDate;
	private String dDisposeDate;
	private String iInputPeriod;
	private String iTransPeriod;
	private String iDisposePeriod;
	private String dblFCValue;
	private String dblExchangeRate;
	private String sOperator;
	private String sDisposer;
	private String dblNetValueAct;
	private String lDeprMonthsAct;
	private String dblCanWorkAct;
	private String dblLegacy;
	private String sVoucherNum;
	private String sEvaluateNum;
	private String dblValue;
	private String dblDecDeprT;
	private String dblDecDepr;
	private String dblDecDeprRate;
	private String lDecDeprMonths;
	private String dblDecWorkT;
	private String dblDecPerDepr;
	private String dblBV;
	private String dblBVRate;
	private String dblCanWork;
	private String dblPerDepr;
	private String lUsedMonths;
	private String sWorkUnit;
	private String dblTransInDeprTCard;
	private String dblTransOutDeprTCard;
	private String iVouchsNextMonthAct;
	private String sTransTypeNum;
	private String sTransDepteNum;
	private String sLockedBy;
	private String dblDecPreValueT;
	private String dblRetDecPreValueT;
	private String bMultiDept;
	private String sDeptNames;
	private String bNewDepring;
	private String sGroupNum;
	private String bSplit;
	private String dblAddTax;
	private String dblValueAndTax;
	private String bFromInv;
	private String sKeeper;
	private String cVenCode;
	private String dPurDate;
	private String sSeriesNum;
	private String sCommodityCode;

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

	public String getsAssetNum() {
		return sAssetNum;
	}

	public void setsAssetNum(String sAssetNum) {
		this.sAssetNum = sAssetNum;
	}

	public String getsAssetName() {
		return sAssetName;
	}

	public void setsAssetName(String sAssetName) {
		this.sAssetName = sAssetName;
	}

	public String getlOptID() {
		return lOptID;
	}

	public void setlOptID(String lOptID) {
		this.lOptID = lOptID;
	}

	public String getiOptType() {
		return iOptType;
	}

	public void setiOptType(String iOptType) {
		this.iOptType = iOptType;
	}

	public String getiNewType() {
		return iNewType;
	}

	public void setiNewType(String iNewType) {
		this.iNewType = iNewType;
	}

	public String getsZWVoucherType() {
		return sZWVoucherType;
	}

	public void setsZWVoucherType(String sZWVoucherType) {
		this.sZWVoucherType = sZWVoucherType;
	}

	public String getsZWVoucherNum() {
		return sZWVoucherNum;
	}

	public void setsZWVoucherNum(String sZWVoucherNum) {
		this.sZWVoucherNum = sZWVoucherNum;
	}

	public String getsDeptNum() {
		return sDeptNum;
	}

	public void setsDeptNum(String sDeptNum) {
		this.sDeptNum = sDeptNum;
	}

	public String getsTypeNum() {
		return sTypeNum;
	}

	public void setsTypeNum(String sTypeNum) {
		this.sTypeNum = sTypeNum;
	}

	public String getsOrgID() {
		return sOrgID;
	}

	public void setsOrgID(String sOrgID) {
		this.sOrgID = sOrgID;
	}

	public String getsOrgAddID() {
		return sOrgAddID;
	}

	public void setsOrgAddID(String sOrgAddID) {
		this.sOrgAddID = sOrgAddID;
	}

	public String getsOrgDisposeID() {
		return sOrgDisposeID;
	}

	public void setsOrgDisposeID(String sOrgDisposeID) {
		this.sOrgDisposeID = sOrgDisposeID;
	}

	public String getsDisposeReason() {
		return sDisposeReason;
	}

	public void setsDisposeReason(String sDisposeReason) {
		this.sDisposeReason = sDisposeReason;
	}

	public String getsStatusID() {
		return sStatusID;
	}

	public void setsStatusID(String sStatusID) {
		this.sStatusID = sStatusID;
	}

	public String getsDeprMethodID() {
		return sDeprMethodID;
	}

	public void setsDeprMethodID(String sDeprMethodID) {
		this.sDeprMethodID = sDeprMethodID;
	}

	public String getsCurrency() {
		return sCurrency;
	}

	public void setsCurrency(String sCurrency) {
		this.sCurrency = sCurrency;
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

	public String getsStyle() {
		return sStyle;
	}

	public void setsStyle(String sStyle) {
		this.sStyle = sStyle;
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

	public String getsSite() {
		return sSite;
	}

	public void setsSite(String sSite) {
		this.sSite = sSite;
	}

	public String getlLife() {
		return lLife;
	}

	public void setlLife(String lLife) {
		this.lLife = lLife;
	}

	public String getdStartdate() {
		return dStartdate;
	}

	public void setdStartdate(String dStartdate) {
		this.dStartdate = dStartdate;
	}

	public String getdInputDate() {
		return dInputDate;
	}

	public void setdInputDate(String dInputDate) {
		this.dInputDate = dInputDate;
	}

	public String getdTransDate() {
		return dTransDate;
	}

	public void setdTransDate(String dTransDate) {
		this.dTransDate = dTransDate;
	}

	public String getdDisposeDate() {
		return dDisposeDate;
	}

	public void setdDisposeDate(String dDisposeDate) {
		this.dDisposeDate = dDisposeDate;
	}

	public String getiInputPeriod() {
		return iInputPeriod;
	}

	public void setiInputPeriod(String iInputPeriod) {
		this.iInputPeriod = iInputPeriod;
	}

	public String getiTransPeriod() {
		return iTransPeriod;
	}

	public void setiTransPeriod(String iTransPeriod) {
		this.iTransPeriod = iTransPeriod;
	}

	public String getiDisposePeriod() {
		return iDisposePeriod;
	}

	public void setiDisposePeriod(String iDisposePeriod) {
		this.iDisposePeriod = iDisposePeriod;
	}

	public String getDblFCValue() {
		return dblFCValue;
	}

	public void setDblFCValue(String dblFCValue) {
		this.dblFCValue = dblFCValue;
	}

	public String getDblExchangeRate() {
		return dblExchangeRate;
	}

	public void setDblExchangeRate(String dblExchangeRate) {
		this.dblExchangeRate = dblExchangeRate;
	}

	public String getsOperator() {
		return sOperator;
	}

	public void setsOperator(String sOperator) {
		this.sOperator = sOperator;
	}

	public String getsDisposer() {
		return sDisposer;
	}

	public void setsDisposer(String sDisposer) {
		this.sDisposer = sDisposer;
	}

	public String getDblNetValueAct() {
		return dblNetValueAct;
	}

	public void setDblNetValueAct(String dblNetValueAct) {
		this.dblNetValueAct = dblNetValueAct;
	}

	public String getlDeprMonthsAct() {
		return lDeprMonthsAct;
	}

	public void setlDeprMonthsAct(String lDeprMonthsAct) {
		this.lDeprMonthsAct = lDeprMonthsAct;
	}

	public String getDblCanWorkAct() {
		return dblCanWorkAct;
	}

	public void setDblCanWorkAct(String dblCanWorkAct) {
		this.dblCanWorkAct = dblCanWorkAct;
	}

	public String getDblLegacy() {
		return dblLegacy;
	}

	public void setDblLegacy(String dblLegacy) {
		this.dblLegacy = dblLegacy;
	}

	public String getsVoucherNum() {
		return sVoucherNum;
	}

	public void setsVoucherNum(String sVoucherNum) {
		this.sVoucherNum = sVoucherNum;
	}

	public String getsEvaluateNum() {
		return sEvaluateNum;
	}

	public void setsEvaluateNum(String sEvaluateNum) {
		this.sEvaluateNum = sEvaluateNum;
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

	public String getDblDecDepr() {
		return dblDecDepr;
	}

	public void setDblDecDepr(String dblDecDepr) {
		this.dblDecDepr = dblDecDepr;
	}

	public String getDblDecDeprRate() {
		return dblDecDeprRate;
	}

	public void setDblDecDeprRate(String dblDecDeprRate) {
		this.dblDecDeprRate = dblDecDeprRate;
	}

	public String getlDecDeprMonths() {
		return lDecDeprMonths;
	}

	public void setlDecDeprMonths(String lDecDeprMonths) {
		this.lDecDeprMonths = lDecDeprMonths;
	}

	public String getDblDecWorkT() {
		return dblDecWorkT;
	}

	public void setDblDecWorkT(String dblDecWorkT) {
		this.dblDecWorkT = dblDecWorkT;
	}

	public String getDblDecPerDepr() {
		return dblDecPerDepr;
	}

	public void setDblDecPerDepr(String dblDecPerDepr) {
		this.dblDecPerDepr = dblDecPerDepr;
	}

	public String getDblBV() {
		return dblBV;
	}

	public void setDblBV(String dblBV) {
		this.dblBV = dblBV;
	}

	public String getDblBVRate() {
		return dblBVRate;
	}

	public void setDblBVRate(String dblBVRate) {
		this.dblBVRate = dblBVRate;
	}

	public String getDblCanWork() {
		return dblCanWork;
	}

	public void setDblCanWork(String dblCanWork) {
		this.dblCanWork = dblCanWork;
	}

	public String getDblPerDepr() {
		return dblPerDepr;
	}

	public void setDblPerDepr(String dblPerDepr) {
		this.dblPerDepr = dblPerDepr;
	}

	public String getlUsedMonths() {
		return lUsedMonths;
	}

	public void setlUsedMonths(String lUsedMonths) {
		this.lUsedMonths = lUsedMonths;
	}

	public String getsWorkUnit() {
		return sWorkUnit;
	}

	public void setsWorkUnit(String sWorkUnit) {
		this.sWorkUnit = sWorkUnit;
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

	public String getiVouchsNextMonthAct() {
		return iVouchsNextMonthAct;
	}

	public void setiVouchsNextMonthAct(String iVouchsNextMonthAct) {
		this.iVouchsNextMonthAct = iVouchsNextMonthAct;
	}

	public String getsTransTypeNum() {
		return sTransTypeNum;
	}

	public void setsTransTypeNum(String sTransTypeNum) {
		this.sTransTypeNum = sTransTypeNum;
	}

	public String getsTransDepteNum() {
		return sTransDepteNum;
	}

	public void setsTransDepteNum(String sTransDepteNum) {
		this.sTransDepteNum = sTransDepteNum;
	}

	public String getsLockedBy() {
		return sLockedBy;
	}

	public void setsLockedBy(String sLockedBy) {
		this.sLockedBy = sLockedBy;
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

	public String getbMultiDept() {
		return bMultiDept;
	}

	public void setbMultiDept(String bMultiDept) {
		this.bMultiDept = bMultiDept;
	}

	public String getsDeptNames() {
		return sDeptNames;
	}

	public void setsDeptNames(String sDeptNames) {
		this.sDeptNames = sDeptNames;
	}

	public String getbNewDepring() {
		return bNewDepring;
	}

	public void setbNewDepring(String bNewDepring) {
		this.bNewDepring = bNewDepring;
	}

	public String getsGroupNum() {
		return sGroupNum;
	}

	public void setsGroupNum(String sGroupNum) {
		this.sGroupNum = sGroupNum;
	}

	public String getbSplit() {
		return bSplit;
	}

	public void setbSplit(String bSplit) {
		this.bSplit = bSplit;
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

	public String getbFromInv() {
		return bFromInv;
	}

	public void setbFromInv(String bFromInv) {
		this.bFromInv = bFromInv;
	}

	public String getcVenCode() {
		return cVenCode;
	}

	public void setcVenCode(String cVenCode) {
		this.cVenCode = cVenCode;
	}

	public String getsKeeper() {
		return sKeeper;
	}

	public void setsKeeper(String sKeeper) {
		this.sKeeper = sKeeper;
	}

	public String getdPurDate() {
		return dPurDate;
	}

	public void setdPurDate(String dPurDate) {
		this.dPurDate = dPurDate;
	}

	public String getsSeriesNum() {
		return sSeriesNum;
	}

	public void setsSeriesNum(String sSeriesNum) {
		this.sSeriesNum = sSeriesNum;
	}

	public String getsCommodityCode() {
		return sCommodityCode;
	}

	public void setsCommodityCode(String sCommodityCode) {
		this.sCommodityCode = sCommodityCode;
	}
}
