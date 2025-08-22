package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "SbyjllDto")
public class SbyjllDto extends SbyjllModel{
    private String sbmc;
    private String sblx;
    private String sblxmc;
    private String ggxh;
    private String xsyr;
    private String entire;
    private String qrlxmc;
    private String qrztmc;
    private String flag;
    private String glryddid;//管理人员ddid
    private String syryddid;//使用人员ddid
    private String syrymc;
    private String syrybmmc;
    private String xsyryhm;
    private String glryyhm;
    private String glry;
    private String signal;
    private String ztmc;
    private String xsybm;//现使用部门
    private String sqbm;
    private String sbbh;//设备编号
    private String xlh;//序列号
    private String mqszbm;//目前所在部门
    private String sbmqwz;//设备目前位置
    private String sbxfzwz;//设备新放置位置
    private String sprjsid;
    private String sprjsmc;
    private String[] sblxs;//设备类型多
    private String[] xsybms;//现使用部门多
    private String[] ysybms;//原使用部门多
    private String yjsjstart;//移交时间开始
    private String yjsjend;//移交时间结束
    private String ysyrmc;//原使用人
    private String ysybmmc;//原使用部门
    private String ybmsbfzrmc;//原设备负责人名称
    private String yymc;//原因
    private String sybmmc;//使用部门名称
    private String bmsbfzrmc;//
    private String glrymc;
    private String yjrmc;//移交人名称
    private String gdzcbh;
    private String sbccbh;
    private String sccj;//生产厂家
    private String sccjlxfs;//生产厂家联系方式
    private String ysrymc;//原始用人
    private String sqbmmc;//申请部门
    private String sqsj;//申请时间
    private String sqrmc;//申请人
    private String lx;//类型
    private String sfbmxz;//部门限制
    private String sblxcskz1;//设备类型参数扩展1
    private String jsrzgmc;//接收人主管名称
    private String jsrzgddid;//接收人主管钉钉id
    private String sbzt;//设备状态
    private String ysbzt;//原设备状态

    public String getSbzt() {
        return sbzt;
    }
    public void setSbzt(String sbzt) {
        this.sbzt = sbzt;
    }

    public String getYsbzt() {
        return ysbzt;
    }

    public void setYsbzt(String ysbzt) {
        this.ysbzt = ysbzt;
    }

    public String getJsrzgddid() {
        return jsrzgddid;
    }

    public void setJsrzgddid(String jsrzgddid) {
        this.jsrzgddid = jsrzgddid;
    }

    public String getJsrzgmc() {
        return jsrzgmc;
    }

    public void setJsrzgmc(String jsrzgmc) {
        this.jsrzgmc = jsrzgmc;
    }

    public String getSblxcskz1() {
        return sblxcskz1;
    }

    public void setSblxcskz1(String sblxcskz1) {
        this.sblxcskz1 = sblxcskz1;
    }

    public String getSfbmxz() {
        return sfbmxz;
    }

