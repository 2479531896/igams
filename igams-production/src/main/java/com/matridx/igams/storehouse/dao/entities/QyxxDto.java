package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value="QyxxDto")
public class QyxxDto extends QyxxModel {
    private static final long serialVersionUID = 1L;
    private String wlbm;//物料编码
    private String wlmc;//物料名称
    private String lyxj;//留样小结
    private String scph;//生产批号
    private String jldw;//单位
    private String qysjstart;
    private String qysjend;
    private String kcl;//库存量
    //附件IDS
    private List<String> fjids;
    private String entire;
    //附件标记
    private String fjbj;
    //业务类型
    private String ywlx;

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getFjbj() {
        return fjbj;
    }

    public void setFjbj(String fjbj) {
        this.fjbj = fjbj;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getQysjstart() {
        return qysjstart;
    }

    public void setQysjstart(String qysjstart) {
        this.qysjstart = qysjstart;
    }

    public String getQysjend() {
        return qysjend;
    }

    public void setQysjend(String qysjend) {
        this.qysjend = qysjend;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getLyxj() {
        return lyxj;
    }

    public void setLyxj(String lyxj) {
        this.lyxj = lyxj;
    }

    public String getScph() {
        return scph;
    }

    public void setScph(String scph) {
        this.scph = scph;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }
}
