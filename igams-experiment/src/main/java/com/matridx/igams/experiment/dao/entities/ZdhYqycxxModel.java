package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYqycxxModel")
public class ZdhYqycxxModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    private String yqycid;
    private String yqid;
    private String zt;
    private String ycyy;
    private String ycsj;
    private String sfcl;
    private String clr;
    private String clsj;
    private String bz;

    public String getYqycid() {
        return yqycid;
    }

    public void setYqycid(String yqycid) {
        this.yqycid = yqycid;
    }

    public String getYqid() {
        return yqid;
    }

    public void setYqid(String yqid) {
        this.yqid = yqid;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getYcyy() {
        return ycyy;
    }

    public void setYcyy(String ycyy) {
        this.ycyy = ycyy;
    }

    public String getYcsj() {
        return ycsj;
    }

    public void setYcsj(String ycsj) {
        this.ycsj = ycsj;
    }

    public String getSfcl() {
        return sfcl;
    }

    public void setSfcl(String sfcl) {
        this.sfcl = sfcl;
    }

    public String getClr() {
        return clr;
    }

    public void setClr(String clr) {
        this.clr = clr;
    }

    public String getClsj() {
        return clsj;
    }

    public void setClsj(String clsj) {
        this.clsj = clsj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
