package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("XzdbglModel")
public class XzdbglModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    //行政调拨id
    private String xzdbid;
    //货物名称
    private String hwmc;
    //货物标准
    private String hwbz;
    //调出库位
    private String dckw;
    //调入库位
    private String drkw;
    //调拨数量
    private String dbsl;
    //调拨时间
    private String dbsj;
    //调拨人员
    private String dbry;
    public String getXzdbid() {
        return xzdbid;
    }

    public void setXzdbid(String xzdbid) {
        this.xzdbid = xzdbid;
    }

    public String getHwmc() {
        return hwmc;
    }

    public void setHwmc(String hwmc) {
        this.hwmc = hwmc;
    }

    public String getHwbz() {
        return hwbz;
    }

    public void setHwbz(String hwbz) {
        this.hwbz = hwbz;
    }

    public String getDckw() {
        return dckw;
    }

    public void setDckw(String dckw) {
        this.dckw = dckw;
    }

    public String getDrkw() {
        return drkw;
    }

    public void setDrkw(String drkw) {
        this.drkw = drkw;
    }

    public String getDbsl() {
        return dbsl;
    }

    public void setDbsl(String dbsl) {
        this.dbsl = dbsl;
    }

    public String getDbsj() {
        return dbsj;
    }

    public void setDbsj(String dbsj) {
        this.dbsj = dbsj;
    }

    public String getDbry() {
        return dbry;
    }

    public void setDbry(String dbry) {
        this.dbry = dbry;
    }
}
