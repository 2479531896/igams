package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @program: igams
 * @description:
 * @create: 2022-07-05 15:06
 **/
@Alias("ThmxModel")
public class ThmxModel extends BaseBasicModel {
    //退货明细id
    private String thmxid;
    //退货id
    private String thid;
    //货物id
    private String hwid;
    //退货数量
    private String thsl;
    //备注
    private String bz;
    //销售订单
    private String xsdd;
    //发货明细关联id
    private String fhmxglid;
    //报价
    private String bj;
    //税率
    private String suil;
    //无税单价
    private String wsdj;
    //含税单价
    private String hsdj;
    //销售明细id
    private String xsmxid;
    //发货明细id
    private String fhmxid;
    //出库id
    private String ckid;
    //u8明细关联id
    private String u8mxglid;
    //出库mxid
    private String ckmxid;
    //出库明细关联id
    private String ckmxglid;

    public String getCkmxglid() {
        return ckmxglid;
    }

    public void setCkmxglid(String ckmxglid) {
        this.ckmxglid = ckmxglid;
    }

    public String getCkmxid() {
        return ckmxid;
    }

    public void setCkmxid(String ckmxid) {
        this.ckmxid = ckmxid;
    }

    public String getU8mxglid() {
        return u8mxglid;
    }

    public void setU8mxglid(String u8mxglid) {
        this.u8mxglid = u8mxglid;
    }

    public String getThmxid() {
        return thmxid;
    }

    public void setThmxid(String thmxid) {
        this.thmxid = thmxid;
    }


    public String getThid() {
        return thid;
    }

    public void setThid(String thid) {
        this.thid = thid;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }


    public String getThsl() {
        return thsl;
    }

    public void setThsl(String thsl) {
        this.thsl = thsl;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }


    public String getXsdd() {
        return xsdd;
    }

    public void setXsdd(String xsdd) {
        this.xsdd = xsdd;
    }


    public String getFhmxglid() {
        return fhmxglid;
    }

    public void setFhmxglid(String fhmxglid) {
        this.fhmxglid = fhmxglid;
    }


    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }


    public String getSuil() {
        return suil;
    }

    public void setSuil(String suil) {
        this.suil = suil;
    }


    public String getWsdj() {
        return wsdj;
    }

    public void setWsdj(String wsdj) {
        this.wsdj = wsdj;
    }


    public String getHsdj() {
        return hsdj;
    }

    public void setHsdj(String hsdj) {
        this.hsdj = hsdj;
    }


    public String getXsmxid() {
        return xsmxid;
    }

    public void setXsmxid(String xsmxid) {
        this.xsmxid = xsmxid;
    }


    public String getFhmxid() {
        return fhmxid;
    }

    public void setFhmxid(String fhmxid) {
        this.fhmxid = fhmxid;
    }


    public String getCkid() {
        return ckid;
    }

    public void setCkid(String ckid) {
        this.ckid = ckid;
    }}
