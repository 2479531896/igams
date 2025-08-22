package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WklcznDto")
public class WklcznDto extends  WklcznModel{
    private String yblx;
    private String nlz;
    private String xb;
    private String glwz;
    private String yygs;
    private String mc;
    private String ly;
    private String fbsj;
    private String url;

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getNlz() {
        return nlz;
    }

    public void setNlz(String nlz) {
        this.nlz = nlz;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getGlwz() {
        return glwz;
    }

    public void setGlwz(String glwz) {
        this.glwz = glwz;
    }

    public String getYygs() {
        return yygs;
    }

    public void setYygs(String yygs) {
        this.yygs = yygs;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private static final long serialVersionUID = 1L;

}
