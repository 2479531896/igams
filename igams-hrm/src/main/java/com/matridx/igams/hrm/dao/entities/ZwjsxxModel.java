package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;


@Alias(value = "ZwjsxxModel")
public class ZwjsxxModel extends BaseModel {
    //职位晋升id
    private String zwjsid;
    //钉钉id
    private String ddid;
    //部门
    private String bm;
    //入职时间
    private String rzsj;
    //原职位
    private String yzw;
    //原职位月薪(税前)
    private String yzwyx;
    //晋升职位
    private String jszw;
    //晋升后月薪(税前)
    private String jshyx;
    //生效时间
    private String sxsj;
    //钉钉实例id
    private String ddslid;
    //审批通过时间
    private String sptgsj;
    //外部程序id
    private String wbcxid;

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }
    public String getZwjsid() {
        return zwjsid;
    }

    public void setZwjsid(String zwjsid) {
        this.zwjsid = zwjsid;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getRzsj() {
        return rzsj;
    }

    public void setRzsj(String rzsj) {
        this.rzsj = rzsj;
    }

    public String getYzw() {
        return yzw;
    }

    public void setYzw(String yzw) {
        this.yzw = yzw;
    }

    public String getYzwyx() {
        return yzwyx;
    }

    public void setYzwyx(String yzwyx) {
        this.yzwyx = yzwyx;
    }

    public String getJszw() {
        return jszw;
    }

    public void setJszw(String jszw) {
        this.jszw = jszw;
    }

    public String getJshyx() {
        return jshyx;
    }

    public void setJshyx(String jshyx) {
        this.jshyx = jshyx;
    }

    public String getSxsj() {
        return sxsj;
    }

    public void setSxsj(String sxsj) {
        this.sxsj = sxsj;
    }

    public String getDdslid() {
        return ddslid;
    }

    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }

    public String getSptgsj() {
        return sptgsj;
    }

    public void setSptgsj(String sptgsj) {
        this.sptgsj = sptgsj;
    }

    private static final long serialVersionUID = 1L;
}
