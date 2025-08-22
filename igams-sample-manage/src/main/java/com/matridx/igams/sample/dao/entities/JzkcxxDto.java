package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzkcxxDto")
public class JzkcxxDto extends JzkcxxModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String flmc;//分类名称
    private String lxmc;//类型名称
    private String lrrymc;//录入人员
    private String lrsjstart;
    private String lrsjend;
    private String[] jzlxs;
    private String[] jzfls;
    private String entire;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    //可领数量
    private String klsl;

    public String getKlsl() {
        return klsl;
    }

    public void setKlsl(String klsl) {
        this.klsl = klsl;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getJzlxs() {
        return jzlxs;
    }

    public void setJzlxs(String[] jzlxs) {
        this.jzlxs = jzlxs;
    }

    public String[] getJzfls() {
        return jzfls;
    }

    public void setJzfls(String[] jzfls) {
        this.jzfls = jzfls;
    }

    public String getLrsjstart() {
        return lrsjstart;
    }

    public void setLrsjstart(String lrsjstart) {
        this.lrsjstart = lrsjstart;
    }

    public String getLrsjend() {
        return lrsjend;
    }

    public void setLrsjend(String lrsjend) {
        this.lrsjend = lrsjend;
    }

    public String getFlmc() {
        return flmc;
    }

    public void setFlmc(String flmc) {
        this.flmc = flmc;
    }

    public String getLxmc() {
        return lxmc;
    }

    public void setLxmc(String lxmc) {
        this.lxmc = lxmc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }
}
