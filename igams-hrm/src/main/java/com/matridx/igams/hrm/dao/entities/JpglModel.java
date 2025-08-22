package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JpglModel")
public class JpglModel extends BaseModel {
    //奖品管理id
    private String jpglid;
    //开始日期
    private String ksrq;
    //标题
    private String bt;
    //备注
    private String bz;
    //结束日期
    private String jsrq;
    //所属部门
    private String ssbm;
    //奖品类型
    private String jplx;
    //是否发送
    private String sffs;
    //通知群
    private String tzq;

    public String getJpglid() {
        return jpglid;
    }

    public void setJpglid(String jpglid) {
        this.jpglid = jpglid;
    }

    public String getKsrq() {
        return ksrq;
    }

    public void setKsrq(String ksrq) {
        this.ksrq = ksrq;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getSsbm() {
        return ssbm;
    }

    public void setSsbm(String ssbm) {
        this.ssbm = ssbm;
    }

    public String getJplx() {
        return jplx;
    }

    public void setJplx(String jplx) {
        this.jplx = jplx;
    }

    public String getSffs() {
        return sffs;
    }

    public void setSffs(String sffs) {
        this.sffs = sffs;
    }

    public String getTzq() {
        return tzq;
    }

    public void setTzq(String tzq) {
        this.tzq = tzq;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
