package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbbfDto")
public class SbbfDto extends SbbfModel{
    private String sqrmc;//申请人名称
    private String sbbh;//设备编号
    private String sbmc;//设备名称
    private String xlh;//序列号
    private String sfgdzcmc;//是否固定资产
    private String gdzcbh;//固定资产编号
    private String fzwz;//放置位置
    private String ggxh;//规格型号
    private String sprjsid;
    private String sprjsmc;
    private String sbccbh;//设备出场编号
    private String sccj;//生产厂家
    private String glrymc;//管理人员
    private String syrymc;//使用人员
    private String sybmmc;//使用部门
    private String sydd;//使用地点
    private String bmsbfzrmc;//部门设备负责人
    private String sqbmmc;//申请部门
    private String entire;
    private String[] sblxs;
    private String[] sybms;
    private String[] glrys;
    private String sqsjstart;
    private String sqsjend;
    private String glry;//管理人员
    private String sblxmc;//设备类型名称
    private String[] zts;//状态多
    private String xsybm;//现使用部门
    private String xsybmmc;//现使用部门名称
    private String sfbmxz;
    private String ysbzt;//原设备状态
    private String sbzt;//设备状态

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
    public String getSfbmxz() {
        return sfbmxz;
    }

    public void setSfbmxz(String sfbmxz) {
        this.sfbmxz = sfbmxz;
    }

    public String getXsybm() {
        return xsybm;
    }

    public void setXsybm(String xsybm) {
        this.xsybm = xsybm;
    }

    public String getXsybmmc() {
        return xsybmmc;
    }

    public void setXsybmmc(String xsybmmc) {
        this.xsybmmc = xsybmmc;
    }

    public String[] getZts() {
        return zts;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
    }

    public String getSblxmc() {
        return sblxmc;
    }

    public void setSblxmc(String sblxmc) {
        this.sblxmc = sblxmc;
    }

    public String getGlry() {
        return glry;
    }

    public void setGlry(String glry) {
        this.glry = glry;
    }

    public String getSbccbh() {
        return sbccbh;
    }

    public void setSbccbh(String sbccbh) {
        this.sbccbh = sbccbh;
    }

    public String getSccj() {
        return sccj;
    }

    public void setSccj(String sccj) {
        this.sccj = sccj;
    }

    public String getGlrymc() {
        return glrymc;
    }

    public void setGlrymc(String glrymc) {
        this.glrymc = glrymc;
    }

    public String getSyrymc() {
        return syrymc;
    }

    public void setSyrymc(String syrymc) {
        this.syrymc = syrymc;
    }

    public String getSybmmc() {
        return sybmmc;
    }

    public void setSybmmc(String sybmmc) {
        this.sybmmc = sybmmc;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
    }

    public String getBmsbfzrmc() {
        return bmsbfzrmc;
    }

    public void setBmsbfzrmc(String bmsbfzrmc) {
        this.bmsbfzrmc = bmsbfzrmc;
    }

    public String getSqbmmc() {
        return sqbmmc;
    }

    public void setSqbmmc(String sqbmmc) {
        this.sqbmmc = sqbmmc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getSblxs() {
        return sblxs;
    }

    public void setSblxs(String[] sblxs) {
        this.sblxs = sblxs;
    }

    public String[] getSybms() {
        return sybms;
    }

    public void setSybms(String[] sybms) {
        this.sybms = sybms;
    }

    public String[] getGlrys() {
        return glrys;
    }

    public void setGlrys(String[] glrys) {
        this.glrys = glrys;
    }

    @Override
    public String getSqsjstart() {
        return sqsjstart;
    }

    @Override
    public void setSqsjstart(String sqsjstart) {
        this.sqsjstart = sqsjstart;
    }

    @Override
    public String getSqsjend() {
        return sqsjend;
    }

    @Override
    public void setSqsjend(String sqsjend) {
        this.sqsjend = sqsjend;
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

    public String getSfgdzcmc() {
        return sfgdzcmc;
    }

    public void setSfgdzcmc(String sfgdzcmc) {
        this.sfgdzcmc = sfgdzcmc;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getSbbh() {
        return sbbh;
    }

    public void setSbbh(String sbbh) {
        this.sbbh = sbbh;
    }

    public String getSbmc() {
        return sbmc;
    }

    public void setSbmc(String sbmc) {
        this.sbmc = sbmc;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }



    public String getGdzcbh() {
        return gdzcbh;
    }

    public void setGdzcbh(String gdzcbh) {
        this.gdzcbh = gdzcbh;
    }

    public String getFzwz() {
        return fzwz;
    }

    public void setFzwz(String fzwz) {
        this.fzwz = fzwz;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }
}
