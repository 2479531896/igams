package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="PxtkglbDto")
public class PxtkglbDto extends PxtkglbModel{

    //题库名称
    private String tkmc;
    //总数量
    private String zsl;
    //题目类型名称
    private String tmlxmc;
    //考试ID
    private String ksid;
    //题目内容
    private String tmnr;
    //选项代码
    private String xxdm;
    //选项内容
    private String xxnr;
    //考试明细ID
    private String ksmxid;
    //分数
    private String fs;
   //答案
   private String da;

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getKsid() {
        return ksid;
    }

    public void setKsid(String ksid) {
        this.ksid = ksid;
    }

    public String getTmnr() {
        return tmnr;
    }

    public void setTmnr(String tmnr) {
        this.tmnr = tmnr;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxnr() {
        return xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    public String getKsmxid() {
        return ksmxid;
    }

    public void setKsmxid(String ksmxid) {
        this.ksmxid = ksmxid;
    }

    public String getTmlxmc() {
        return tmlxmc;
    }

    public void setTmlxmc(String tmlxmc) {
        this.tmlxmc = tmlxmc;
    }

    public String getZsl() {
        return zsl;
    }

    public void setZsl(String zsl) {
        this.zsl = zsl;
    }

    public String getTkmc() {
        return tkmc;
    }

    public void setTkmc(String tkmc) {
        this.tkmc = tkmc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
