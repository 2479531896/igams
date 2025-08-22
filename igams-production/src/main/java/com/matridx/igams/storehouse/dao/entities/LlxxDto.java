package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="LlxxDto")
public class LlxxDto extends LlxxModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String zsxm;
    private String llrq;
    private String jsid;
    private String llrqend;
    private String llrqstart;
    private String entire;
    private String wlmc;
    private String wlbm;
    private String scph;
    private String zsh;
    private String ckmc;
    private String scs;
    private String ychh;
    private String gg;
    private String jldw;
    private String[] cks;
    private String wlid;

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String[] getCks() {
        return cks;
    }

    public void setCks(String[] cks) {
        this.cks = cks;
    }
    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getScph() {
        return scph;
    }

    public void setScph(String scph) {
        this.scph = scph;
    }

    public String getZsh() {
        return zsh;
    }

    public void setZsh(String zsh) {
        this.zsh = zsh;
    }

    public String getCkmc() {
        return ckmc;
    }

    public void setCkmc(String ckmc) {
        this.ckmc = ckmc;
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

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getLlrqend() {
        return llrqend;
    }

    public void setLlrqend(String llrqend) {
        this.llrqend = llrqend;
    }

    public String getLlrqstart() {
        return llrqstart;
    }

    public void setLlrqstart(String llrqstart) {
        this.llrqstart = llrqstart;
    }

    public String getJsid() {
        return jsid;
    }

    public void setJsid(String jsid) {
        this.jsid = jsid;
    }

    public String getLlrq() {
        return llrq;
    }

    public void setLlrq(String llrq) {
        this.llrq = llrq;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }
}
