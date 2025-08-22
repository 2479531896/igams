package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbglwjDto")
public class SbglwjDto extends SbglwjModel{
    private String wjmc;//文件名称
    private String wjbm;//文件编码
    private String bbh;//版本号
    private String sxrq;//生效日期
    private String bzrymc;//编制人员
    private String ssdw;//所属单位

    public String getWjmc() {
        return wjmc;
    }

    public void setWjmc(String wjmc) {
        this.wjmc = wjmc;
    }

    public String getWjbm() {
        return wjbm;
    }

    public void setWjbm(String wjbm) {
        this.wjbm = wjbm;
    }

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getSxrq() {
        return sxrq;
    }

    public void setSxrq(String sxrq) {
        this.sxrq = sxrq;
    }

    public String getBzrymc() {
        return bzrymc;
    }

    public void setBzrymc(String bzrymc) {
        this.bzrymc = bzrymc;
    }

    public String getSsdw() {
        return ssdw;
    }

    public void setSsdw(String ssdw) {
        this.ssdw = ssdw;
    }
}
