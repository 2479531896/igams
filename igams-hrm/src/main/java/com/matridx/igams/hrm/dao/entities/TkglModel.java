package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="TkglModel")
public class TkglModel extends BaseModel {


    //题库id
    private String tkid;
    //题库名称
    private String tkmc;
    //题库类型
    private String tklx;
    //题库类型名称
    private String tklxmc;
    //题库标签
    private String tkbq;
    //所属公司
    private String ssgs;

    public String getSsgs() {
        return ssgs;
    }

    public void setSsgs(String ssgs) {
        this.ssgs = ssgs;
    }

    public String getTkid() {
        return tkid;
    }

    public void setTkid(String tkid) {
        this.tkid = tkid;
    }

    public String getTkmc() {
        return tkmc;
    }

    public void setTkmc(String tkmc) {
        this.tkmc = tkmc;
    }

    public String getTklx() {
        return tklx;
    }

    public void setTklx(String tklx) {
        this.tklx = tklx;
    }

    public String getTklxmc() {
        return tklxmc;
    }

    public void setTklxmc(String tklxmc) {
        this.tklxmc = tklxmc;
    }

    public String getTkbq() {
        return tkbq;
    }

    public void setTkbq(String tkbq) {
        this.tkbq = tkbq;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
