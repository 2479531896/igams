package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="GfjxglDto")
public class GfjxglDto extends GfjxglModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String shrmc;
    private String khxzmc;
    private String lrrymc;
    private String lxr;
    private String dh;
    private String dz;
    private String gfbh;
    private String gfmc;
    private String cz;

    private String entire;
    private String khkssjstart;
    private String khkssjend;
    private String khjssjstart;
    private String khjssjend;
    private String SqlParam; 	//导出关联标记位//所选择的字段
    //查询参数[多个条件]
    private String searchParam;
    //查询条数
    private String count;
    private String[]gfgllbs;
    private String[]khxzs;
    private String[]pjs;
    private String gfjxmxJson;
    private String pjmc;
    private String updateFlg;
    private String jgid;
    private String shrddid;
    private String ddid;
    private String sprjsid;
    private String sprjsmc;
    private String khxzCskz1;
    private String gfgllbmc;

    public String getGfgllbmc() {
        return gfgllbmc;
    }

    public void setGfgllbmc(String gfgllbmc) {
        this.gfgllbmc = gfgllbmc;
    }

    public String getKhxzCskz1() {
        return khxzCskz1;
    }

    public void setKhxzCskz1(String khxzCskz1) {
        this.khxzCskz1 = khxzCskz1;
    }

    public String getSprjsid() {
        return sprjsid;
    }

    public void setSprjsid(String sprjsid) {
        this.sprjsid = sprjsid;
    }

    public String getSprjsmc() {
        return sprjsmc;
    }

    public void setSprjsmc(String sprjsmc) {
        this.sprjsmc = sprjsmc;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getShrddid() {
        return shrddid;
    }

    public void setShrddid(String shrddid) {
        this.shrddid = shrddid;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getUpdateFlg() {
        return updateFlg;
    }

    public void setUpdateFlg(String updateFlg) {
        this.updateFlg = updateFlg;
    }

    public String getPjmc() {
        return pjmc;
    }

    public void setPjmc(String pjmc) {
        this.pjmc = pjmc;
    }

    public String getGfjxmxJson() {
        return gfjxmxJson;
    }

    public void setGfjxmxJson(String gfjxmxJson) {
        this.gfjxmxJson = gfjxmxJson;
    }

    public String[] getPjs() {
        return pjs;
    }

    public void setPjs(String[] pjs) {
        this.pjs = pjs;
    }

    public String getShrmc() {
        return shrmc;
    }

    public void setShrmc(String shrmc) {
        this.shrmc = shrmc;
    }

    public String getKhxzmc() {
        return khxzmc;
    }

    public void setKhxzmc(String khxzmc) {
        this.khxzmc = khxzmc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getGfbh() {
        return gfbh;
    }

    public void setGfbh(String gfbh) {
        this.gfbh = gfbh;
    }

    public String getGfmc() {
        return gfmc;
    }

    public void setGfmc(String gfmc) {
        this.gfmc = gfmc;
    }

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getKhkssjstart() {
        return khkssjstart;
    }

    public void setKhkssjstart(String khkssjstart) {
        this.khkssjstart = khkssjstart;
    }

    public String getKhkssjend() {
        return khkssjend;
    }

    public void setKhkssjend(String khkssjend) {
        this.khkssjend = khkssjend;
    }

    public String getKhjssjstart() {
        return khjssjstart;
    }

    public void setKhjssjstart(String khjssjstart) {
        this.khjssjstart = khjssjstart;
    }

    public String getKhjssjend() {
        return khjssjend;
    }

    public void setKhjssjend(String khjssjend) {
        this.khjssjend = khjssjend;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String[] getGfgllbs() {
        return gfgllbs;
    }

    public void setGfgllbs(String[] gfgllbs) {
        this.gfgllbs = gfgllbs;
    }

    public String[] getKhxzs() {
        return khxzs;
    }

    public void setKhxzs(String[] khxzs) {
        this.khxzs = khxzs;
    }
}
