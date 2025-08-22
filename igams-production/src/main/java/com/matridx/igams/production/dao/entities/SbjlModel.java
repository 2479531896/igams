package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbjlModel")
public class SbjlModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String sbjlid;//设备计量id
    private String sbysid;//设备验收id
    private String qwwcsj;//期望完成时间
    private String jlrq;//计量日期
    private String sqrq;//申请日期
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

    public String getSbjlid() {
        return sbjlid;
    }

    public void setSbjlid(String sbjlid) {
        this.sbjlid = sbjlid;
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

    public String getJlrq() {
        return jlrq;
    }

    public void setJlrq(String jlrq) {
        this.jlrq = jlrq;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
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
}
