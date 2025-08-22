package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value ="YhqjmxModel")
public class YhqjmxModel extends BaseModel {
    private String qjmxid;//请假明细id
    private String yhjqid;//用户假期id
    private String yhqjid;//用户请假id
    private String sc;//时长
    private static final long serialVersionUID = 1L;

    public String getQjmxid() {
        return qjmxid;
    }

    public void setQjmxid(String qjmxid) {
        this.qjmxid = qjmxid;
    }

    public String getYhjqid() {
        return yhjqid;
    }

    public void setYhjqid(String yhjqid) {
        this.yhjqid = yhjqid;
    }

    public String getYhqjid() {
        return yhqjid;
    }

    public void setYhqjid(String yhqjid) {
        this.yhqjid = yhqjid;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }
}
