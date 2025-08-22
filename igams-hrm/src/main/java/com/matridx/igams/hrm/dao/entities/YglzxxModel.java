package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "YglzxxModel")
public class YglzxxModel extends BaseModel {
    private String yglzid;	//员工离职id
    private String yghmcid;	//员工花名册id
    private String rzrq;	//入职日期
    private String lzrq;	//离职日期

    public String getYglzid() {
        return yglzid;
    }

    public void setYglzid(String yglzid) {
        this.yglzid = yglzid;
    }

    public String getYghmcid() {
        return yghmcid;
    }

    public void setYghmcid(String yghmcid) {
        this.yghmcid = yghmcid;
    }

    public String getRzrq() {
        return rzrq;
    }

    public void setRzrq(String rzrq) {
        this.rzrq = rzrq;
    }

    public String getLzrq() {
        return lzrq;
    }

    public void setLzrq(String lzrq) {
        this.lzrq = lzrq;
    }

    private static final long serialVersionUID = 1L;
}
