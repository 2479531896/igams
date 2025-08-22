package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "ActionrecordDto")
public class ActionrecordDto extends ActionrecordModel {

    private String czid;//	操作id
    private String wkbh;//	文库编号
    private String wkcxid;//	文库测序id
    private String bbh;//版本
    private String lx;//审核对像
    private String jtcz;//具体操作
    private String czsj;//	操作时间
    private String czyh;//操作用户

    public String getCzid() {
        return czid;
    }

    public void setCzid(String czid) {
        this.czid = czid;
    }

    public String getWkbh() {
        return wkbh;
    }

    public void setWkbh(String wkbh) {
        this.wkbh = wkbh;
    }

    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getJtcz() {
        return jtcz;
    }

    public void setJtcz(String jtcz) {
        this.jtcz = jtcz;
    }

    public String getCzsj() {
        return czsj;
    }

    public void setCzsj(String czsj) {
        this.czsj = czsj;
    }

    public String getCzyh() {
        return czyh;
    }

    public void setCzyh(String czyh) {
        this.czyh = czyh;
    }

    private static final long serialVersionUID = 1L;

}
