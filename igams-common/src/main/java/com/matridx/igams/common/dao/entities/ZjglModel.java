package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value = "ZjglModel")
public class ZjglModel extends BaseModel {
    private String zjid;//组件id
    private String zjmc;//组件名称
    private String fbsmc;//分布式名称
    private String fbsbj;//分布式标记
    private String zjqf;//组件区分
    private String zjlx;//组件类型
    private String zjlj;//组件路径
    private String width;//宽
    private String height;//高
    private String zylj;
    private String xh;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getZylj() {
        return zylj;
    }

    public void setZylj(String zylj) {
        this.zylj = zylj;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjlj() {
        return zjlj;
    }

    public void setZjlj(String zjlj) {
        this.zjlj = zjlj;
    }
    public String getZjqf() {
        return zjqf;
    }

    public void setZjqf(String zjqf) {
        this.zjqf = zjqf;
    }

    public String getZjid() {
        return zjid;
    }

    public void setZjid(String zjid) {
        this.zjid = zjid;
    }

    public String getZjmc() {
        return zjmc;
    }

    public void setZjmc(String zjmc) {
        this.zjmc = zjmc;
    }

    public String getFbsmc() {
        return fbsmc;
    }

    public void setFbsmc(String fbsmc) {
        this.fbsmc = fbsmc;
    }

    public String getFbsbj() {
        return fbsbj;
    }

    public void setFbsbj(String fbsbj) {
        this.fbsbj = fbsbj;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
