package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzllmxDto")
public class JzllmxDto extends JzllmxModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String bh;//编号
    private String mc;//名称
    private String ph;//批号
    private String gg;//规格
    private String qlsl;//请领数量
    private String cksl;//出口数量
    private String wz;//位置
    private String ly;//来源
    //预定数
    private String yds;
    //可领数量
    private String klsl;
    //库存量
    private String kcl;
    private String kbs;//拷贝数

    public String getKbs() {
        return kbs;
    }

    public void setKbs(String kbs) {
        this.kbs = kbs;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getKlsl() {
        return klsl;
    }

    public void setKlsl(String klsl) {
        this.klsl = klsl;
    }

    public String getYds() {
        return yds;
    }

    public void setYds(String yds) {
        this.yds = yds;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    @Override
    public String getQlsl() {
        return qlsl;
    }

    @Override
    public void setQlsl(String qlsl) {
        this.qlsl = qlsl;
    }

    @Override
    public String getCksl() {
        return cksl;
    }

    @Override
    public void setCksl(String cksl) {
        this.cksl = cksl;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }
}
