package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "SbtkDto")
public class SbtkDto extends SbtkModel {
    private String sqrmc;//申请人名称
    private String sqbmmc;//申请部门名称
    private String sbmc;//设备名称
    private String sbbh;//设备编号
    private String ggxh;//规格
    private String xlh;//序列号
    private String sydd;//使用地点
    private String entire;//查询
    private String gdzcbh;//固定资产编号
    private String sbccbh;//设备出厂编号
    private String glrymc;//设备管理员
    private String syrymc;//使用人员
    private String bmsbfzrmc;//部门设备负责人
    private String gzsj;//购置时间
    private String sccj;//生产厂家
    private String sccjlxfs;//生产厂家联系方式
    private String xsybmmc;//使用部门
    private String sblxmc;//设备类型名称
    private String[] sblxs;//设备类型多
    private String[] xsybms;//现使用部门多
    private String[] glrys;//管理人员
    private String sqrqstart;//申请日期
    private String sqrqend;//申请日期
    private String xsybm;//使用部门
    private String glry;//管理人员
    private String[] zts;//状态
    private String wlid;//物料id
    private String sbzt;//设备状态
    private String ysbzt;//原设备状态
    private String sfbmxz;//是否部门限制

    public String getSfbmxz() {
        return sfbmxz;
    }

    public void setSfbmxz(String sfbmxz) {
        this.sfbmxz = sfbmxz;
    }

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
    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String[] getZts() {
        return zts;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
    }

    public String getGlry() {
        return glry;
    }

    public void setGlry(String glry) {
        this.glry = glry;
    }

    public String getXsybm() {
        return xsybm;
    }

    public void setXsybm(String xsybm) {
        this.xsybm = xsybm;
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

    public String[] getGlrys() {
        return glrys;
    }

    public void setGlrys(String[] glrys) {
        this.glrys = glrys;
    }

    public String getSqrqstart() {
        return sqrqstart;
    }

    public void setSqrqstart(String sqrqstart) {
        this.sqrqstart = sqrqstart;
    }

    public String getSqrqend() {
        return sqrqend;
    }

    public void setSqrqend(String sqrqend) {
        this.sqrqend = sqrqend;
    }

    public String getSblxmc() {
        return sblxmc;
    }

    public void setSblxmc(String sblxmc) {
        this.sblxmc = sblxmc;
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

    public String getXsybmmc() {
        return xsybmmc;
    }

    public void setXsybmmc(String xsybmmc) {
        this.xsybmmc = xsybmmc;
    }

    public String getGzsj() {
        return gzsj;
    }

    public void setGzsj(String gzsj) {
        this.gzsj = gzsj;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
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

    public String getBmsbfzrmc() {
        return bmsbfzrmc;
    }

    public void setBmsbfzrmc(String bmsbfzrmc) {
        this.bmsbfzrmc = bmsbfzrmc;
    }

    public String getSbbh() {
        return sbbh;
    }

    public void setSbbh(String sbbh) {
        this.sbbh = sbbh;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
    }

    public String getSbmc() {
        return sbmc;
    }

    public void setSbmc(String sbmc) {
        this.sbmc = sbmc;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getSqbmmc() {
        return sqbmmc;
    }

    public void setSqbmmc(String sqbmmc) {
        this.sqbmmc = sqbmmc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
