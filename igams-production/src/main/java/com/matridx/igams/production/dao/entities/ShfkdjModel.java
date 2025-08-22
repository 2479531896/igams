package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("ShfkdjModel")
public class ShfkdjModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    private String shfkid;//售后反馈id
    private String khid;//客户id
    private String fzr;//负责人
    private String wtms;//问题描述
    private String clyj;//处理意见
    private String jd;//进度
    private String lx;//类型

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public void setShfkid(String shfkid) {
        this.shfkid = shfkid;
    }

    public String getShfkid() {
        return shfkid;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr;
    }

    public String getWtms() {
        return wtms;
    }

    public void setWtms(String wtms) {
        this.wtms = wtms;
    }

    public String getClyj() {
        return clyj;
    }

    public void setClyj(String clyj) {
        this.clyj = clyj;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }
}
