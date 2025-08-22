package com.matridx.igams.production.dao.entities;

/*
 *@date 2022年05月26日13:47
 *
 */
public class CustomerModel {


    //客户代码
    private String cCusCode;
    //客户名称
    private String cCusName;
    //客户简称
    private String cCusAbbName;
    //省份代码，参数扩展
    private String cCCCode;
    //发展日期
    private String dCusDevDate;
    //客户代码
    private String cCusHeadCode;
    //录入人员
    private String cCreatePerson;
    //修改人员
    private String cModifyPerson;
    //修改时间
    private String dModifyDate;
    //客户代码
    private String iId;
    //客户代码
    private String cInvoiceCompany;
    //币种名称
    private String cCusExch_name;
    //客户代码
    private String cCusCreditCompany;
    //建档日期
    private String dCusCreateDatetime;
    //客户类型代码
    private String cCusMngTypeCode;
    //客户类型
    private String account_type;
    //修改前客户代码修改对应
    private String xgqkhdm;

    public String getXgqkhdm() {
        return xgqkhdm;
    }

    public void setXgqkhdm(String xgqkhdm) {
        this.xgqkhdm = xgqkhdm;
    }

    public String getcCusName() {
        return cCusName;
    }

    public void setcCusName(String cCusName) {
        this.cCusName = cCusName;
    }

    public String getcCusAbbName() {
        return cCusAbbName;
    }

    public void setcCusAbbName(String cCusAbbName) {
        this.cCusAbbName = cCusAbbName;
    }

    public String getcCCCode() {
        return cCCCode;
    }

    public void setcCCCode(String cCCCode) {
        this.cCCCode = cCCCode;
    }

    public String getdCusDevDate() {
        return dCusDevDate;
    }

    public void setdCusDevDate(String dCusDevDate) {
        this.dCusDevDate = dCusDevDate;
    }

    public String getcCusHeadCode() {
        return cCusHeadCode;
    }

    public void setcCusHeadCode(String cCusHeadCode) {
        this.cCusHeadCode = cCusHeadCode;
    }

    public String getcCreatePerson() {
        return cCreatePerson;
    }

    public void setcCreatePerson(String cCreatePerson) {
        this.cCreatePerson = cCreatePerson;
    }

    public String getcModifyPerson() {
        return cModifyPerson;
    }

    public void setcModifyPerson(String cModifyPerson) {
        this.cModifyPerson = cModifyPerson;
    }

    public String getdModifyDate() {
        return dModifyDate;
    }

    public void setdModifyDate(String dModifyDate) {
        this.dModifyDate = dModifyDate;
    }

    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getcInvoiceCompany() {
        return cInvoiceCompany;
    }

    public void setcInvoiceCompany(String cInvoiceCompany) {
        this.cInvoiceCompany = cInvoiceCompany;
    }

    public String getcCusExch_name() {
        return cCusExch_name;
    }

    public void setcCusExch_name(String cCusExch_name) {
        this.cCusExch_name = cCusExch_name;
    }

    public String getcCusCreditCompany() {
        return cCusCreditCompany;
    }

    public void setcCusCreditCompany(String cCusCreditCompany) {
        this.cCusCreditCompany = cCusCreditCompany;
    }

    public String getdCusCreateDatetime() {
        return dCusCreateDatetime;
    }

    public void setdCusCreateDatetime(String dCusCreateDatetime) {
        this.dCusCreateDatetime = dCusCreateDatetime;
    }

    public String getcCusMngTypeCode() {
        return cCusMngTypeCode;
    }

    public void setcCusMngTypeCode(String cCusMngTypeCode) {
        this.cCusMngTypeCode = cCusMngTypeCode;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getcCusCode() {
        return cCusCode;
    }

    public void setcCusCode(String cCusCode) {
        this.cCusCode = cCusCode;
    }
}
