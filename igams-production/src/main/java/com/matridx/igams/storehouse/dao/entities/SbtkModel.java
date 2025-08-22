package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "SbtkModel")
public class SbtkModel extends BaseBasicModel {
    private String sbtkid;//设备退库id
    private String sbysid;//设备验收id
    private String sqr;//申请人
    private String sqbm;//申请部门
    private String sqrq;//申请日期
    private String zt;//状态
    private String yy;//原因

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getSbtkid() {
        return sbtkid;
    }

    public void setSbtkid(String sbtkid) {
        this.sbtkid = sbtkid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getSqbm() {
        return sqbm;
    }

    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
