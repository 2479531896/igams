package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="EtiologyDto")
public class EtiologyDto extends EtiologyModel{

	//周期开始时间
    private String zqkssj;
    //周期结束时间
    private String zqjssj;
    //物种中文名
    private String wzzwm;
    //类型
    private String cxlx;
    //概率
    private String gl;
    //投诉率
    private String tsl;


    public String getZqkssj() {
        return zqkssj;
    }

    public void setZqkssj(String zqkssj) {
        this.zqkssj = zqkssj;
    }

    public String getZqjssj() {
        return zqjssj;
    }

    public void setZqjssj(String zqjssj) {
        this.zqjssj = zqjssj;
    }

    public String getWzzwm() {
        return wzzwm;
    }

    public void setWzzwm(String wzzwm) {
        this.wzzwm = wzzwm;
    }

    public String getCxlx() {
        return cxlx;
    }

    public void setCxlx(String cxlx) {
        this.cxlx = cxlx;
    }

    public String getGl() {
        return gl;
    }

    public void setGl(String gl) {
        this.gl = gl;
    }

    public String getTsl() {
        return tsl;
    }

    public void setTsl(String tsl) {
        this.tsl = tsl;
    }
}
