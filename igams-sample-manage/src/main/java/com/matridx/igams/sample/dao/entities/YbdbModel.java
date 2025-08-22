package com.matridx.igams.sample.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="YbdbModel")
public class YbdbModel extends BaseBasicModel {

    private static final long serialVersionUID = 1L;
    //样本调拨id
    private String ybdbid;
    //调拨人
    private String dbr;
    //调拨日期
    private String dbrq;
    //调入储存单位
    private String drccdw;
    //调入冰箱
    private String drbx;
    //调入抽屉
    private String drct;
    //调出储存单位
    private String dcccdw;
    //调出冰箱
    private String dcbx;
    //调出抽屉
    private String dcct;
    //物流信息
    private String wlxx;
    //备注
    private String bz;
    //状态
    private String zt;

    public String getYbdbid() {
        return ybdbid;
    }

    public void setYbdbid(String ybdbid) {
        this.ybdbid = ybdbid;
    }

    public String getDbr() {
        return dbr;
    }

    public void setDbr(String dbr) {
        this.dbr = dbr;
    }

    public String getDbrq() {
        return dbrq;
    }

    public void setDbrq(String dbrq) {
        this.dbrq = dbrq;
    }

    public String getDrccdw() {
        return drccdw;
    }

    public void setDrccdw(String drccdw) {
        this.drccdw = drccdw;
    }

    public String getDrbx() {
        return drbx;
    }

    public void setDrbx(String drbx) {
        this.drbx = drbx;
    }

    public String getDrct() {
        return drct;
    }

    public void setDrct(String drct) {
        this.drct = drct;
    }

    public String getDcccdw() {
        return dcccdw;
    }

    public void setDcccdw(String dcccdw) {
        this.dcccdw = dcccdw;
    }

    public String getDcbx() {
        return dcbx;
    }

    public void setDcbx(String dcbx) {
        this.dcbx = dcbx;
    }

    public String getDcct() {
        return dcct;
    }

    public void setDcct(String dcct) {
        this.dcct = dcct;
    }

    public String getWlxx() {
        return wlxx;
    }

    public void setWlxx(String wlxx) {
        this.wlxx = wlxx;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
