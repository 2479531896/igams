package com.matridx.las.netty.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias("JqrcsszModel")
public class JqrcsszModel extends BaseModel {

    private String csszid;

    private String tdh;

    private String cbgddbh;

    private String sbpzbh;

    private String scbj;

    private String cbwzbh;

    public String getCsszid() {
        return csszid;
    }

    public void setCsszid(String csszid) {
        this.csszid = csszid;
    }

    public String getTdh() {
        return tdh;
    }

    public void setTdh(String tdh) {
        this.tdh = tdh;
    }

    public String getCbgddbh() {
        return cbgddbh;
    }

    public void setCbgddbh(String cbgddbh) {
        this.cbgddbh = cbgddbh;
    }

    public String getSbpzbh() {
        return sbpzbh;
    }

    public void setSbpzbh(String sbpzbh) {
        this.sbpzbh = sbpzbh;
    }

    @Override
    public String getScbj() {
        return scbj;
    }

    @Override
    public void setScbj(String scbj) {
        this.scbj = scbj;
    }

    public String getCbwzbh() {
        return cbwzbh;
    }

    public void setCbwzbh(String cbwzbh) {
        this.cbwzbh = cbwzbh;
    }
    private static final long serialVersionUID = 1L;
}
