package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SbglwjModel")
public class SbglwjModel extends BaseBasicModel {
    private String sbglwjid;//设备关联文件id
    private String sbglid;//设备关联id
    private String lb;//类别
    private String wjid;//文件id
    private String sbysid;//设备验收id

    public String getSbglwjid() {
        return sbglwjid;
    }

    public void setSbglwjid(String sbglwjid) {
        this.sbglwjid = sbglwjid;
    }

    public String getSbglid() {
        return sbglid;
    }

    public void setSbglid(String sbglid) {
        this.sbglid = sbglid;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getWjid() {
        return wjid;
    }

    public void setWjid(String wjid) {
        this.wjid = wjid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
