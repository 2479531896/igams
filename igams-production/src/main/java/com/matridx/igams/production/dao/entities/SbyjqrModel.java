package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "SbyjqrModel")
public class SbyjqrModel extends BaseBasicModel {
    private String qrid;//确认id
    private String llid;//履历id
    private String qrzt;//确认状态 0：不通过 1:通过
    private String qrsj;//确认时间
    private String bz;//备注

    public String getQrid() {
        return qrid;
    }

    public void setQrid(String qrid) {
        this.qrid = qrid;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getQrzt() {
        return qrzt;
    }

    public void setQrzt(String qrzt) {
        this.qrzt = qrzt;
    }

    public String getQrsj() {
        return qrsj;
    }

    public void setQrsj(String qrsj) {
        this.qrsj = qrsj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
