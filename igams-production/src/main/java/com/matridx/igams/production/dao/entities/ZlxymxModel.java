package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "ZlxymxModel")
public class ZlxymxModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String zlxymxid;//质量协议明细id
    private String zlxyid;//质量协议id
    private String wlid;//物料id
    private String fwmc;//服务名称
    private String sjxmh;//涉及项目号
    private String jszb;//技术指标
    private String zlyq;//质量要求
    private String ysbz;//验收标准
    private String xh;//序号
    private String zt;//状态

    public String getZlxymxid() {
        return zlxymxid;
    }

    public void setZlxymxid(String zlxymxid) {
        this.zlxymxid = zlxymxid;
    }

    public String getZlxyid() {
        return zlxyid;
    }

    public void setZlxyid(String zlxyid) {
        this.zlxyid = zlxyid;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getFwmc() {
        return fwmc;
    }

    public void setFwmc(String fwmc) {
        this.fwmc = fwmc;
    }

    public String getSjxmh() {
        return sjxmh;
    }

    public void setSjxmh(String sjxmh) {
        this.sjxmh = sjxmh;
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

    public String getYsbz() {
        return ysbz;
    }

    public void setYsbz(String ysbz) {
        this.ysbz = ysbz;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
