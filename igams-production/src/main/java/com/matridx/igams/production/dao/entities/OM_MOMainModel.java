package com.matridx.igams.production.dao.entities;


import org.apache.ibatis.type.Alias;

/*
 *@date 2022年06月30日9:39
 *
 */
@Alias(value="OM_MOMainModel")
public class OM_MOMainModel {
    //ID
    private String MOID;
    //合同单号
    private String cCode;
    //合同日期
    private String dDate;
    //供应商代码
    private String cVenCode;
    //部门代码
    private String cDepCode;
    //采购类型代码
    private String cPTCode;
    //币种名称
    private String cexch_name;
    //汇率
    private String nflat;
    //税率
    private String iTaxRate;
    //备注
    private String cMemo;
    //制单人
    private String cMaker;
    //审核人
    private String cVerifier;
    // 记录编号
    private String cDefine1;
    // 模板号8157
    private String iVTid;
    // 委外加工
    private String cBusType;
    //审核人
    private String cLocker;
    //创建时间
    private String dCreateTime;
    //审核日期
    private String dVerifyDate;
    //审核时间
    private String dVerifyTime;
    //请购单号
    private String csrccode;
    //||ommo|合同单号
    private String csysbarcode;
    private String cState;

    public String getcState() {
        return cState;
    }

    public void setcState(String cState) {
        this.cState = cState;
    }

    public String getMOID() {
        return MOID;
    }

    public void setMOID(String MOID) {
        this.MOID = MOID;
    }

    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    public String getdDate() {
        return dDate;
    }

    public void setdDate(String dDate) {
        this.dDate = dDate;
    }

    public String getcVenCode() {
        return cVenCode;
    }

    public void setcVenCode(String cVenCode) {
        this.cVenCode = cVenCode;
    }

    public String getcDepCode() {
        return cDepCode;
    }

    public void setcDepCode(String cDepCode) {
        this.cDepCode = cDepCode;
    }

    public String getcPTCode() {
        return cPTCode;
    }

    public void setcPTCode(String cPTCode) {
        this.cPTCode = cPTCode;
    }

    public String getCexch_name() {
        return cexch_name;
    }

    public void setCexch_name(String cexch_name) {
        this.cexch_name = cexch_name;
    }

    public String getNflat() {
        return nflat;
    }

    public void setNflat(String nflat) {
        this.nflat = nflat;
    }

    public String getiTaxRate() {
        return iTaxRate;
    }

    public void setiTaxRate(String iTaxRate) {
        this.iTaxRate = iTaxRate;
    }

    public String getcMemo() {
        return cMemo;
    }

    public void setcMemo(String cMemo) {
        this.cMemo = cMemo;
    }

    public String getcMaker() {
        return cMaker;
    }

    public void setcMaker(String cMaker) {
        this.cMaker = cMaker;
    }

    public String getcVerifier() {
        return cVerifier;
    }

    public void setcVerifier(String cVerifier) {
        this.cVerifier = cVerifier;
    }

    public String getcDefine1() {
        return cDefine1;
    }

    public void setcDefine1(String cDefine1) {
        this.cDefine1 = cDefine1;
    }

    public String getiVTid() {
        return iVTid;
    }

    public void setiVTid(String iVTid) {
        this.iVTid = iVTid;
    }

    public String getcBusType() {
        return cBusType;
    }

    public void setcBusType(String cBusType) {
        this.cBusType = cBusType;
    }

    public String getcLocker() {
        return cLocker;
    }

    public void setcLocker(String cLocker) {
        this.cLocker = cLocker;
    }

    public String getdCreateTime() {
        return dCreateTime;
    }

    public void setdCreateTime(String dCreateTime) {
        this.dCreateTime = dCreateTime;
    }

    public String getdVerifyDate() {
        return dVerifyDate;
    }

    public void setdVerifyDate(String dVerifyDate) {
        this.dVerifyDate = dVerifyDate;
    }

    public String getdVerifyTime() {
        return dVerifyTime;
    }

    public void setdVerifyTime(String dVerifyTime) {
        this.dVerifyTime = dVerifyTime;
    }

    public String getCsrccode() {
        return csrccode;
    }

    public void setCsrccode(String csrccode) {
        this.csrccode = csrccode;
    }

    public String getCsysbarcode() {
        return csysbarcode;
    }

    public void setCsysbarcode(String csysbarcode) {
        this.csysbarcode = csysbarcode;
    }
}
