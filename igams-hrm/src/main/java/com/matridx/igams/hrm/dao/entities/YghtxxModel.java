package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;


@Alias(value = "YghtxxModel")
public class YghtxxModel extends BaseModel {
    private String yghtid;	//员工合同id
    private String yghmcid;	//员工花名册id
    private String htqsr;	//合同起始日
    private String htdqr;	//合同到期日

    public String getYghtid() {
        return yghtid;
    }

    public void setYghtid(String yghtid) {
        this.yghtid = yghtid;
    }

    public String getYghmcid() {
        return yghmcid;
    }

    public void setYghmcid(String yghmcid) {
        this.yghmcid = yghmcid;
    }

    public String getHtqsr() {
        return htqsr;
    }

    public void setHtqsr(String htqsr) {
        this.htqsr = htqsr;
    }

    public String getHtdqr() {
        return htdqr;
    }

    public void setHtdqr(String htdqr) {
        this.htdqr = htdqr;
    }

    private static final long serialVersionUID = 1L;
}
