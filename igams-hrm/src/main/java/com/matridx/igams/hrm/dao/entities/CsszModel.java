package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className CsszModel
 * @description TODO
 * @date 15:30 2023/1/9
 **/
@Alias(value = "CsszModel")
public class CsszModel extends BaseModel {
    private String csszid;       //初始设置id
    private String jxshid;       //审核id
    private String khlx;         //考核类型
    private String khzq;         //考核周期
    // private String sxrq;         //生效日期
    // private String yxq;          //失效日期
    private String mbjb;         //模板级别
    private String zp;           //自评 0无需自评 1需要自评
    private String mbshid;       //模板审核id
    // private String ffrq;         //发放日期
    private String ffkhy;        //发放考核月(0本月，1次月)
    // private String jzrq;         //截止日期
    private String jzkhy;        //截止考核月
    // private String jxtx;         //绩效提醒
    private String zdtj;         //自动提交
    private String jxtzsj;       //绩效通知上级
    private String mbtzsj;       //模板通知上级
    private String mblx;       //模板类型
    private String mbzlx;       //模板子类型
    private String yxzq;       //有效周期
    private String xffrq;       //发放日期新
    private String xjzrq;       //截止日期新
    private String xjxtx;       //绩效提醒新
    //分数限制
    private String fsxz;

    public String getFsxz() {
        return fsxz;
    }

    public void setFsxz(String fsxz) {
        this.fsxz = fsxz;
    }
    public String getYxzq() {
        return yxzq;
    }

    public void setYxzq(String yxzq) {
        this.yxzq = yxzq;
    }

    public String getXffrq() {
        return xffrq;
    }

    public void setXffrq(String xffrq) {
        this.xffrq = xffrq;
    }

    public String getXjzrq() {
        return xjzrq;
    }

    public void setXjzrq(String xjzrq) {
        this.xjzrq = xjzrq;
    }

    public String getXjxtx() {
        return xjxtx;
    }

    public void setXjxtx(String xjxtx) {
        this.xjxtx = xjxtx;
    }

    public String getMbzlx() {
        return mbzlx;
    }

    public void setMbzlx(String mbzlx) {
        this.mbzlx = mbzlx;
    }

    public String getMblx() {
        return mblx;
    }

    public void setMblx(String mblx) {
        this.mblx = mblx;
    }

    public String getCsszid() {
        return csszid;
    }

    public void setCsszid(String csszid) {
        this.csszid = csszid;
    }

    public String getJxshid() {
        return jxshid;
    }

    public void setJxshid(String jxshid) {
        this.jxshid = jxshid;
    }

    public String getKhlx() {
        return khlx;
    }

    public void setKhlx(String khlx) {
        this.khlx = khlx;
    }

    public String getKhzq() {
        return khzq;
    }

    public void setKhzq(String khzq) {
        this.khzq = khzq;
    }

/*
    public String getSxrq() {
        return sxrq;
    }

    public void setSxrq(String sxrq) {
        this.sxrq = sxrq;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }
*/

    public String getMbjb() {
        return mbjb;
    }

    public void setMbjb(String mbjb) {
        this.mbjb = mbjb;
    }

    public String getZp() {
        return zp;
    }

    public void setZp(String zp) {
        this.zp = zp;
    }

    public String getMbshid() {
        return mbshid;
    }

    public void setMbshid(String mbshid) {
        this.mbshid = mbshid;
    }

/*    public String getFfrq() {
        return ffrq;
    }

    public void setFfrq(String ffrq) {
        this.ffrq = ffrq;
    }*/

    public String getFfkhy() {
        return ffkhy;
    }

    public void setFfkhy(String ffkhy) {
        this.ffkhy = ffkhy;
    }

   /* public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }*/

    public String getJzkhy() {
        return jzkhy;
    }

    public void setJzkhy(String jzkhy) {
        this.jzkhy = jzkhy;
    }

/*
    public String getJxtx() {
        return jxtx;
    }

    public void setJxtx(String jxtx) {
        this.jxtx = jxtx;
    }
*/

    public String getZdtj() {
        return zdtj;
    }

    public void setZdtj(String zdtj) {
        this.zdtj = zdtj;
    }

    public String getJxtzsj() {
        return jxtzsj;
    }

    public void setJxtzsj(String jxtzsj) {
        this.jxtzsj = jxtzsj;
    }

    public String getMbtzsj() {
        return mbtzsj;
    }

    public void setMbtzsj(String mbtzsj) {
        this.mbtzsj = mbtzsj;
    }

    private static final long serialVersionUID = 1L;
}
