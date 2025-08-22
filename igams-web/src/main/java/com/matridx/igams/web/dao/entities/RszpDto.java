package com.matridx.igams.web.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value="RszpDto")
public class RszpDto extends RszpModel {

    //预计月薪
    private String yjyx;
    //发起人部门名称
    private String fqrbmmc;
    //审批id
    private String spid;
    private String xwdgrqstart;
    private String xwdgrqend;
    private String fqsjstart;
    private String fqsjend;
    private String SqlParam; 	//导出关联标记位//所选择的字段
    private String lyrs;

    public String getLyrs() {
        return lyrs;
    }

    public void setLyrs(String lyrs) {
        this.lyrs = lyrs;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public String getXwdgrqstart() {
        return xwdgrqstart;
    }

    public void setXwdgrqstart(String xwdgrqstart) {
        this.xwdgrqstart = xwdgrqstart;
    }

    public String getXwdgrqend() {
        return xwdgrqend;
    }

    public void setXwdgrqend(String xwdgrqend) {
        this.xwdgrqend = xwdgrqend;
    }

    public String getFqsjstart() {
        return fqsjstart;
    }

    public void setFqsjstart(String fqsjstart) {
        this.fqsjstart = fqsjstart;
    }

    public String getFqsjend() {
        return fqsjend;
    }

    public void setFqsjend(String fqsjend) {
        this.fqsjend = fqsjend;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getFqrbmmc() {
        return fqrbmmc;
    }

    public void setFqrbmmc(String fqrbmmc) {
        this.fqrbmmc = fqrbmmc;
    }

    public String getYjyx() {
        return yjyx;
    }

    public void setYjyx(String yjyx) {
        this.yjyx = yjyx;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
