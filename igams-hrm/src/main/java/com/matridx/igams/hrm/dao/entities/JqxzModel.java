package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "JqxzModel")
public class JqxzModel extends BaseModel {
    private String jqxzid;//假期限制id
    private String jqlx;//假期类型
    private String sc;//时长
    private String ffszid;//发放设置

    public String getJqxzid() {
        return jqxzid;
    }

    public void setJqxzid(String jqxzid) {
        this.jqxzid = jqxzid;
    }

    public String getJqlx() {
        return jqlx;
    }

    public void setJqlx(String jqlx) {
        this.jqlx = jqlx;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getFfszid() {
        return ffszid;
    }

    public void setFfszid(String ffszid) {
        this.ffszid = ffszid;
    }
    private static final long serialVersionUID = 1L;

}
