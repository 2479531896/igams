package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzllcDto")
public class JzllcDto extends JzllcModel{
    //可领数量
    private String klsl;
    private String mc;//名称
    private String ph;//批号
    private String rksl;//入库数量
    private String kcl;//库存量
    private String yds;//预定数
    private String gg;//规格
    private String kbs;//拷贝数

    public String getKbs() {
        return kbs;
    }

    public void setKbs(String kbs) {
        this.kbs = kbs;
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

    public String getRksl() {
        return rksl;
    }

    public void setRksl(String rksl) {
        this.rksl = rksl;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getYds() {
        return yds;
    }

    public void setYds(String yds) {
        this.yds = yds;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getKlsl() {
        return klsl;
    }

    public void setKlsl(String klsl) {
        this.klsl = klsl;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
