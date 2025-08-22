package com.matridx.igams.production.dao.entities;

/*
 *@date 2022年05月26日13:55
 *
 */
public class Customer_extradefineModel {
    //客户代码
    private String cCusCode;
    //修改前客户代码修改对应
    private String xgqkhdm;

    public String getXgqkhdm() {
        return xgqkhdm;
    }

    public void setXgqkhdm(String xgqkhdm) {
        this.xgqkhdm = xgqkhdm;
    }


    public String getcCusCode() {
        return cCusCode;
    }

    public void setcCusCode(String cCusCode) {
        this.cCusCode = cCusCode;
    }
}
