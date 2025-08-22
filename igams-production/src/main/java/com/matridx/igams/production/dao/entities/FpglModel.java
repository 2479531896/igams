package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FpglModel")
public class FpglModel extends BaseBasicModel {


    //发票ID
    private String fpid;
    //发票号
    private String fph;
    //发票类型
    private String fplx;
    //发票种类
    private String fpzl;
    //开票日期
    private String kprq;
    //供应商
    private String gys;
    //代垫单位
    private String dddw;
    //部门
    private String bm;
    //状态
    private String zt;
    //备注
    private String bz;
    //业务员
    private String ywy;
    //税率
    private String sl;
    //汇率
    private String hl;
    //币种
    private String biz;
    //开票金额
    private String kpje;
    //发票代码
    private String fpdm;

    public String getKpje() {
        return kpje;
    }

    public void setKpje(String kpje) {
        this.kpje = kpje;
    }

    public String getFpdm() {
        return fpdm;
    }

    public void setFpdm(String fpdm) {
        this.fpdm = fpdm;
    }

    public String getYwy() {
        return ywy;
    }

    public void setYwy(String ywy) {
        this.ywy = ywy;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getFpid() {
        return fpid;
    }

    public void setFpid(String fpid) {
        this.fpid = fpid;
    }

    public String getFph() {
        return fph;
    }

    public void setFph(String fph) {
        this.fph = fph;
    }

    public String getFplx() {
        return fplx;
    }

    public void setFplx(String fplx) {
        this.fplx = fplx;
    }

    public String getFpzl() {
        return fpzl;
    }

    public void setFpzl(String fpzl) {
        this.fpzl = fpzl;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getGys() {
        return gys;
    }

    public void setGys(String gys) {
        this.gys = gys;
    }

    public String getDddw() {
        return dddw;
    }

    public void setDddw(String dddw) {
        this.dddw = dddw;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
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

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