    public void setSfbmxz(String sfbmxz) {
        this.sfbmxz = sfbmxz;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getSqsj() {
        return sqsj;
    }

    public void setSqsj(String sqsj) {
        this.sqsj = sqsj;
    }

    public String getSqbmmc() {
        return sqbmmc;
    }

    public void setSqbmmc(String sqbmmc) {
        this.sqbmmc = sqbmmc;
    }

    public String getSccj() {
        return sccj;
    }

    public void setSccj(String sccj) {
        this.sccj = sccj;
    }

    public String getSccjlxfs() {
        return sccjlxfs;
    }

    public void setSccjlxfs(String sccjlxfs) {
        this.sccjlxfs = sccjlxfs;
    }

    public String getYsrymc() {
        return ysrymc;
    }

    public void setYsrymc(String ysrymc) {
        this.ysrymc = ysrymc;
    }

    public String getGdzcbh() {
        return gdzcbh;
    }

    public void setGdzcbh(String gdzcbh) {
        this.gdzcbh = gdzcbh;
    }

    public String getSbccbh() {
        return sbccbh;
    }

    public void setSbccbh(String sbccbh) {
        this.sbccbh = sbccbh;
    }

    public String getYjrmc() {
        return yjrmc;
    }

    public void setYjrmc(String yjrmc) {
        this.yjrmc = yjrmc;
    }

    public String getGlrymc() {
        return glrymc;
    }

    public void setGlrymc(String glrymc) {
        this.glrymc = glrymc;
    }

    public String getBmsbfzrmc() {
        return bmsbfzrmc;
    }

    public void setBmsbfzrmc(String bmsbfzrmc) {
        this.bmsbfzrmc = bmsbfzrmc;
    }

    public String getSybmmc() {
        return sybmmc;
    }

    public void setSybmmc(String sybmmc) {
        this.sybmmc = sybmmc;
    }

    public String getYymc() {
        return yymc;
    }

    public void setYymc(String yymc) {
        this.yymc = yymc;
    }

    public String[] getSblxs() {
        return sblxs;
    }

    public void setSblxs(String[] sblxs) {
        this.sblxs = sblxs;
    }

    public String[] getXsybms() {
        return xsybms;
    }

    public void setXsybms(String[] xsybms) {
        this.xsybms = xsybms;
    }

    public String[] getYsybms() {
        return ysybms;
    }

    public void setYsybms(String[] ysybms) {
        this.ysybms = ysybms;
    }

    public String getYjsjstart() {
        return yjsjstart;
    }

    public void setYjsjstart(String yjsjstart) {
        this.yjsjstart = yjsjstart;
    }

    public String getYjsjend() {
        return yjsjend;
    }

    public void setYjsjend(String yjsjend) {
        this.yjsjend = yjsjend;
    }

    public String getYsyrmc() {
        return ysyrmc;
    }

    public void setYsyrmc(String ysyrmc) {
        this.ysyrmc = ysyrmc;
    }

    public String getYsybmmc() {
        return ysybmmc;
    }

    public void setYsybmmc(String ysybmmc) {
        this.ysybmmc = ysybmmc;
    }

    public String getYbmsbfzrmc() {
        return ybmsbfzrmc;
    }

    public void setYbmsbfzrmc(String ybmsbfzrmc) {
        this.ybmsbfzrmc = ybmsbfzrmc;
    }

    public String getSprjsid() {
        return sprjsid;
    }

    public void setSprjsid(String sprjsid) {
        this.sprjsid = sprjsid;
    }

    public String getSprjsmc() {
        return sprjsmc;
    }

    public void setSprjsmc(String sprjsmc) {
        this.sprjsmc = sprjsmc;
    }

    public String getMqszbm() {
        return mqszbm;
    }

    public void setMqszbm(String mqszbm) {
        this.mqszbm = mqszbm;
    }

    public String getSbmqwz() {
        return sbmqwz;
    }

    public void setSbmqwz(String sbmqwz) {
        this.sbmqwz = sbmqwz;
    }

    public String getSbxfzwz() {
        return sbxfzwz;
    }

    public void setSbxfzwz(String sbxfzwz) {
        this.sbxfzwz = sbxfzwz;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getSbbh() {
        return sbbh;
    }

    public void setSbbh(String sbbh) {
        this.sbbh = sbbh;
    }

    public String getSqbm() {
        return sqbm;
    }

    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public String getXsybm() {
        return xsybm;
    }

    public void setXsybm(String xsybm) {
        this.xsybm = xsybm;
    }

    public String getZtmc() {
        return ztmc;
    }

    public void setZtmc(String ztmc) {
        this.ztmc = ztmc;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getGlry() {
        return glry;
    }

    public void setGlry(String glry) {
        this.glry = glry;
    }

    public String getGlryyhm() {
        return glryyhm;
    }

    public void setGlryyhm(String glryyhm) {
        this.glryyhm = glryyhm;
    }

    public String getXsyryhm() {
        return xsyryhm;
    }

    public void setXsyryhm(String xsyryhm) {
        this.xsyryhm = xsyryhm;
    }

    public String getSyrybmmc() {
        return syrybmmc;
    }

    public void setSyrybmmc(String syrybmmc) {
        this.syrybmmc = syrybmmc;
    }

    public String getSyrymc() {
        return syrymc;
    }

    public void setSyrymc(String syrymc) {
        this.syrymc = syrymc;
    }

    public String getSyryddid() {
        return syryddid;
    }

    public void setSyryddid(String syryddid) {
        this.syryddid = syryddid;
    }

    public String getGlryddid() {
        return glryddid;
    }

    public void setGlryddid(String glryddid) {
        this.glryddid = glryddid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getQrlxmc() {
        return qrlxmc;
    }

    public void setQrlxmc(String qrlxmc) {
        this.qrlxmc = qrlxmc;
    }

    public String getQrztmc() {
        return qrztmc;
    }

    public void setQrztmc(String qrztmc) {
        this.qrztmc = qrztmc;
    }

    public String getSbmc() {
        return sbmc;
    }

    public void setSbmc(String sbmc) {
        this.sbmc = sbmc;
    }

    public String getSblx() {
        return sblx;
    }

    public void setSblx(String sblx) {
        this.sblx = sblx;
    }

    public String getSblxmc() {
        return sblxmc;
    }

    public void setSblxmc(String sblxmc) {
        this.sblxmc = sblxmc;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getXsyr() {
        return xsyr;
    }

    public void setXsyr(String xsyr) {
        this.xsyr = xsyr;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }
}
