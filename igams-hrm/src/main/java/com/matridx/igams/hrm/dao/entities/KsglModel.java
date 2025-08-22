package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KsglModel")
public class KsglModel extends BaseModel {
    //考试id
    private String ksid;
    //培训id
    private String pxid;
    //题目类型
    private String tmlx;
    //题目内容
    private String tmnr;
    //分数
    private String fs;
    //选项代码
    private String xxdm;
    //选项内容
    private String xxnr;
    //序号
    private String xh;
    //答案
    private String da;

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxnr() {
        return xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getKsid() {
        return ksid;
    }

    public void setKsid(String ksid) {
        this.ksid = ksid;
    }

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    public String getTmlx() {
        return tmlx;
    }

    public void setTmlx(String tmlx) {
        this.tmlx = tmlx;
    }

    public String getTmnr() {
        return tmnr;
    }

    public void setTmnr(String tmnr) {
        this.tmnr = tmnr;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
