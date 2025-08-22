package com.matridx.igams.production.dao.entities;

/*
 *@date 2022年05月26日14:01
 *
 */
public class Sa_invoicecustomersModel {

    //客户代码
    private String cinvoicecompany;
    //客户代码
    private String ccuscode;
    //自增
    private String autoid;
    //1
    private String bdefault;
    //修改前客户代码修改对应
    private String xgqkhdm;

    public String getXgqkhdm() {
        return xgqkhdm;
    }

    public void setXgqkhdm(String xgqkhdm) {
        this.xgqkhdm = xgqkhdm;
    }


    public String getCinvoicecompany() {
        return cinvoicecompany;
    }

    public void setCinvoicecompany(String cinvoicecompany) {
        this.cinvoicecompany = cinvoicecompany;
    }

    public String getCcuscode() {
        return ccuscode;
    }

    public void setCcuscode(String ccuscode) {
        this.ccuscode = ccuscode;
    }

    public String getAutoid() {
        return autoid;
    }

    public void setAutoid(String autoid) {
        this.autoid = autoid;
    }

    public String getBdefault() {
        return bdefault;
    }

    public void setBdefault(String bdefault) {
        this.bdefault = bdefault;
    }
}
