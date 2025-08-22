package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "XgwxModel")
public class XgwxModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String wxid;
    private String zz;
    private String bt;
    private String qk;
    private String taxid;
    private String yblx;
    private String mrwx;


    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getZz() {
        return zz;
    }

    public void setZz(String zz) {
        this.zz = zz;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getQk() {
        return qk;
    }

    public void setQk(String qk) {
        this.qk = qk;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getMrwx() {
        return mrwx;
    }

    public void setMrwx(String mrwx) {
        this.mrwx = mrwx;
    }
}
