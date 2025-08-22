package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("XzdbglDto")
public class XzdbglDto extends XzdbglModel {
    private static final long serialVersionUID = 1L;
    //调出库位名称
    private String drkwmc;
    //调入库位名称
    private String dckwmc;
    //调拨人员名称
    private String dbrymc;
    //调出库位多
    private String[] dckws;

    //调入库位多
    private String[] drkws;
    //调拨开始时间
    private String dbsjstart;
    //调拨结束时间
    private String dbsjend;
    private String dbmx_json;
    private String xzkcid;
    private String kcl;
    private String kwid;

    public String getKwid() {
        return kwid;
    }

    public void setKwid(String kwid) {
        this.kwid = kwid;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getXzkcid() {
        return xzkcid;
    }

    public void setXzkcid(String xzkcid) {
        this.xzkcid = xzkcid;
    }

    public String getDbmx_json() {
        return dbmx_json;
    }

    public void setDbmx_json(String dbmx_json) {
        this.dbmx_json = dbmx_json;
    }

    public String getDbsjstart() {
        return dbsjstart;
    }

    public void setDbsjstart(String dbsjstart) {
        this.dbsjstart = dbsjstart;
    }

    public String getDbsjend() {
        return dbsjend;
    }

    public void setDbsjend(String dbsjend) {
        this.dbsjend = dbsjend;
    }
    //调拨结束时间

    public String[] getDckws() {
        return dckws;
    }

    public void setDckws(String[] dckws) {
        this.dckws = dckws;
    }

    public String[] getDrkws() {
        return drkws;
    }

    public void setDrkws(String[] drkws) {
        this.drkws = drkws;
    }

    public String getDbrymc() {
        return dbrymc;
    }

    public void setDbrymc(String dbrymc) {
        this.dbrymc = dbrymc;
    }

    public String getDrkwmc() {
        return drkwmc;
    }

    public void setDrkwmc(String drkwmc) {
        this.drkwmc = drkwmc;
    }

    public String getDckwmc() {
        return dckwmc;
    }

    public void setDckwmc(String dckwmc) {
        this.dckwmc = dckwmc;
    }
}
