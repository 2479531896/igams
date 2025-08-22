package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("ShfkdjDto")
public class ShfkdjDto extends ShfkdjModel {
    private static final long serialVersionUID = 1L;
    //客户名称
    private String khmc;
    //负责人名称
    private String fzrmc;
    //登记人员
    private String djry;
    //进度
    private String jdmc;
    //登记时间开始
    private String djsjstart;
    //登记时间结束
    private String djsjend;
    //进度多
    private String[] jds;
    private String entire;
 	//备注
    private String bz;
    //是否结束
    private String sfjs;
    //录入时间
    private String lrtime;
    private String entireSignal;
    private String entireAll;
    //领料id
    private String llid;
    //系统用户id
    private String yhid;
    private String emtire;
    //个人或者全部标记
    private String qflb;
    //序列号
    private String xlh;
    //联系人
    private String lxr;
    //仪器名称
    private String yqmc;
    //负责人用户名
    private String fzryhm;
    //负责人ddid
    private String fzrddid;
    //类型名称
    private String lxmc;
    //类型多
    private String[] lxs;

    public String[] getLxs() {
        return lxs;
    }

    public void setLxs(String[] lxs) {
        this.lxs = lxs;
    }

    public String getLxmc() {
        return lxmc;
    }

    public void setLxmc(String lxmc) {
        this.lxmc = lxmc;
    }

    public String getFzryhm() {
        return fzryhm;
    }

    public void setFzryhm(String fzryhm) {
        this.fzryhm = fzryhm;
    }

    public String getFzrddid() {
        return fzrddid;
    }

    public void setFzrddid(String fzrddid) {
        this.fzrddid = fzrddid;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getYqmc() {
        return yqmc;
    }

    public void setYqmc(String yqmc) {
        this.yqmc = yqmc;
    }

    public String getQflb() {
        return qflb;
    }

    public void setQflb(String qflb) {
        this.qflb = qflb;
    }

    public String getEmtire() {
        return emtire;
    }

    public void setEmtire(String emtire) {
        this.emtire = emtire;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getEntireSignal() {
        return entireSignal;
    }

    public void setEntireSignal(String entireSignal) {
        this.entireSignal = entireSignal;
    }

    public String getEntireAll() {
        return entireAll;
    }

    public void setEntireAll(String entireAll) {
        this.entireAll = entireAll;
    }

    public String getLrtime() {
        return lrtime;
    }

    public void setLrtime(String lrtime) {
        this.lrtime = lrtime;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getSfjs() {
        return sfjs;
    }

    public void setSfjs(String sfjs) {
        this.sfjs = sfjs;
    }

    public String[] getJds() {
        return jds;
    }

    public void setJds(String[] jds) {
        this.jds = jds;
    }

    public String getDjsjstart() {
        return djsjstart;
    }

    public void setDjsjstart(String djsjstart) {
        this.djsjstart = djsjstart;
    }

    public String getDjsjend() {
        return djsjend;
    }

    public void setDjsjend(String djsjend) {
        this.djsjend = djsjend;
    }

    public String getJdmc() {
        return jdmc;
    }

    public void setJdmc(String jdmc) {
        this.jdmc = jdmc;
    }

    public String getDjry() {
        return djry;
    }

    public void setDjry(String djry) {
        this.djry = djry;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getFzrmc() {
        return fzrmc;
    }

    public void setFzrmc(String fzrmc) {
        this.fzrmc = fzrmc;
    }
}
