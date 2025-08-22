package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KqqjxxModel")
public class KqqjxxModel extends BaseModel {

    private String spid;
    private String yhid;
    private String rq;
    private String yhmc;
    private String qjkssj;
    private String qjjssj;
    private String qjsc;
    private String qjcs;
    private String qjlx;
    private String wbcxid;

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }


    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.yhmc = yhmc;
    }

    public String getQjkssj() {
        return qjkssj;
    }

    public void setQjkssj(String qjkssj) {
        this.qjkssj = qjkssj;
    }

    public String getQjjssj() {
        return qjjssj;
    }

    public void setQjjssj(String qjjssj) {
        this.qjjssj = qjjssj;
    }

    public String getQjsc() {
        return qjsc;
    }

    public void setQjsc(String qjsc) {
        this.qjsc = qjsc;
    }

    public String getQjcs() {
        return qjcs;
    }

    public void setQjcs(String qjcs) {
        this.qjcs = qjcs;
    }

    public String getQjlx() {
        return qjlx;
    }

    public void setQjlx(String qjlx) {
        this.qjlx = qjlx;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
