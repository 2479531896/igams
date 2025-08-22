package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="EtiologyModel")
public class EtiologyModel extends BaseBasicModel {

    private String etiologyid;
    //类型
    private String lx;
    //日期
    private String rq;
    //物种id
    private String wzid;
    //数据
    private String sj;
    //参数
    private String cs;
    
    private String zbbs;
    //基准差
    private String jz;
    //方差
    private String fc;
    //标准差
    private String bzc;
    //物种中文名
    private String wzzwm;
    //数据
    private String cn;
    //送检数据
    private String sjcn;

    public String getEtiologyid() {
        return etiologyid;
    }

    public void setEtiologyid(String etiologyid) {
        this.etiologyid = etiologyid;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getZbbs() {
        return zbbs;
    }

    public void setZbbs(String zbbs) {
        this.zbbs = zbbs;
    }

    public String getJz() {
        return jz;
    }

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getFc() {
        return fc;
    }

    public void setFc(String fc) {
        this.fc = fc;
    }

    public String getBzc() {
        return bzc;
    }

    public void setBzc(String bzc) {
        this.bzc = bzc;
    }

    public String getSjcn() {
        return sjcn;
    }

    public void setSjcn(String sjcn) {
        this.sjcn = sjcn;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getWzzwm() {
        return wzzwm;
    }

    public void setWzzwm(String wzzwm) {
        this.wzzwm = wzzwm;
    }
}
