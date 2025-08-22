package com.matridx.igams.sample.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="YbdbmxModel")
public class YbdbmxModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    //调拨明细id
    private String dbmxid;
    //样本调拨id
    private String ybdbid;
    //序号
    private String xh;
    //调出盒子
    private String dchz;
    //调入盒子
    private String drhz;
    //样本数
    private String ybs;

    public String getDbmxid() {
        return dbmxid;
    }

    public void setDbmxid(String dbmxid) {
        this.dbmxid = dbmxid;
    }

    public String getYbdbid() {
        return ybdbid;
    }

    public void setYbdbid(String ybdbid) {
        this.ybdbid = ybdbid;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getDchz() {
        return dchz;
    }

    public void setDchz(String dchz) {
        this.dchz = dchz;
    }

    public String getDrhz() {
        return drhz;
    }

    public void setDrhz(String drhz) {
        this.drhz = drhz;
    }

    public String getYbs() {
        return ybs;
    }

    public void setYbs(String ybs) {
        this.ybs = ybs;
    }
}
