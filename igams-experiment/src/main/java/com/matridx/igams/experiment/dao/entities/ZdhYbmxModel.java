package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYbmxModel")
public class ZdhYbmxModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String ybmxid;
    private String ybxxid;
    private String lc;
    private String zlc;
    private String lckssj;
    private String lcjssj;
    private String zlckssj;
    private String zlcjssj;

    public String getYbmxid() {
        return ybmxid;
    }

    public void setYbmxid(String ybmxid) {
        this.ybmxid = ybmxid;
    }

    public String getYbxxid() {
        return ybxxid;
    }

    public void setYbxxid(String ybxxid) {
        this.ybxxid = ybxxid;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getZlc() {
        return zlc;
    }

    public void setZlc(String zlc) {
        this.zlc = zlc;
    }

    public String getLckssj() {
        return lckssj;
    }

    public void setLckssj(String lckssj) {
        this.lckssj = lckssj;
    }

    public String getLcjssj() {
        return lcjssj;
    }

    public void setLcjssj(String lcjssj) {
        this.lcjssj = lcjssj;
    }

    public String getZlckssj() {
        return zlckssj;
    }

    public void setZlckssj(String zlckssj) {
        this.zlckssj = zlckssj;
    }

    public String getZlcjssj() {
        return zlcjssj;
    }

    public void setZlcjssj(String zlcjssj) {
        this.zlcjssj = zlcjssj;
    }
}
