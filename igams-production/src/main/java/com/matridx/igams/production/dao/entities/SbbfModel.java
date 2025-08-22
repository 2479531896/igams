package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbbfModel")
public class SbbfModel extends BaseBasicModel {
    private String sbbfid;//设备报废id
    private String sbysid;//设备验收id
    private String sqr;//申请人
    private String sqbm;//申请部门
    private String sqsj;//申请时间
    private String bz;//备注
    private String zt;//状态
    private String ddslid;//钉钉实例id

    public String getSbbfid() {
        return sbbfid;
    }

    public void setSbbfid(String sbbfid) {
        this.sbbfid = sbbfid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    @Override
    public String getSqr() {
        return sqr;
    }

    @Override
    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getSqbm() {
        return sqbm;
    }

    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public String getSqsj() {
        return sqsj;
    }

    public void setSqsj(String sqsj) {
        this.sqsj = sqsj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    @Override
    public String getDdslid() {
        return ddslid;
    }

    @Override
    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }
}
