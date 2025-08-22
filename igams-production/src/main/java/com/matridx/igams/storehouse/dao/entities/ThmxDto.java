package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("ThmxDto")
public class ThmxDto extends ThmxModel{

    //物料名称
    private String wlmc;
    //生产商
    private String scs;
    //原厂货号
    private String ychh;
    //规格
    private String gg;
    //物料id
    private String wlid;
    //可退货数量
    private String kthsl;
    //失效日期
    private String yxq;
    //单位
    private String jldw;
    //物料编码
    private String wlbm;
    //发货单号
    private String fhdh;
    //生产批号
    private String scph;
    //仓库
    private String ck;
    //库位编号
    private String kw;
    //追溯号
    private String zsh;
    //仓库名称
    private String ckmc;
    //库位名称
    private String kwmc;
    //退货ids
    private List<String> thids;
    //u8销售单号
    private String u8xsdh;
    //仓库代码
    private String ckdm;
    //明细关联id
    private String mxglid;
    //保质期flag
    private String bzqflg;
    //保质期
    private String bzq;
    //库位仓库代码
    private String kwckdm;
    //订单序号
    private String ddxh;
    //订单mxid
    private String ddmxid;
    //销售主表关联id
    private String glid;
    //发货id
    private String fhid;
    //发货单u8关联id
    private String fhglid;
    //退货状态
    private String zt;
    //检验单号
    private String jydh;
    //到货检验id
    private String dhjyid;
    //退货单号
    private String thdh;
    //到货id
    private String dhid;

    public String getDhid() {
        return dhid;
    }

    public void setDhid(String dhid) {
        this.dhid = dhid;
    }

    public String getThdh() {
        return thdh;
    }

    public void setThdh(String thdh) {
        this.thdh = thdh;
    }

    public String getDhjyid() {
        return dhjyid;
    }

    public void setDhjyid(String dhjyid) {
        this.dhjyid = dhjyid;
    }

    public String getJydh() {
        return jydh;
    }

    public void setJydh(String jydh) {
        this.jydh = jydh;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getFhglid() {
        return fhglid;
    }

    public void setFhglid(String fhglid) {
        this.fhglid = fhglid;
    }

    public String getFhid() {
        return fhid;
    }

    public void setFhid(String fhid) {
        this.fhid = fhid;
    }

    public String getGlid() {
        return glid;
    }

    public void setGlid(String glid) {
        this.glid = glid;
    }

    public String getDdmxid() {
        return ddmxid;
    }

    public void setDdmxid(String ddmxid) {
        this.ddmxid = ddmxid;
    }

    public String getDdxh() {
        return ddxh;
    }

    public void setDdxh(String ddxh) {
        this.ddxh = ddxh;
    }

    public String getKwckdm() {
        return kwckdm;
    }

    public void setKwckdm(String kwckdm) {
        this.kwckdm = kwckdm;
    }

    public String getBzq() {
        return bzq;
    }

    public void setBzq(String bzq) {
        this.bzq = bzq;
    }

    public String getBzqflg() {
        return bzqflg;
    }

    public void setBzqflg(String bzqflg) {
        this.bzqflg = bzqflg;
    }

    public String getMxglid() {
        return mxglid;
    }

    public void setMxglid(String mxglid) {
        this.mxglid = mxglid;
    }

    public String getCkdm() {
        return ckdm;
    }

    public void setCkdm(String ckdm) {
        this.ckdm = ckdm;
    }

    public String getU8xsdh() {
        return u8xsdh;
    }

    public void setU8xsdh(String u8xsdh) {
        this.u8xsdh = u8xsdh;
    }

    public String getKwmc() {
        return kwmc;
    }

    public void setKwmc(String kwmc) {
        this.kwmc = kwmc;
    }

    public List<String> getThids() {
        return thids;
    }

    public void setThids(List<String> thids) {
        this.thids = thids;
    }

    public String getCkmc() {
        return ckmc;
    }

    public void setCkmc(String ckmc) {
        this.ckmc = ckmc;
    }

    public String getZsh() {
        return zsh;
    }

    public void setZsh(String zsh) {
        this.zsh = zsh;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public String getCk() {
        return ck;
    }

    public void setCk(String ck) {
        this.ck = ck;
    }

    public String getScph() {
        return scph;
    }

    public void setScph(String scph) {
        this.scph = scph;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getFhdh() {
        return fhdh;
    }

    public void setFhdh(String fhdh) {
        this.fhdh = fhdh;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getScrq() {
        return scrq;
    }

    public void setScrq(String scrq) {
        this.scrq = scrq;
    }

    //生产日期
    private String scrq;
    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    public String getYchh() {
        return ychh;
    }

    public void setYchh(String ychh) {
        this.ychh = ychh;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getKthsl() {
        return kthsl;
    }

    public void setKthsl(String kthsl) {
        this.kthsl = kthsl;
    }
}
