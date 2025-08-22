package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JcjymxModel")
public class JcjymxModel extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 借用明细id
    private String jymxid;
    //借用信息id
    private String jyxxid;
    //u8明细关联id
    private String mxglid;
    //借出借用id
    private String jcjyid;
    //货物id
    private String hwid;
    //借用数量
    private String jysl;
    //备注
    private String bz;

    public String getMxglid() {
        return mxglid;
    }

    public void setMxglid(String mxglid) {
        this.mxglid = mxglid;
    }

    public String getJymxid() {
        return jymxid;
    }

    public void setJymxid(String jymxid) {
        this.jymxid = jymxid;
    }

    public String getJyxxid() {
        return jyxxid;
    }

    public void setJyxxid(String jyxxid) {
        this.jyxxid = jyxxid;
    }

    public String getJcjyid() {
        return jcjyid;
    }

    public void setJcjyid(String jcjyid) {
        this.jcjyid = jcjyid;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public String getJysl() {
        return jysl;
    }

    public void setJysl(String jysl) {
        this.jysl = jysl;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
