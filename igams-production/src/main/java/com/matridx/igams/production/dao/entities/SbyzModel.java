package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbyzModel")
public class SbyzModel extends BaseBasicModel {
    private String sbyzid;//设备验证id
    private String sbysid;//设备验收id
    private String qwwcsj;//期望完成时间
    private String sqrq;//申请日期
    private String yzrq;//验证日期
    private String sqr;//申请人
    private String sqbm;//申请部门
    private String zt;//状态
    private String yjwcsj;//预计完成时间

    public String getYjwcsj() {
        return yjwcsj;
    }

    public void setYjwcsj(String yjwcsj) {
        this.yjwcsj = yjwcsj;
    }

    public String getSbyzid() {
        return sbyzid;
    }

    public void setSbyzid(String sbyzid) {
        this.sbyzid = sbyzid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getQwwcsj() {
        return qwwcsj;
    }

    public void setQwwcsj(String qwwcsj) {
        this.qwwcsj = qwwcsj;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    public String getYzrq() {
        return yzrq;
    }

    public void setYzrq(String yzrq) {
        this.yzrq = yzrq;
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

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
    private static final long serialVersionUID = 1L;
}
