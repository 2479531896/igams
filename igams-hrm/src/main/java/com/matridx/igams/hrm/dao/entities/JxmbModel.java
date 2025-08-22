package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("JxmbModel")
public class JxmbModel extends BaseBasicModel {
    //绩效模板id
    private String jxmbid;
    //模板名称
    private String mbmc;
    //部门
    private String bm;
    //考核类型
    private String khlx;
    //修改项说明
    private String xgxsm;
    //类型
    private String lx;
    //提交时间
    private String tjsj;
    //状态
    private String zt;
    //总分
    private String zf;
    //指标上级id
    private String zbsjid;
    //使用日期
    private String syrq;

    public String getSyrq() {
        return syrq;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public String getZbsjid() {
        return zbsjid;
    }

    public void setZbsjid(String zbsjid) {
        this.zbsjid = zbsjid;
    }

    public String getJxmbid() {
        return jxmbid;
    }

    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getMbmc() {
        return mbmc;
    }

    public void setMbmc(String mbmc) {
        this.mbmc = mbmc;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getKhlx() {
        return khlx;
    }

    public void setKhlx(String khlx) {
        this.khlx = khlx;
    }

    public String getXgxsm() {
        return xgxsm;
    }

    public void setXgxsm(String xgxsm) {
        this.xgxsm = xgxsm;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getTjsj() {
        return tjsj;
    }

    public void setTjsj(String tjsj) {
        this.tjsj = tjsj;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }
    private static final long serialVersionUID = 1L;
}
