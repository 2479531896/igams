package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "NdpxModel")
public class NdpxModel extends BaseModel {

    //年度培训id
    private String ndpxid;
    //年度
    private String nd;
    //培训名称
    private String pxmc;
    //计划培训时间
    private String jhpxsj;
    //参培部门/人员
    private String cpbmry;
    //部门
    private String ssbm;
    //讲师
    private String js;
    //课时
    private String ks;
    //培训方式
    private String pxfs;
    //备注
    private String bz;
    //是否发送
    private String sffs;
    //类别
    private String lb;

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getSffs() {
        return sffs;
    }

    public void setSffs(String sffs) {
        this.sffs = sffs;
    }

    public String getNdpxid() {
        return ndpxid;
    }

    public void setNdpxid(String ndpxid) {
        this.ndpxid = ndpxid;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getPxmc() {
        return pxmc;
    }

    public void setPxmc(String pxmc) {
        this.pxmc = pxmc;
    }

    public String getJhpxsj() {
        return jhpxsj;
    }

    public void setJhpxsj(String jhpxsj) {
        this.jhpxsj = jhpxsj;
    }

    public String getCpbmry() {
        return cpbmry;
    }

    public void setCpbmry(String cpbmry) {
        this.cpbmry = cpbmry;
    }

    public String getSsbm() {
        return ssbm;
    }

    public void setSsbm(String ssbm) {
        this.ssbm = ssbm;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getPxfs() {
        return pxfs;
    }

    public void setPxfs(String pxfs) {
        this.pxfs = pxfs;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
