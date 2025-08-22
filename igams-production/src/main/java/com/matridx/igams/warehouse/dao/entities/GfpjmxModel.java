package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author jld
 */
@Alias(value="GfpjmxModel")
public class GfpjmxModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    private String pjmxid;
    private String gfpjid;
    private String yzmxid;
    private String wlid;
    private String jszb;
    private String zlyq;
    private String fwmc;
    private String xh;
    private String xmh;

    public String getXmh() {
        return xmh;
    }

    public void setXmh(String xmh) {
        this.xmh = xmh;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getFwmc() {
        return fwmc;
    }

    public void setFwmc(String fwmc) {
        this.fwmc = fwmc;
    }

    public String getPjmxid() {
        return pjmxid;
    }

    public void setPjmxid(String pjmxid) {
        this.pjmxid = pjmxid;
    }

    public String getGfpjid() {
        return gfpjid;
    }

    public void setGfpjid(String gfpjid) {
        this.gfpjid = gfpjid;
    }

    public String getYzmxid() {
        return yzmxid;
    }

    public void setYzmxid(String yzmxid) {
        this.yzmxid = yzmxid;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getJszb() {
        return jszb;
    }

    public void setJszb(String jszb) {
        this.jszb = jszb;
    }

    public String getZlyq() {
        return zlyq;
    }

    public void setZlyq(String zlyq) {
        this.zlyq = zlyq;
    }
}
