package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value = "JqxzDto")
public class JqxzDto extends JqxzModel{
    private String jqlxmc;//假期类型
    private List<String> ffszids;//发放设置ids
    private String yhid;//用户id
    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public List<String> getFfszids() {
        return ffszids;
    }

    public void setFfszids(List<String> ffszids) {
        this.ffszids = ffszids;
    }

    public String getJqlxmc() {
        return jqlxmc;
    }

    public void setJqlxmc(String jqlxmc) {
        this.jqlxmc = jqlxmc;
    }
}
