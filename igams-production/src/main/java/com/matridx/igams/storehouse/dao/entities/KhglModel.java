package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KhglModel")
public class KhglModel extends BaseModel {

    //'客户id'
    private String khid;

    //'客户代码'
    private String khdm;

    //'客户名称';
    private String khmc;

    //'客户简称';
    private String khjc;

    //'发展日期';
    private String fzrq;

    //'省份';
    private String sf;

    //'币种';
    private String biz;

    //'客户管理类型';
    private String khgllx;

    //发展开始日期
    private String fzrqstart;
    //发展结束日期
    private String fzrqend;
    //全部(查询用)
    private String entire;
    //最大编码
    private  String num;
    //省份名称
    private String sfmc;
    //客户管理类型名称
    private String khgllxmc;
    //币种名称
    private String bizmc;

    //修改前客户代码对应u8表
    private String xgqkhdm;

    //导出关联标记位//所选择的字段
    private String SqlParam;
    //录入人员名称
    private String lrrymc;
    //'修改人员名称';
    private String xgrymc;

    //省份参数dm实cskz1
    private String sfdm;
    //客户类型参数dm
    private String khgllxdm;
    //客户类别
    private String khlb;
    //联系方式
    private String lxfs;
    //客户类别多
    private String[] khlbs;
    //联系人
    private String lxr;
    //邮编
    private String yb;
    //联系地址
    private String lxdz;
    //是否生产
    private String sfsc;
    //终端客户
    private String zdkh;
    //终端联系方式
    private String zdlxfs;

    public String getZdkh() {
        return zdkh;
    }

    public void setZdkh(String zdkh) {
        this.zdkh = zdkh;
    }

    public String getZdlxfs() {
        return zdlxfs;
    }

    public void setZdlxfs(String zdlxfs) {
        this.zdlxfs = zdlxfs;
    }

    public String getSfsc() {
        return sfsc;
    }

    public void setSfsc(String sfsc) {
        this.sfsc = sfsc;
    }

    public String getLxdz() {
        return lxdz;
    }

    public void setLxdz(String lxdz) {
        this.lxdz = lxdz;
    }

    public String getYb() {
        return yb;
    }

    public void setYb(String yb) {
        this.yb = yb;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getKhlb() {
        return khlb;
    }

    public void setKhlb(String khlb) {
        this.khlb = khlb;
    }

    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }

    public String[] getKhlbs() {
        return khlbs;
    }

    public void setKhlbs(String[] khlbs) {
        this.khlbs = khlbs;
    }

    public String getKhgllxdm() {
        return khgllxdm;
    }

    public void setKhgllxdm(String khgllxdm) {
        this.khgllxdm = khgllxdm;
    }

    public String getSfdm() {
        return sfdm;
    }

    public void setSfdm(String sfdm) {
        this.sfdm = sfdm;
    }

    public String getXgrymc() {
        return xgrymc;
    }

    public void setXgrymc(String xgrymc) {
        this.xgrymc = xgrymc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public String getXgqkhdm() {
        return xgqkhdm;
    }

    public void setXgqkhdm(String xgqkhdm) {
        this.xgqkhdm = xgqkhdm;
    }
    public String getSfmc() {
        return sfmc;
    }
    public void setSfmc(String sfmc) {
        this.sfmc = sfmc;
    }
    public String getKhgllxmc() {
        return khgllxmc;
    }
    public void setKhgllxmc(String khgllxmc) {
        this.khgllxmc = khgllxmc;
    }
    public String getBizmc() {
        return bizmc;
    }
    public void setBizmc(String bizmc) {
        this.bizmc = bizmc;
    }
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getFzrqstart() {
        return fzrqstart;
    }

    public void setFzrqstart(String fzrqstart) {
        this.fzrqstart = fzrqstart;
    }

    public String getFzrqend() {
        return fzrqend;
    }

    public void setFzrqend(String fzrqend) {
        this.fzrqend = fzrqend;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getKhdm() {
        return khdm;
    }

    public void setKhdm(String khdm) {
        this.khdm = khdm;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getKhjc() {
        return khjc;
    }

    public void setKhjc(String khjc) {
        this.khjc = khjc;
    }

    public String getFzrq() {
        return fzrq;
    }

    public void setFzrq(String fzrq) {
        this.fzrq = fzrq;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getKhgllx() {
        return khgllx;
    }

    public void setKhgllx(String khgllx) {
        this.khgllx = khgllx;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
