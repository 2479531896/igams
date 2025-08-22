package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "SbyjllModel")
public class SbyjllModel extends BaseBasicModel {
    private String llid;//履历id
    private String sbysid;//设备验收id
    private String sydd;//使用地点
    private String zt;//状态 0：正常 1：报废 2：售出
    private String bz;//备注
    private String syry;//使用人员
    private String qrzt;//确认状态 0：不通过  1：通过
    private String qrlx;//确认类型0：移交  1：盘库
    private String yy;//原因
    private String ddslid;//钉钉实例id
    private String bmsbfzr;//部门设备负责人
    private String yjr;//移交人
    private String zyjyzq;//转移借用周期
    private String ysyr;//原使用人员
    private String ysybm;//原使用部门
    private String ysydd;//原使用地点
    private String ybmsbfzr;//原部门设备负责人
    private String sybm;//使用部门
    private String yjsj;
    private String jsrzg;//接收人主管

    public String getJsrzg() {
        return jsrzg;
    }

    public void setJsrzg(String jsrzg) {
        this.jsrzg = jsrzg;
    }

    public String getYjsj() {
        return yjsj;
    }

    public void setYjsj(String yjsj) {
        this.yjsj = yjsj;
    }

    public String getSybm() {
        return sybm;
    }

    public void setSybm(String sybm) {
        this.sybm = sybm;
    }

    public String getYsyr() {
        return ysyr;
    }

    public void setYsyr(String ysyr) {
        this.ysyr = ysyr;
    }

    public String getYsybm() {
        return ysybm;
    }

    public void setYsybm(String ysybm) {
        this.ysybm = ysybm;
    }

    public String getYsydd() {
        return ysydd;
    }

    public void setYsydd(String ysydd) {
        this.ysydd = ysydd;
    }

    public String getYbmsbfzr() {
        return ybmsbfzr;
    }

    public void setYbmsbfzr(String ybmsbfzr) {
        this.ybmsbfzr = ybmsbfzr;
    }

    public String getZyjyzq() {
        return zyjyzq;
    }

    public void setZyjyzq(String zyjyzq) {
        this.zyjyzq = zyjyzq;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    @Override
    public String getDdslid() {
        return ddslid;
    }

    @Override
    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }

    public String getBmsbfzr() {
        return bmsbfzr;
    }

    public void setBmsbfzr(String bmsbfzr) {
        this.bmsbfzr = bmsbfzr;
    }

    public String getYjr() {
        return yjr;
    }

    public void setYjr(String yjr) {
        this.yjr = yjr;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
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

    public String getSyry() {
        return syry;
    }

    public void setSyry(String syry) {
        this.syry = syry;
    }

    public String getQrzt() {
        return qrzt;
    }

    public void setQrzt(String qrzt) {
        this.qrzt = qrzt;
    }

    public String getQrlx() {
        return qrlx;
    }

    public void setQrlx(String qrlx) {
        this.qrlx = qrlx;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
