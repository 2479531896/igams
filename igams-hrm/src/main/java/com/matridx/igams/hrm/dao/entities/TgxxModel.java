package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;


@Alias(value = "TgxxModel")
public class TgxxModel extends BaseModel {
    //调岗信息
    private String tgxxid;
    //钉钉id
    private String ddid;
    //入职日期
    private String rzrq;
    //原部门
    private String ybm;
    //原主部门
    private String yzbm;
    //原职位
    private String yzw;
    //转入部门
    private String zrbm;
    //职位
    private String zw;
    //生效日期
    private String sxrq;
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
    public String getTgxxid() {
        return tgxxid;
    }

    public void setTgxxid(String tgxxid) {
        this.tgxxid = tgxxid;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getRzrq() {
        return rzrq;
    }

    public void setRzrq(String rzrq) {
        this.rzrq = rzrq;
    }

    public String getYbm() {
        return ybm;
    }

    public void setYbm(String ybm) {
        this.ybm = ybm;
    }

    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    public String getYzw() {
        return yzw;
    }

    public void setYzw(String yzw) {
        this.yzw = yzw;
    }

    public String getZrbm() {
        return zrbm;
    }

    public void setZrbm(String zrbm) {
        this.zrbm = zrbm;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }

    public String getSxrq() {
        return sxrq;
    }

    public void setSxrq(String sxrq) {
        this.sxrq = sxrq;
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
