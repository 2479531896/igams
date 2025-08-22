package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "SyszModel")
public class SyszModel extends BaseModel {
    private String syszid;//首页设置id
    private String yhid;//用户id
    private String zjid;//组件id
    private String line;//行
    private String arrange;//列
    private String sfxs;//是否显示
    private String xh;//序号

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getSyszid() {
        return syszid;
    }

    public void setSyszid(String syszid) {
        this.syszid = syszid;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getZjid() {
        return zjid;
    }

    public void setZjid(String zjid) {
        this.zjid = zjid;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getArrange() {
        return arrange;
    }

    public void setArrange(String arrange) {
        this.arrange = arrange;
    }

    public String getSfxs() {
        return sfxs;
    }

    public void setSfxs(String sfxs) {
        this.sfxs = sfxs;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
