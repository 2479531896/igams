package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="FlowingDto")
public class FlowingDto extends FlowingModel {
    private static final long serialVersionUID = 1L;
    //查看用的id
    private String viewId;
    //所选择的字段
    private String SqlParam;
    private String dcid;

    private String lsrq;

    private String lsrqstart;

    private String lsrqend;
    //导出用的id

    public String getDcid() {
        return dcid;
    }

    public void setDcid(String dcid) {
        this.dcid = dcid;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    //收发类别多
    private String[] sflbs;

    public String[] getSflbs() {
        return sflbs;
    }

    public void setSflbs(String[] sflbs) {
        this.sflbs = sflbs;
    }
    private String[] djlxs;//收发类别多

    public String[] getDjlxs() {
        return djlxs;
    }

    public void setDjlxs(String[] djlxs) {
        this.djlxs = djlxs;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }
    private String entire;

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getLsrq() {
        return lsrq;
    }

    public void setLsrq(String lsrq) {
        this.lsrq = lsrq;
    }

    public String getLsrqstart() {
        return lsrqstart;
    }

    public void setLsrqstart(String lsrqstart) {
        this.lsrqstart = lsrqstart;
    }

    public String getLsrqend() {
        return lsrqend;
    }

    public void setLsrqend(String lsrqend) {
        this.lsrqend = lsrqend;
    }
}

