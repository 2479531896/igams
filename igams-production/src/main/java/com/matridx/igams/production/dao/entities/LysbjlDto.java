package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value ="LysbjlDto")
public class LysbjlDto extends LysbjlModel {
    private static final long serialVersionUID = 1L;
    //记录人
    private String jlrymc;
    //设备编号
    private String sbbh;
    //设备名称
    private String sbmc;
    //编号类别
    private String bhlb;
    //设备编号类别名称
    private String bhlbmc;
    //记录时间开始
    private String jlsjstart;
    //记录时间结束
    private String jlsjend;
    //编号类别多
    private String[] bhlbs;
    //最低温度
    private String zdwd;
    //最高温度
    private String zgwd;
    //设备编号
    private String bhlbdm;
    //设备名称s
    private String[] bhlbmcs;
    //温度状态
    private String wdzt;

    public String getWdzt() {
        return wdzt;
    }

    public void setWdzt(String wdzt) {
        this.wdzt = wdzt;
    }

    public String[] getBhlbmcs() {
        return bhlbmcs;
    }

    public void setBhlbmcs(String[] bhlbmcs) {
        this.bhlbmcs = bhlbmcs;
    }


    public String getBhlbdm() {
        return bhlbdm;
    }

    public void setBhlbdm(String bhlbdm) {
        this.bhlbdm = bhlbdm;
    }

    public String getZdwd() {
        return zdwd;
    }

    public void setZdwd(String zdwd) {
        this.zdwd = zdwd;
    }

    public String getZgwd() {
        return zgwd;
    }

    public void setZgwd(String zgwd) {
        this.zgwd = zgwd;
    }

    public String getJlsjstart() {
        return jlsjstart;
    }

    public void setJlsjstart(String jlsjstart) {
        this.jlsjstart = jlsjstart;
    }

    public String getJlsjend() {
        return jlsjend;
    }

    public void setJlsjend(String jlsjend) {
        this.jlsjend = jlsjend;
    }

    public String[] getBhlbs() {
        return bhlbs;
    }

    public void setBhlbs(String[] bhlbs) {
        this.bhlbs = bhlbs;
    }

    public String getBhlb() {
        return bhlb;
    }

    public void setBhlb(String bhlb) {
        this.bhlb = bhlb;
    }

    public String getBhlbmc() {
        return bhlbmc;
    }

    public void setBhlbmc(String bhlbmc) {
        this.bhlbmc = bhlbmc;
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

    public String getJlrymc() {
        return jlrymc;
    }

    public void setJlrymc(String jlrymc) {
        this.jlrymc = jlrymc;
    }
}

