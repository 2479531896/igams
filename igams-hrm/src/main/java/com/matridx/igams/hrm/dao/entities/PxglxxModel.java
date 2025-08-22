package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "PxglxxModel")
public class PxglxxModel extends BaseModel {
    private String pxglid;//培训关联id
    private String gllx;//关联类型
    private String glid;//关联id
    private String pxid;//培训id

    public String getPxglid() {
        return pxglid;
    }

    public void setPxglid(String pxglid) {
        this.pxglid = pxglid;
    }

    public String getGllx() {
        return gllx;
    }

    public void setGllx(String gllx) {
        this.gllx = gllx;
    }

    public String getGlid() {
        return glid;
    }

    public void setGlid(String glid) {
        this.glid = glid;
    }

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
