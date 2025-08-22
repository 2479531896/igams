package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DzglModel")
public class DzglModel {
    //地址id
    private String dzid;
    //人员id
    private String ryid;
    //省份
    private String sf;
    //城市
    private String cs;
    //详细地址
    private String xxdz;
    //业务类型
    private String ywlx;
    //是否默认
    private String sfmr;
    //电话
    private String dh;
    //姓名
    private String xm;

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getDzid() {
        return dzid;
    }

    public void setDzid(String dzid) {
        this.dzid = dzid;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getXxdz() {
        return xxdz;
    }

    public void setXxdz(String xxdz) {
        this.xxdz = xxdz;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getSfmr() {
        return sfmr;
    }

    public void setSfmr(String sfmr) {
        this.sfmr = sfmr;
    }
}
