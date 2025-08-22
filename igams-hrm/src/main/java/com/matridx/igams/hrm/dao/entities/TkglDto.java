package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="TkglDto")
public class TkglDto extends TkglModel {

    //单选题数
    private String danxts;
    //多选题数
    private String duoxts;
    //简答题数
    private String jdts;
    //判断题数
    private String pdts;
    //填空题数
    private String tkts;
    private String tmmx_json;
    //选项A
    private String xxA;
    //选项B
    private String xxB;
    //选项C
    private String xxC;
    //选项D
    private String xxD;
    //选项E
    private String xxE;
    //选项F
    private String xxF;
    //选项G
    private String xxG;
    //选项H
    private String xxH;
    //题目类型
    private String tmlx;
    //题目内容
    private String tmnr;
    //分数
    private String fs;
    //选项代码
    private String xxdm;
    //选项内容
    private String xxnr;
    //序号
    private String xh;
    //答案
    private String da;
    //答案解析
    private String dajx;
    //是否更新分数
    private String sfgx;
    //修改前题库
    private String xgqtk;
    //所属公司多
    private String[] ssgss;
    //所属公司
    private String ssgsmc;

    public String getSsgsmc() {
        return ssgsmc;
    }

    public void setSsgsmc(String ssgsmc) {
        this.ssgsmc = ssgsmc;
    }

    public String[] getSsgss() {
        return ssgss;
    }

    public void setSsgss(String[] ssgss) {
        this.ssgss = ssgss;
    }

    public String getXgqtk() {
        return xgqtk;
    }

    public void setXgqtk(String xgqtk) {
        this.xgqtk = xgqtk;
    }

    public String getSfgx() {
        return sfgx;
    }

    public void setSfgx(String sfgx) {
        this.sfgx = sfgx;
    }

    public String getDajx() {
        return dajx;
    }

    public void setDajx(String dajx) {
        this.dajx = dajx;
    }

    public String getPdts() {
        return pdts;
    }

    public void setPdts(String pdts) {
        this.pdts = pdts;
    }

    public String getTkts() {
        return tkts;
    }

    public void setTkts(String tkts) {
        this.tkts = tkts;
    }

    public String getXxA() {
        return xxA;
    }

    public void setXxA(String xxA) {
        this.xxA = xxA;
    }

    public String getXxB() {
        return xxB;
    }

    public void setXxB(String xxB) {
        this.xxB = xxB;
    }

    public String getXxC() {
        return xxC;
    }

    public void setXxC(String xxC) {
        this.xxC = xxC;
    }

    public String getXxD() {
        return xxD;
    }

    public void setXxD(String xxD) {
        this.xxD = xxD;
    }

    public String getXxE() {
        return xxE;
    }

    public void setXxE(String xxE) {
        this.xxE = xxE;
    }

    public String getXxF() {
        return xxF;
    }

    public void setXxF(String xxF) {
        this.xxF = xxF;
    }

    public String getXxG() {
        return xxG;
    }

    public void setXxG(String xxG) {
        this.xxG = xxG;
    }

    public String getXxH() {
        return xxH;
    }

    public void setXxH(String xxH) {
        this.xxH = xxH;
    }

    public String getTmlx() {
        return tmlx;
    }

    public void setTmlx(String tmlx) {
        this.tmlx = tmlx;
    }

    public String getTmnr() {
        return tmnr;
    }

    public void setTmnr(String tmnr) {
        this.tmnr = tmnr;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
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

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getTmmx_json() {
        return tmmx_json;
    }

    public void setTmmx_json(String tmmx_json) {
        this.tmmx_json = tmmx_json;
    }

    public String getDanxts() {
        return danxts;
    }

    public void setDanxts(String danxts) {
        this.danxts = danxts;
    }

    public String getDuoxts() {
        return duoxts;
    }

    public void setDuoxts(String duoxts) {
        this.duoxts = duoxts;
    }

    public String getJdts() {
        return jdts;
    }

    public void setJdts(String jdts) {
        this.jdts = jdts;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
