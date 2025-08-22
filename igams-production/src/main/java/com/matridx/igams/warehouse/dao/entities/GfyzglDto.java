package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author jld
 */
@Alias(value="GfyzglDto")
public class GfyzglDto extends GfyzglModel{
    private static final long serialVersionUID = 1L;
    private String applicationAudit;//申请审核类型
    private String entire;
    private String yzshsjstart;
    private String yzshsjend;
    private String yzsqsjstart;
    private String yzsqsjend;
    private String SqlParam; 	//导出关联标记位//所选择的字段
    //查询参数[多个条件]
    private String searchParam;
    //查询条数
    private String count;
    private String[]gfgllbs;
    private String gfmc;
    private String lxr;
    private String cz;
    private String dh;
    private String gfbh;
    private String sqrmc;
    private String fzrq;
    private String dz;
    private String sybmshrmc;
    private String gfyzmxJson;
    private String shrddid;
    private String jgid;
    private String ddid;
    private String updateFlg;
    private String sprjsid;
    private String sprjsmc;
    private String chooseFlg;
    private String gfpjid;
    private String wlmc;
    private String wlbm;
    private String xmh;
    private String gfgllbmc;

    public String getGfgllbmc() {
        return gfgllbmc;
    }

    public void setGfgllbmc(String gfgllbmc) {
        this.gfgllbmc = gfgllbmc;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getXmh() {
        return xmh;
    }

    public void setXmh(String xmh) {
        this.xmh = xmh;
    }

    public String getGfpjid() {
        return gfpjid;
    }

    public void setGfpjid(String gfpjid) {
        this.gfpjid = gfpjid;
    }

    public String getChooseFlg() {
        return chooseFlg;
    }

    public void setChooseFlg(String chooseFlg) {
        this.chooseFlg = chooseFlg;
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

    public String getUpdateFlg() {
        return updateFlg;
    }

    public void setUpdateFlg(String updateFlg) {
        this.updateFlg = updateFlg;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    public String getShrddid() {
        return shrddid;
    }

    public void setShrddid(String shrddid) {
        this.shrddid = shrddid;
    }

    public String getGfyzmxJson() {
        return gfyzmxJson;
    }

    public void setGfyzmxJson(String gfyzmxJson) {
        this.gfyzmxJson = gfyzmxJson;
    }

    public String getYzsqsjstart() {
        return yzsqsjstart;
    }

    public void setYzsqsjstart(String yzsqsjstart) {
        this.yzsqsjstart = yzsqsjstart;
    }

    public String getYzsqsjend() {
        return yzsqsjend;
    }

    public void setYzsqsjend(String yzsqsjend) {
        this.yzsqsjend = yzsqsjend;
    }

    public String getSybmshrmc() {
        return sybmshrmc;
    }

    public void setSybmshrmc(String sybmshrmc) {
        this.sybmshrmc = sybmshrmc;
    }

    public String getFzrq() {
        return fzrq;
    }

    public void setFzrq(String fzrq) {
        this.fzrq = fzrq;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getGfmc() {
        return gfmc;
    }

    public void setGfmc(String gfmc) {
        this.gfmc = gfmc;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getGfbh() {
        return gfbh;
    }

    public void setGfbh(String gfbh) {
        this.gfbh = gfbh;
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

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getYzshsjstart() {
        return yzshsjstart;
    }

    public void setYzshsjstart(String yzshsjstart) {
        this.yzshsjstart = yzshsjstart;
    }

    public String getYzshsjend() {
        return yzshsjend;
    }

    public void setYzshsjend(String yzshsjend) {
        this.yzshsjend = yzshsjend;
    }

    public String getApplicationAudit() {
        return applicationAudit;
    }

    public void setApplicationAudit(String applicationAudit) {
        this.applicationAudit = applicationAudit;
    }
}
