package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "BcglModel")
public class BcglModel extends BaseModel {
    private String bcglid;//班次管理id
    private String btglid;//补贴管理id
    private String bcid;//班次id
    private String bcmc;//班次名称
    private String sbdksj;//上班打卡时间
    private String xbdksj;//下班打卡时间

    public String getBcglid() {
        return bcglid;
    }

    public void setBcglid(String bcglid) {
        this.bcglid = bcglid;
    }

    public String getBtglid() {
        return btglid;
    }

    public void setBtglid(String btglid) {
        this.btglid = btglid;
    }

    public String getBcid() {
        return bcid;
    }

    public void setBcid(String bcid) {
        this.bcid = bcid;
    }

    public String getBcmc() {
        return bcmc;
    }

    public void setBcmc(String bcmc) {
        this.bcmc = bcmc;
    }

    public String getSbdksj() {
        return sbdksj;
    }

    public void setSbdksj(String sbdksj) {
        this.sbdksj = sbdksj;
    }

    public String getXbdksj() {
        return xbdksj;
    }

    public void setXbdksj(String xbdksj) {
        this.xbdksj = xbdksj;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
