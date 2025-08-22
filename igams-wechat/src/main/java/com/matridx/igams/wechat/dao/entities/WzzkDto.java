package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="WzzkDto")
public class WzzkDto extends WzzkModel{

    private String jcdw;
    /**
     * 检测单位集合
     */
    private List<String> jcdws;
    /**
     * 原信息集合
     */
    private List<String> yxxs;
    /**
     *项目对应类型字符串
     */
    private String xmdyStr;

    private String ncnum;

    private String wkrqstart;

    private String wkrqend;

    private String yxx;

    private String cxy;

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public List<String> getJcdws() {
        return jcdws;
    }

    public void setJcdws(List<String> jcdws) {
        this.jcdws = jcdws;
    }

    public List<String> getYxxs() {
        return yxxs;
    }

    public void setYxxs(List<String> yxxs) {
        this.yxxs = yxxs;
    }

    public String getXmdyStr() {
        return xmdyStr;
    }

    public void setXmdyStr(String xmdyStr) {
        this.xmdyStr = xmdyStr;
    }

    public String getNcnum() {
        return ncnum;
    }

    public void setNcnum(String ncnum) {
        this.ncnum = ncnum;
    }

    public String getWkrqend() {
        return wkrqend;
    }

    public void setWkrqend(String wkrqend) {
        this.wkrqend = wkrqend;
    }

    public String getWkrqstart() {
        return wkrqstart;
    }

    public void setWkrqstart(String wkrqstart) {
        this.wkrqstart = wkrqstart;
    }

    public String getYxx() {
        return yxx;
    }

    public void setYxx(String yxx) {
        this.yxx = yxx;
    }

    public String getCxy() {
        return cxy;
    }

    public void setCxy(String cxy) {
        this.cxy = cxy;
    }
}
