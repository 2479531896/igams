package com.matridx.igams.production.dao.entities;

/*
 *@date 2022年05月26日14:03
 *
 */
public class Tc_tmp_duplicationModel {
    //1
    private String obj_type;
    //客户代码
    private String code;
    //add
    private String oper;
    //修改前客户代码修改对应
    private String xgqkhdm;

    public String getXgqkhdm() {
        return xgqkhdm;
    }

    public void setXgqkhdm(String xgqkhdm) {
        this.xgqkhdm = xgqkhdm;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getObj_type() {
        return obj_type;
    }

    public void setObj_type(String obj_type) {
        this.obj_type = obj_type;
    }
}
