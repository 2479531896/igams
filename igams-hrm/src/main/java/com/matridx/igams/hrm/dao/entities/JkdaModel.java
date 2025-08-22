package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value = "JkdaModel")
public class JkdaModel extends BaseModel {
    private String jkdaid;
    private String yghmcid;
    private String bm;
    private String gwmc;
    private String jcsj;
    private String jcjg;
    private String clyj;

    public String getJkdaid() {
        return jkdaid;
    }

    public void setJkdaid(String jkdaid) {
        this.jkdaid = jkdaid;
    }

    public String getYghmcid() {
        return yghmcid;
    }

    public void setYghmcid(String yghmcid) {
        this.yghmcid = yghmcid;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getJcsj() {
        return jcsj;
    }

    public void setJcsj(String jcsj) {
        this.jcsj = jcsj;
    }

    public String getJcjg() {
        return jcjg;
    }

    public void setJcjg(String jcjg) {
        this.jcjg = jcjg;
    }

    public String getClyj() {
        return clyj;
    }

    public void setClyj(String clyj) {
        this.clyj = clyj;
    }
}
