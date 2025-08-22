package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="WbsjxxDto")
public class WbsjxxDto extends WbsjxxModel{

    //合作伙伴List
    private List<String> dbs;
    //检测项目cskz1
    private String jcxmcskz1;
    //检测项目cskz3
    private String jcxmcskz3;
    //lastwbbm
    private String lastwbbm;
    //样本编号
    private String ybbh;
    //接收日期
    private String jsrq;
    //检测项目名称
    private String jcxmmc;

    public String getJcxmmc() {
        return jcxmmc;
    }

    public void setJcxmmc(String jcxmmc) {
        this.jcxmmc = jcxmmc;
    }

    public List<String> getDbs() {
        return dbs;
    }

    public void setDbs(List<String> dbs) {
        this.dbs = dbs;
    }

    public String getJcxmcskz1() {
        return jcxmcskz1;
    }

    public void setJcxmcskz1(String jcxmcskz1) {
        this.jcxmcskz1 = jcxmcskz1;
    }

    public String getJcxmcskz3() {
        return jcxmcskz3;
    }

    public void setJcxmcskz3(String jcxmcskz3) {
        this.jcxmcskz3 = jcxmcskz3;
    }

    public String getLastwbbm() {
        return lastwbbm;
    }

    public void setLastwbbm(String lastwbbm) {
        this.lastwbbm = lastwbbm;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}
}
