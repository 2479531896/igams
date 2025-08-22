package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;


/**
 * @author:JYK
 */
@Alias(value="FlowingModel")
public class FlowingModel extends BaseModel {
    //仓库流水id
    private String lsid;
    //单据类型
    private String djlx;
    //单据表
    private String djb;
    //单据号
    private String djh;
    //收发类别
    private String sflb;
    //部门
    private String bm;
    //物料名称
    private String wlmc;
    //规格型号
    private String ggxh;
    //单位
    private String dw;
    //批号
    private String ph;
    //入库数量
    private String sl;
    //仓库
    private String ck;
    //表体备注
    private String btbz;
    //供应商
    private String gys;
    //制单人
    private String zdr;
    //项目编码
    private String xmbm;
    //物料id
    private String wlid;
    //物料编码
    private String wlbm;
    private String bz;

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getLsid() {
        return lsid;
    }

    public void setLsid(String lsid) {
        this.lsid = lsid;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getDjlx() {
        return djlx;
    }

    public void setDjlx(String djlx) {
        this.djlx = djlx;
    }

    public String getDjb() {
        return djb;
    }

    public void setDjb(String djb) {
        this.djb = djb;
    }

    public String getSflb() {
        return sflb;
    }

    public void setSflb(String sflb) {
        this.sflb = sflb;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getGgxh() {
        return ggxh;
    }

    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getCk() {
        return ck;
    }

    public void setCk(String ck) {
        this.ck = ck;
    }

    public String getBtbz() {
        return btbz;
    }

    public void setBtbz(String btbz) {
        this.btbz = btbz;
    }

    public String getGys() {
        return gys;
    }

    public void setGys(String gys) {
        this.gys = gys;
    }

    public String getZdr() {
        return zdr;
    }

    public void setZdr(String zdr) {
        this.zdr = zdr;
    }

    public String getXmbm() {
        return xmbm;
    }

    public void setXmbm(String xmbm) {
        this.xmbm = xmbm;
    }

    private static final long serialVersionUID = 1L;


}
