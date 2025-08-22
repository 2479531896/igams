package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className JxtxmxDto
 * @description TODO
 * @date 9:13 2023/3/24
 **/
@Alias(value = "JxtxmxDto")
public class JxtxmxDto extends JxtxmxModel{
    private String bmmc;//部门
    private String yhm;//用户名
    private String zsxm;//真实姓名
    private String gwmc;//岗位名称
    private String zjmc;//职级
    private String khzqcskz1;//考核周期参数1
    private String khzqcskz2;//考核周期参数2
    private String txjb;//提醒级别
    private String txy;//提醒月
    private String txrq;//提醒日期
    private String khzq;//考核周期

    public String getKhzq() {
        return khzq;
    }

    public void setKhzq(String khzq) {
        this.khzq = khzq;
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

    public String getTxjb() {
        return txjb;
    }

    public void setTxjb(String txjb) {
        this.txjb = txjb;
    }

    public String getTxy() {
        return txy;
    }

    public void setTxy(String txy) {
        this.txy = txy;
    }

    public String getTxrq() {
        return txrq;
    }

    public void setTxrq(String txrq) {
        this.txrq = txrq;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getZjmc() {
        return zjmc;
    }

    public void setZjmc(String zjmc) {
        this.zjmc = zjmc;
    }
}
