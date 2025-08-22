package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className CsszDto
 * @description TODO
 * @date 15:30 2023/1/9
 **/
@Alias(value="CsszDto")
public class CsszDto extends CsszModel{
    //考核基础类别
    private String khlxjclb;
    //考核类型名称
    private String khlxmc;
    //考核周期名称
    private String khzqmc;
    //考核周期参数扩展1
    private String khzqcskz1;
    //考核周期参数扩展2
    private String khzqcskz2;
    private String sfsz;
    //权重信息json
    private String qzxx_json;
    //模板岗位ids
    private String mbgw_json;
    //绩效岗位json
    private String jxgw_json;
    //新增审批岗位json
    private String addspgw_json;
    //录入人员名称
    private String lrrymc;
    //修改人员名称
    private String xgrymc;
    //模板类型名称
    private String mblxmc;

    public String getMblxmc() {
        return mblxmc;
    }

    public void setMblxmc(String mblxmc) {
        this.mblxmc = mblxmc;
    }

    public String getJxgw_json() {
        return jxgw_json;
    }

    public void setJxgw_json(String jxgw_json) {
        this.jxgw_json = jxgw_json;
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

    public String getMbgw_json() {
        return mbgw_json;
    }

    public void setMbgw_json(String mbgw_json) {
        this.mbgw_json = mbgw_json;
    }

    public String getAddspgw_json() {
        return addspgw_json;
    }

    public void setAddspgw_json(String addspgw_json) {
        this.addspgw_json = addspgw_json;
    }


    public String getQzxx_json() {
        return qzxx_json;
    }

    public void setQzxx_json(String qzxx_json) {
        this.qzxx_json = qzxx_json;
    }

    public String getSfsz() {
        return sfsz;
    }

    public void setSfsz(String sfsz) {
        this.sfsz = sfsz;
    }

    public String getKhlxjclb() {
        return khlxjclb;
    }

    public void setKhlxjclb(String khlxjclb) {
        this.khlxjclb = khlxjclb;
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

}
