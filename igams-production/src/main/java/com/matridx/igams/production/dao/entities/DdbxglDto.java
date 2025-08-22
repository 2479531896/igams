package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value="DdbxglDto")
public class DdbxglDto extends DdbxglModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7703277656880406633L;
	//报销类型
    private String bxlxmc;
    //部门名称
    private String bmmc;
    //申请人名称
    private String sqrmc;
    //审批人名称
    private String sprmc;
    //报销类型多
    private String[] bxlxs;
    //报销类型代码
    private String bxlxdm;
    //报销类型参数代码1
    private String bxlxcskz1;
    //报销类型参数代码2
    private String bxlxcskz2;
    //报销类型参数代码3
    private String bxlxcskz3;
    //部门扩展参数1
    private String bmkzcs1;
    //审批实例IDs
    private List<String> spslids;
    //U8用户id
    private String u8yhid;
    //机构代码
    private String jgdm;
    //大区
    private String ssdq;

    public String getSsdq() {
        return ssdq;
    }

    public void setSsdq(String ssdq) {
        this.ssdq = ssdq;
    }

    public String getJgdm() {
        return jgdm;
    }

    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    public String getU8yhid() {
        return u8yhid;
    }

    public void setU8yhid(String u8yhid) {
        this.u8yhid = u8yhid;
    }

    public List<String> getSpslids() {
        return spslids;
    }

    public void setSpslids(List<String> spslids) {
        this.spslids = spslids;
    }

    public String getBxlxcskz3() {
        return bxlxcskz3;
    }

    public void setBxlxcskz3(String bxlxcskz3) {
        this.bxlxcskz3 = bxlxcskz3;
    }

    public String getBmkzcs1() {
        return bmkzcs1;
    }

    public void setBmkzcs1(String bmkzcs1) {
        this.bmkzcs1 = bmkzcs1;
    }

    public String getBxlxcskz2() {
        return bxlxcskz2;
    }

    public void setBxlxcskz2(String bxlxcskz2) {
        this.bxlxcskz2 = bxlxcskz2;
    }

    public String getBxlxcskz1() {
        return bxlxcskz1;
    }

    public void setBxlxcskz1(String bxlxcskz1) {
        this.bxlxcskz1 = bxlxcskz1;
    }

    public String getBxlxdm() {
        return bxlxdm;
    }

    public void setBxlxdm(String bxlxdm) {
        this.bxlxdm = bxlxdm;
    }

    public String[] getBxlxs() {
        return bxlxs;
    }

    public void setBxlxs(String[] bxlxs) {
        this.bxlxs = bxlxs;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getSprmc() {
        return sprmc;
    }

    public void setSprmc(String sprmc) {
        this.sprmc = sprmc;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getBxlxmc() {
        return bxlxmc;
    }

    public void setBxlxmc(String bxlxmc) {
        this.bxlxmc = bxlxmc;
    }
}
