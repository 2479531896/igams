package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="GfjxmxDto")
public class GfjxmxDto extends GfjxmxModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String xmmc;
    private String nrmc;
    private String bizmc;
    private String xmcsdm;
    private String nrcsdm;
    private String bizfs;

    public String getBizfs() {
        return bizfs;
    }

    public void setBizfs(String bizfs) {
        this.bizfs = bizfs;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getNrmc() {
        return nrmc;
    }

    public void setNrmc(String nrmc) {
        this.nrmc = nrmc;
    }

    public String getBizmc() {
        return bizmc;
    }

    public void setBizmc(String bizmc) {
        this.bizmc = bizmc;
    }

    public String getXmcsdm() {
        return xmcsdm;
    }

    public void setXmcsdm(String xmcsdm) {
        this.xmcsdm = xmcsdm;
    }

    public String getNrcsdm() {
        return nrcsdm;
    }

    public void setNrcsdm(String nrcsdm) {
        this.nrcsdm = nrcsdm;
    }
}
