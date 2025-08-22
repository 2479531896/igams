package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("PdjlDto")
public class PdjlDto extends PdjlModel {
    private static final long serialVersionUID = 1L;
    //登记人员
    private String djry;

    public String getDjry() {
        return djry;
    }

    public void setDjry(String djry) {
        this.djry = djry;
    }

    //进度
    private String jd;
    //客户名称
    private String khmc;
    //问题描述
    private String wtms;
    //状态名称
    private String ztmc;
    private String entire;

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getZtmc() {
        return ztmc;
    }

    public void setZtmc(String ztmc) {
        this.ztmc = ztmc;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getWtms() {
        return wtms;
    }

    public void setWtms(String wtms) {
        this.wtms = wtms;
    }
}
