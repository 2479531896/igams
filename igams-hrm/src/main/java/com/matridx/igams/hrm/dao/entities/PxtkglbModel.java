package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="PxtkglbModel")
public class PxtkglbModel extends BaseModel {
    //培训id
    private String pxid;
    //题库id
    private String tkid;
    //题目类型
    private String tmlx;
    //数量
    private String sl;
    //分值
    private String fz;
    //序号
    private String Xh;

    public String getXh() {
        return Xh;
    }

    public void setXh(String xh) {
        Xh = xh;
    }

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    public String getTkid() {
        return tkid;
    }

    public void setTkid(String tkid) {
        this.tkid = tkid;
    }

    public String getTmlx() {
        return tmlx;
    }

    public void setTmlx(String tmlx) {
        this.tmlx = tmlx;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getFz() {
        return fz;
    }

    public void setFz(String fz) {
        this.fz = fz;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
