package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYbxxModel")
public class ZdhYbxxModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String ybxxid;//标本信息ID
    private String sjid;//送检ID
    private String bbbm;//标本编号
    private String jclx;//检测类型
    private String jkgw;//建库工位
    private String chgw;//纯化工位
    private String sgbh;//试管编号
    private String sgjbh;//试管架编号
    private String ybjbh;//样本架编号
    private String jthbh;//接头号编号
    private String jtblgwz;//接头八连管位置
    private String kssj;//开始时间
    private String jssj;//结束时间
    private String sywz;//收样位置
    private String lcmc;//流程名称
    private String zlcmc;//子流程名称
    private String jcdw;//检测单位
    private String ybmxid;//样本明细ID
    private String bz;//备注
    private String lcxh;//流程序号
    private String zlcxh;//子流程序号

    private String zt;//状态
    private String yqbm;//仪器编码
	private String yqid;//仪器id
    private String hssywz;
    private String wksywz;

    public String getHssywz() {
        return hssywz;
    }

    public void setHssywz(String hssywz) {
        this.hssywz = hssywz;
    }

    public String getWksywz() {
        return wksywz;
    }

    public void setWksywz(String wksywz) {
        this.wksywz = wksywz;
    }

    public String getYqid() {
        return yqid;
    }

    public void setYqid(String yqid) {
        this.yqid = yqid;
    }

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getYqbm() {
        return yqbm;
    }

    public void setYqbm(String yqbm) {
        this.yqbm = yqbm;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getYbxxid() {
        return ybxxid;
    }

    public void setYbxxid(String ybxxid) {
        this.ybxxid = ybxxid;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getBbbm() {
        return bbbm;
    }

    public void setBbbm(String bbbm) {
        this.bbbm = bbbm;
    }

    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }

    public String getJkgw() {
        return jkgw;
    }

    public void setJkgw(String jkgw) {
        this.jkgw = jkgw;
    }

    public String getChgw() {
        return chgw;
    }

    public void setChgw(String chgw) {
        this.chgw = chgw;
    }

    public String getSgbh() {
        return sgbh;
    }

    public void setSgbh(String sgbh) {
        this.sgbh = sgbh;
    }

    public String getSgjbh() {
        return sgjbh;
    }

    public void setSgjbh(String sgjbh) {
        this.sgjbh = sgjbh;
    }

    public String getYbjbh() {
        return ybjbh;
    }

    public void setYbjbh(String ybjbh) {
        this.ybjbh = ybjbh;
    }

    public String getJthbh() {
        return jthbh;
    }

    public void setJthbh(String jthbh) {
        this.jthbh = jthbh;
    }

    public String getJtblgwz() {
        return jtblgwz;
    }

    public void setJtblgwz(String jtblgwz) {
        this.jtblgwz = jtblgwz;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getSywz() {
        return sywz;
    }

    public void setSywz(String sywz) {
        this.sywz = sywz;
    }

    public String getLcmc() {
        return lcmc;
    }

    public void setLcmc(String lcmc) {
        this.lcmc = lcmc;
    }

    public String getZlcmc() {
        return zlcmc;
    }

    public void setZlcmc(String zlcmc) {
        this.zlcmc = zlcmc;
    }

    public String getYbmxid() {
        return ybmxid;
    }

    public void setYbmxid(String ybmxid) {
        this.ybmxid = ybmxid;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getLcxh() {
        return lcxh;
    }

    public void setLcxh(String lcxh) {
        this.lcxh = lcxh;
    }

    public String getZlcxh() {
        return zlcxh;
    }

    public void setZlcxh(String zlcxh) {
        this.zlcxh = zlcxh;
    }
}
