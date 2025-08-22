package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value = "YhjqDto")
public class YhjqDto extends YhjqModel {
    private String clearAll;
    private String ddid;
    private String zsxm;
    private String jqlxmc;
    private String ddsyed_t;
    private String qjkssj;//请假开始时间
    private String sc;//时长
    private String qjmxsc;//请假明细时长
    private String jqlxcskz2;//假期类型参数扩展2
    private List<String> jqlxs;//假期类型s
    private String entire;
    private String diffentFlag;//diffentFlag 差异标记
    private String yhm;//用户名

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getDiffentFlag() {
        return diffentFlag;
    }

    public void setDiffentFlag(String diffentFlag) {
        this.diffentFlag = diffentFlag;
    }

    public List<String> getJqlxs() {
        return jqlxs;
    }

    public void setJqlxs(List<String> jqlxs) {
        this.jqlxs = jqlxs;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getJqlxcskz2() {
        return jqlxcskz2;
    }

    public void setJqlxcskz2(String jqlxcskz2) {
        this.jqlxcskz2 = jqlxcskz2;
    }

    public String getQjmxsc() {
        return qjmxsc;
    }

    public void setQjmxsc(String qjmxsc) {
        this.qjmxsc = qjmxsc;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getQjkssj() {
        return qjkssj;
    }

    public void setQjkssj(String qjkssj) {
        this.qjkssj = qjkssj;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getJqlxmc() {
        return jqlxmc;
    }

    public void setJqlxmc(String jqlxmc) {
        this.jqlxmc = jqlxmc;
    }

    public String getDdsyed_t() {
        return ddsyed_t;
    }

    public void setDdsyed_t(String ddsyed_t) {
        this.ddsyed_t = ddsyed_t;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getClearAll() {
        return clearAll;
    }

    public void setClearAll(String clearAll) {
        this.clearAll = clearAll;
    }
}
