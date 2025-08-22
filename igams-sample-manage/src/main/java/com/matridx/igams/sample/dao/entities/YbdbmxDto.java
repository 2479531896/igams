package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="YbdbmxDto")
public class YbdbmxDto extends YbdbmxModel{
    //调出盒子名称
    private String dchzmc;
    //调入盒子名称
    private String drhzmc;
    //存放数
    private String cfs;
    //调出盒子设备号
    private String dchzsbh;
    //调入盒子设备号
    private String drhzsbh;
    //调出冰箱设备号
    private String dcbxsbh;
    //盒子List
    private List<String> hzs;
    //ybdbids
    private List<String> ybdbids;
    //调入盒子编号
    private String drhzbh;
    //调入储存单位
    private String drccdw;
    //调入冰箱
    private String drbx;
    //调入抽屉
    private String drct;
    //调出储存单位
    private String dcccdw;
    //调出冰箱
    private String dcbx;
    //调出抽屉
    private String dcct;

    private String hzpx;
    private String ycfs;

    public String getYcfs() {
        return ycfs;
    }

    public void setYcfs(String ycfs) {
        this.ycfs = ycfs;
    }

    public String getHzpx() {
        return hzpx;
    }

    public void setHzpx(String hzpx) {
        this.hzpx = hzpx;
    }

    public String getDrccdw() {
        return drccdw;
    }

    public void setDrccdw(String drccdw) {
        this.drccdw = drccdw;
    }

    public String getDrbx() {
        return drbx;
    }

    public void setDrbx(String drbx) {
        this.drbx = drbx;
    }

    public String getDrct() {
        return drct;
    }

    public void setDrct(String drct) {
        this.drct = drct;
    }

    public String getDcccdw() {
        return dcccdw;
    }

    public void setDcccdw(String dcccdw) {
        this.dcccdw = dcccdw;
    }

    public String getDcbx() {
        return dcbx;
    }

    public void setDcbx(String dcbx) {
        this.dcbx = dcbx;
    }

    public String getDcct() {
        return dcct;
    }

    public void setDcct(String dcct) {
        this.dcct = dcct;
    }

    public String getDrhzbh() {
        return drhzbh;
    }

    public void setDrhzbh(String drhzbh) {
        this.drhzbh = drhzbh;
    }
    public List<String> getYbdbids() {
        return ybdbids;
    }

    public void setYbdbids(List<String> ybdbids) {
        this.ybdbids = ybdbids;
    }

    public List<String> getHzs() {
        return hzs;
    }
    public void setHzs(List<String> hzs) {
        this.hzs = hzs;
    }
    public String getDchzmc() {
        return dchzmc;
    }

    public void setDchzmc(String dchzmc) {
        this.dchzmc = dchzmc;
    }

    public String getCfs() {
        return cfs;
    }

    public void setCfs(String cfs) {
        this.cfs = cfs;
    }

    public String getDchzsbh() {
        return dchzsbh;
    }

    public void setDchzsbh(String dchzsbh) {
        this.dchzsbh = dchzsbh;
    }

    public String getDcbxsbh() {
        return dcbxsbh;
    }

    public void setDcbxsbh(String dcbxsbh) {
        this.dcbxsbh = dcbxsbh;
    }

    public String getDrhzmc() {
        return drhzmc;
    }

    public void setDrhzmc(String drhzmc) {
        this.drhzmc = drhzmc;
    }

    public String getDrhzsbh() {
        return drhzsbh;
    }

    public void setDrhzsbh(String drhzsbh) {
        this.drhzsbh = drhzsbh;
    }
}
