package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="MbszDto")
public class MbszDto extends MbszModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String[] mblxs;
    private String[] zqlxs;
    private String[] splxs;
    private String[] bms;
    private String[] zts;
    private String entire;
    //用于区分是什么列表
    private String flag;
    //所属部门
    private String jgid;
    private String bm;
    private String yhid;
    //考核类型名称
    private String khlxmc;
    //考核周期名称
    private String khzqmc;
    //考核周期参数扩展1
    private String khzqcskz1;
    //考核周期参数扩展2
    private String khzqcskz2;
    //修改人员名称
    private String xgrymc;
    //录入人员名称
    private String lrrymc;
    //绩效岗位json
    private String jxgw_json;
    //新增审批岗位json
    private String addspgw_json;
    //权重信息json
    private String qzxx_json;
    private String jxshmc;
    private String mbshmc;
    private String mbjbmc;
    private String ffkhymc;
    //模板类型名称
    private String mblxmc;
    //模板子类型名称
    private String mbzlxmc;

    public String getMbzlxmc() {
        return mbzlxmc;
    }

    public void setMbzlxmc(String mbzlxmc) {
        this.mbzlxmc = mbzlxmc;
    }

    public String getMblxmc() {
        return mblxmc;
    }

    public void setMblxmc(String mblxmc) {
        this.mblxmc = mblxmc;
    }
    public String getFfkhymc() {
        return ffkhymc;
    }

    public void setFfkhymc(String ffkhymc) {
        this.ffkhymc = ffkhymc;
    }

    public String getMbjbmc() {
        return mbjbmc;
    }

    public void setMbjbmc(String mbjbmc) {
        this.mbjbmc = mbjbmc;
    }

    public String getJxshmc() {
        return jxshmc;
    }

    public void setJxshmc(String jxshmc) {
        this.jxshmc = jxshmc;
    }

    public String getMbshmc() {
        return mbshmc;
    }

    public void setMbshmc(String mbshmc) {
        this.mbshmc = mbshmc;
    }

    public String getQzxx_json() {
        return qzxx_json;
    }

    public void setQzxx_json(String qzxx_json) {
        this.qzxx_json = qzxx_json;
    }

    public String getJxgw_json() {
        return jxgw_json;
    }

    public void setJxgw_json(String jxgw_json) {
        this.jxgw_json = jxgw_json;
    }

    public String getAddspgw_json() {
        return addspgw_json;
    }

    public void setAddspgw_json(String addspgw_json) {
        this.addspgw_json = addspgw_json;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getXgrymc() {
        return xgrymc;
    }

    public void setXgrymc(String xgrymc) {
        this.xgrymc = xgrymc;
    }

    public String getKhlxmc() {
        return khlxmc;
    }

    public void setKhlxmc(String khlxmc) {
        this.khlxmc = khlxmc;
    }

    public String getKhzqmc() {
        return khzqmc;
    }

    public void setKhzqmc(String khzqmc) {
        this.khzqmc = khzqmc;
    }

    public String getKhzqcskz1() {
        return khzqcskz1;
    }

    public void setKhzqcskz1(String khzqcskz1) {
        this.khzqcskz1 = khzqcskz1;
    }

    public String getKhzqcskz2() {
        return khzqcskz2;
    }

    public void setKhzqcskz2(String khzqcskz2) {
        this.khzqcskz2 = khzqcskz2;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    //模板名称
    private String mbmc;

    public String getMbmc() {
        return mbmc;
    }

    public void setMbmc(String mbmc) {
        this.mbmc = mbmc;
    }

    public String[] getMblxs() {
        return mblxs;
    }

    public void setMblxs(String[] mblxs) {
        this.mblxs = mblxs;
    }

    public String[] getZqlxs() {
        return zqlxs;
    }

    public void setZqlxs(String[] zqlxs) {
        this.zqlxs = zqlxs;
    }

    public String[] getSplxs() {
        return splxs;
    }

    public void setSplxs(String[] splxs) {
        this.splxs = splxs;
    }

    public String[] getBms() {
        return bms;
    }

    public void setBms(String[] bms) {
        this.bms = bms;
    }

    public String[] getZts() {
        return zts;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
    }
}
