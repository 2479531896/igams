package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbyzDto")
public class SbyzDto extends SbyzModel{
    private String sbbh;//设备编号
    private String entire;//模糊查询的全部
    private String sbmc;//设备名称
    private String gdzcbh;//固定资产编号
    private String ggxh;//规格型号
    private String sbccbh;//设备出厂编号
    private String xlh;//序列号
    private String glrymc;//管理人员
    private String sydd;//使用地点
    private String sbbmfzrmc;//设备部门负责人
    private String sqrmc;//申请人
    private String sqbmmc;//申请部门
    private String[] sblxs;//设备类型多
    private String[] xsybms;//使用部门多
    private String[] yzzqs;//验证周期多
    private String[] glrys;//管理人员多
    private String[] zts;//状态多
    private String qwwcsjstart;//期望完成日期开始
    private String qwwcsjend;//期望完成日期结束
    private String sqrqstart;//申请日期开始
    private String sqrqend;//申请日期结束
    private String yzrqstart;//验证日期开始
    private String yzrqend;//验证日期结束
    private String sblxmc;//设备类型名称
    private String xsyrmc;//使用人员名称
    private String xsybmmc;//使用部门名称
    private String yzzq;//计量周期
    private String xcjlsj;//下次计量时间
    private String wlid;//物料id
    private String lrrymc;//录入人员名称
    private String xgrymc;//修改人员名称
    private String yjwcsjstart;//预计完成时间开始
    private String yjwcsjend;//预计完成时间结束
    private String sccjlxfs;//生产厂家联系方式
    private String jlrqstart;
    private String jlrqend;
    private String xcyzsj;//下次验证时间
    private String sccj;//生产厂家
    private String xsybm;//现使用部门
    private String glry;
    private String sfbmxz;//部门限制
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

    public String getSccj() {
        return sccj;
    }

    public void setSccj(String sccj) {
        this.sccj = sccj;
    }

    public String getXcyzsj() {
        return xcyzsj;
    }

    public void setXcyzsj(String xcyzsj) {
        this.xcyzsj = xcyzsj;
    }

    public String getJlrqstart() {
        return jlrqstart;
    }

    public void setJlrqstart(String jlrqstart) {
        this.jlrqstart = jlrqstart;
    }

    public String getJlrqend() {
        return jlrqend;
    }

    public void setJlrqend(String jlrqend) {
        this.jlrqend = jlrqend;
    }

    public String getSbbh() {
        return sbbh;
    }

    public void setSbbh(String sbbh) {
        this.sbbh = sbbh;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getSbmc() {
        return sbmc;
    }

    public void setSbmc(String sbmc) {
        this.sbmc = sbmc;
    }

    public String getGdzcbh() {
        return gdzcbh;
    }

    public void setGdzcbh(String gdzcbh) {
        this.gdzcbh = gdzcbh;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getSbccbh() {
        return sbccbh;
    }

    public void setSbccbh(String sbccbh) {
        this.sbccbh = sbccbh;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getGlrymc() {
        return glrymc;
    }

    public void setGlrymc(String glrymc) {
        this.glrymc = glrymc;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
    }

    public String getSbbmfzrmc() {
        return sbbmfzrmc;
    }

    public void setSbbmfzrmc(String sbbmfzrmc) {
        this.sbbmfzrmc = sbbmfzrmc;
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

    public String[] getYzzqs() {
        return yzzqs;
    }

    public void setYzzqs(String[] yzzqs) {
        this.yzzqs = yzzqs;
    }

    public String[] getGlrys() {
        return glrys;
    }

    public void setGlrys(String[] glrys) {
        this.glrys = glrys;
    }

    public String[] getZts() {
        return zts;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
    }

    public String getQwwcsjstart() {
        return qwwcsjstart;
    }

    public void setQwwcsjstart(String qwwcsjstart) {
        this.qwwcsjstart = qwwcsjstart;
    }

    public String getQwwcsjend() {
        return qwwcsjend;
    }

    public void setQwwcsjend(String qwwcsjend) {
        this.qwwcsjend = qwwcsjend;
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

    public String getYzrqstart() {
        return yzrqstart;
    }

    public void setYzrqstart(String yzrqstart) {
        this.yzrqstart = yzrqstart;
    }

    public String getYzrqend() {
        return yzrqend;
    }

    public void setYzrqend(String yzrqend) {
        this.yzrqend = yzrqend;
    }

    public String getSblxmc() {
        return sblxmc;
    }

    public void setSblxmc(String sblxmc) {
        this.sblxmc = sblxmc;
    }

    public String getXsyrmc() {
        return xsyrmc;
    }

    public void setXsyrmc(String xsyrmc) {
        this.xsyrmc = xsyrmc;
    }

    public String getXsybmmc() {
        return xsybmmc;
    }

    public void setXsybmmc(String xsybmmc) {
        this.xsybmmc = xsybmmc;
    }

    public String getYzzq() {
        return yzzq;
    }

    public void setYzzq(String yzzq) {
        this.yzzq = yzzq;
    }

    public String getXcjlsj() {
        return xcjlsj;
    }

    public void setXcjlsj(String xcjlsj) {
        this.xcjlsj = xcjlsj;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getXgrymc() {
        return xgrymc;
    }

    public void setXgrymc(String xgrymc) {
        this.xgrymc = xgrymc;
    }

    public String getYjwcsjstart() {
        return yjwcsjstart;
    }

    public void setYjwcsjstart(String yjwcsjstart) {
        this.yjwcsjstart = yjwcsjstart;
    }

    public String getYjwcsjend() {
        return yjwcsjend;
    }

    public void setYjwcsjend(String yjwcsjend) {
        this.yjwcsjend = yjwcsjend;
    }

    public String getSccjlxfs() {
        return sccjlxfs;
    }

    public void setSccjlxfs(String sccjlxfs) {
        this.sccjlxfs = sccjlxfs;
    }
}
