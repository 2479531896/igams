package com.matridx.igams.hrm.dao.entities;



import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="KsglDto")
public class KsglDto extends KsglModel {
    //选项集合
    private List<KsmxDto> xxlist;
    //题目类型名称
    private String tmlxmc;
    //工作id
    private String gzid;
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
    //题库id
    private String tkid;
    //题库名称
    private String tkmc;
    //答案解析
    private String dajx;
    //ksmxid
    private String ksmxid;
    //数量
    private String sl;
    //分值
    private String fz;
    //总分
    private String zf;
    private List<KsmxDto> ksmxDtos;

    public List<KsmxDto> getKsmxDtos() {
        return ksmxDtos;
    }

    public void setKsmxDtos(List<KsmxDto> ksmxDtos) {
        this.ksmxDtos = ksmxDtos;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getFz() {
        return fz;
    }

    public void setFz(String fz) {
        this.fz = fz;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getKsmxid() {
        return ksmxid;
    }

    public void setKsmxid(String ksmxid) {
        this.ksmxid = ksmxid;
    }

    public String getDajx() {
        return dajx;
    }

    public void setDajx(String dajx) {
        this.dajx = dajx;
    }

    public String getTkmc() {
        return tkmc;
    }

    public void setTkmc(String tkmc) {
        this.tkmc = tkmc;
    }

    public String getTkid() {
        return tkid;
    }

    public void setTkid(String tkid) {
        this.tkid = tkid;
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

    public String getGzid() {
        return gzid;
    }

    public void setGzid(String gzid) {
        this.gzid = gzid;
    }

    public String getTmlxmc() {
        return tmlxmc;
    }

    public void setTmlxmc(String tmlxmc) {
        this.tmlxmc = tmlxmc;
    }

    public List<KsmxDto> getXxlist() {
        return xxlist;
    }

    public void setXxlist(List<KsmxDto> xxlist) {
        this.xxlist = xxlist;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
