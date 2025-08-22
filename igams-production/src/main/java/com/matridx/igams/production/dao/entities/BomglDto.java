package com.matridx.igams.production.dao.entities;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("BomglDto")
public class BomglDto extends BomglModel {
    //bom类别名称
    private String bomlbmc;
    //母件物料名称
    private String mjwlmc;
    //母件物料编码
    private String mjwlbm;
    //bom明细数据
    private String bommx_json;
    private String zwlbm;//子物料编码
    private String bzyl;//标准用量
    private String jldw;//单位
    private String gg;//规格
    private List<String> bomlbs;//bom类别s
    private String entire;//模糊查询
    private String zjwlbm;//子件物料编码
    private String zjwlmc;//子件物料名称
    private String bbrqstart;//子件物料名称
    private String bbrqend;//子件物料名称
    private String nowwlid;//wlid

    public String getNowwlid() {
        return nowwlid;
    }

    public void setNowwlid(String nowwlid) {
        this.nowwlid = nowwlid;
    }

    public String getBbrqstart() {
        return bbrqstart;
    }

    public void setBbrqstart(String bbrqstart) {
        this.bbrqstart = bbrqstart;
    }

    public String getBbrqend() {
        return bbrqend;
    }

    public void setBbrqend(String bbrqend) {
        this.bbrqend = bbrqend;
    }

    public String getZjwlbm() {
        return zjwlbm;
    }

    public void setZjwlbm(String zjwlbm) {
        this.zjwlbm = zjwlbm;
    }

    public String getZjwlmc() {
        return zjwlmc;
    }

    public void setZjwlmc(String zjwlmc) {
        this.zjwlmc = zjwlmc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public List<String> getBomlbs() {
        return bomlbs;
    }

    public void setBomlbs(List<String> bomlbs) {
        this.bomlbs = bomlbs;
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

    public String getBommx_json() {
        return bommx_json;
    }

    public void setBommx_json(String bommx_json) {
        this.bommx_json = bommx_json;
    }

    public String getMjwlmc() {
        return mjwlmc;
    }

    public void setMjwlmc(String mjwlmc) {
        this.mjwlmc = mjwlmc;
    }


    public String getBomlbmc() {
        return bomlbmc;
    }

    public void setBomlbmc(String bomlbmc) {
        this.bomlbmc = bomlbmc;
    }

    public String getBzyl() {
        return bzyl;
    }

    public void setBzyl(String bzyl) {
        this.bzyl = bzyl;
    }

    public String getZwlbm() {
        return zwlbm;
    }

    public void setZwlbm(String zwlbm) {
        this.zwlbm = zwlbm;
    }

    public String getMjwlbm() {
        return mjwlbm;
    }

    public void setMjwlbm(String mjwlbm) {
        this.mjwlbm = mjwlbm;
    }
    private static final long serialVersionUID = 1L;
}
