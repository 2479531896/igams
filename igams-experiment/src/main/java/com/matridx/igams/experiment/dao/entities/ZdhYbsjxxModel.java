package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYbsjxxModel")
public class ZdhYbsjxxModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String ybsjid;
    private String ybmxid;
    private String sjmc;
    private String ph;
    private String yl;
    private String sj;

    public String getYbsjid() {
        return ybsjid;
    }

    public void setYbsjid(String ybsjid) {
        this.ybsjid = ybsjid;
    }

    public String getYbmxid() {
        return ybmxid;
    }

    public void setYbmxid(String ybmxid) {
        this.ybmxid = ybmxid;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getYl() {
        return yl;
    }

    public void setYl(String yl) {
        this.yl = yl;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }
}
