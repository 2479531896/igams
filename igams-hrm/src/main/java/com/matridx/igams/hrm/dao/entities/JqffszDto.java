package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "JqffszDto")
public class JqffszDto extends JqffszModel{
    private String kssjstart;
    private String kssjend;
    private String jssjstart;
    private String jssjend;
    private String jqlxmc;//假期类型
    private String yhm;//用户名
    private String zsxm;//姓名
    private String lrrymc;
    private String[] scbjs;
    private String[] jqlxs;
    private String entire;
    private String yhids;
    private String jqxz_json;
    private String jzmonth;
    private String jzday;
    private String kssj_t;
    private String ddid;//钉钉id
    private String jqlxcskz2;//假期类型参数扩展2
    private String ljed_t;//前累计金额
    private String sc_t;//前时长

    public String getSc_t() {
        return sc_t;
    }

    public void setSc_t(String sc_t) {
        this.sc_t = sc_t;
    }

    public String getLjed_t() {
        return ljed_t;
    }

    public void setLjed_t(String ljed_t) {
        this.ljed_t = ljed_t;
    }

    public String getJqlxcskz2() {
        return jqlxcskz2;
    }

    public void setJqlxcskz2(String jqlxcskz2) {
        this.jqlxcskz2 = jqlxcskz2;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getKssj_t() {
        return kssj_t;
    }

    public void setKssj_t(String kssj_t) {
        this.kssj_t = kssj_t;
    }

    public String getJzmonth() {
        return jzmonth;
    }

    public void setJzmonth(String jzmonth) {
        this.jzmonth = jzmonth;
    }

    public String getJzday() {
        return jzday;
    }

    public void setJzday(String jzday) {
        this.jzday = jzday;
    }

    public String getJqxz_json() {
        return jqxz_json;
    }

    public void setJqxz_json(String jqxz_json) {
        this.jqxz_json = jqxz_json;
    }

    public String getYhids() {
        return yhids;
    }

    public void setYhids(String yhids) {
        this.yhids = yhids;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getScbjs() {
        return scbjs;
    }

    public void setScbjs(String[] scbjs) {
        this.scbjs = scbjs;
    }

    public String[] getJqlxs() {
        return jqlxs;
    }

    public void setJqlxs(String[] jqlxs) {
        this.jqlxs = jqlxs;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getKssjstart() {
        return kssjstart;
    }

    public void setKssjstart(String kssjstart) {
        this.kssjstart = kssjstart;
    }

    public String getKssjend() {
        return kssjend;
    }

    public void setKssjend(String kssjend) {
        this.kssjend = kssjend;
    }

    public String getJssjstart() {
        return jssjstart;
    }

    public void setJssjstart(String jssjstart) {
        this.jssjstart = jssjstart;
    }

    public String getJssjend() {
        return jssjend;
    }

    public void setJssjend(String jssjend) {
        this.jssjend = jssjend;
    }

    public String getJqlxmc() {
        return jqlxmc;
    }

    public void setJqlxmc(String jqlxmc) {
        this.jqlxmc = jqlxmc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }
}
