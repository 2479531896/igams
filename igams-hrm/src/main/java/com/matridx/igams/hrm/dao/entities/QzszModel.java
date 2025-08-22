package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className CsszModel
 * @description TODO
 * @date 15:30 2023/1/29
 **/
@Alias(value = "QzszModel")
public class QzszModel extends BaseModel {
    private String csszid;
    private String qzszid;
    private String jxmbid;
    private String gwid;
    private String qz;
    private String mbszid;
    private String xh;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }
    public String getCsszid() {
        return csszid;
    }

    public void setCsszid(String csszid) {
        this.csszid = csszid;
    }

    public String getQzszid() {
        return qzszid;
    }

    public void setQzszid(String qzszid) {
        this.qzszid = qzszid;
    }

    public String getJxmbid() {
        return jxmbid;
    }

    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getGwid() {
        return gwid;
    }

    public void setGwid(String gwid) {
        this.gwid = gwid;
    }

    public String getQz() {
        return qz;
    }

    public void setQz(String qz) {
        this.qz = qz;
    }

    public String getMbszid() {
        return mbszid;
    }

    public void setMbszid(String mbszid) {
        this.mbszid = mbszid;
    }

    private static final long serialVersionUID = 1L;
}
