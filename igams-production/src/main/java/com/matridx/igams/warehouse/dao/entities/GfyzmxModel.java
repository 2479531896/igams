package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author jld
 */
@Alias(value="GfyzmxModel")
public class GfyzmxModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    private String yzmxid;
    private String gfyzid;
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

    public String getYzmxid() {
        return yzmxid;
    }

    public void setYzmxid(String yzmxid) {
        this.yzmxid = yzmxid;
    }

    public String getGfyzid() {
        return gfyzid;
    }

    public void setGfyzid(String gfyzid) {
        this.gfyzid = gfyzid;
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
