package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/*
 *  退货管理
 *
 */
@Alias(value = "ThglModel")
public class ThglModel extends BaseBasicModel {
    //退货id
    private String thid;
    //退货单号
    private String thdh;
    //U8退货单号
    private String u8thdh;
    //经手人
    private String jsr;
    //销售类型
    private String xslx;
    //单据日期
    private String djrq;
    //销售部门
    private String xsbm;
    //订单号
    private String ddh;
    //客户
    private String kh;
    //收货地址
    private String shdz;
    //状态
    private String zt;
    //备注
    private String bz;
    //u8关联id
    private String u8glid;

    public String getU8glid() {
        return u8glid;
    }

    public void setU8glid(String u8glid) {
        this.u8glid = u8glid;
    }

    public String getThid() {
        return thid;
    }

    public void setThid(String thid) {
        this.thid = thid;
    }

    public String getThdh() {
        return thdh;
    }

    public void setThdh(String thdh) {
        this.thdh = thdh;
    }

    public String getU8thdh() {
        return u8thdh;
    }

    public void setU8thdh(String u8thdh) {
        this.u8thdh = u8thdh;
    }

    public String getJsr() {
        return jsr;
    }

    public void setJsr(String jsr) {
        this.jsr = jsr;
    }

    public String getXslx() {
        return xslx;
    }

    public void setXslx(String xslx) {
        this.xslx = xslx;
    }

    public String getDjrq() {
        return djrq;
    }

    public void setDjrq(String djrq) {
        this.djrq = djrq;
    }

    public String getXsbm() {
        return xsbm;
    }

    public void setXsbm(String xsbm) {
        this.xsbm = xsbm;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getKh() {
        return kh;
    }

    public void setKh(String kh) {
        this.kh = kh;
    }

    public String getShdz() {
        return shdz;
    }

    public void setShdz(String shdz) {
        this.shdz = shdz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

}
