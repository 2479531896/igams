package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias("SyfwDto")
public class SyfwDto extends SyfwModel{
    private String mbmc;//模板名称
    private String xm;//姓名
    //适用范围json
    private String syfw_json;
    //钉钉id
    private String ddid;
    private String yhm;//用户名
    private String gwmc;//岗位名称
    private String zsxm;//真实姓名
    private String bm;//部门
    private String bmmc;//部门名称
    private String khlx;//考核类型
    private List<String> yhids;//用户ids

    public List<String> getYhids() {
        return yhids;
    }

    public void setYhids(List<String> yhids) {
        this.yhids = yhids;
    }

    public String getKhlx() {
        return khlx;
    }

    public void setKhlx(String khlx) {
        this.khlx = khlx;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getSyfw_json() {
        return syfw_json;
    }

    public void setSyfw_json(String syfw_json) {
        this.syfw_json = syfw_json;
    }
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getMbmc() {
        return mbmc;
    }

    public void setMbmc(String mbmc) {
        this.mbmc = mbmc;
    }
    private static final long serialVersionUID = 1L;
}
