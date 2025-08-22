package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JcghmxModel")
public class JcghmxModel extends BaseModel {

    //归还明细ID
    private String ghmxid;
    //借出归还ID
    private String jcghid;
    //明细关联ID
    private String mxglid;
    //借用明细ID
    private String jyxxid;
    //是否异种归还
    private String sfyzgh;
    //货物ID
    private String hwid;
    //归还数量
    private String ghsl;
    //备注
    private String bz;
    //借用明细id
    private String jymxid;
    private String mxglid2;

    public String getMxglid2() {
        return mxglid2;
    }

    public void setMxglid2(String mxglid2) {
        this.mxglid2 = mxglid2;
    }

    public String getJymxid() {
        return jymxid;
    }

    public void setJymxid(String jymxid) {
        this.jymxid = jymxid;
    }

    public String getGhmxid() {
        return ghmxid;
    }

    public void setGhmxid(String ghmxid) {
        this.ghmxid = ghmxid;
    }

    public String getJcghid() {
        return jcghid;
    }

    public void setJcghid(String jcghid) {
        this.jcghid = jcghid;
    }

    public String getMxglid() {
        return mxglid;
    }

    public void setMxglid(String mxglid) {
        this.mxglid = mxglid;
    }

    public String getJyxxid() {
        return jyxxid;
    }

    public void setJyxxid(String jyxxid) {
        this.jyxxid = jyxxid;
    }

    public String getSfyzgh() {
        return sfyzgh;
    }

    public void setSfyzgh(String sfyzgh) {
        this.sfyzgh = sfyzgh;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public String getGhsl() {
        return ghsl;
    }

    public void setGhsl(String ghsl) {
        this.ghsl = ghsl;
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
