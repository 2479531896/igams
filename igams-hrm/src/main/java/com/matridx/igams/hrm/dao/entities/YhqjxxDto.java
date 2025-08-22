package com.matridx.igams.hrm.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value = "YhqjxxDto")
public class YhqjxxDto extends YhqjxxModel{
    private List<String> yhids;//用户ids
    private List<String> qjlxs;//请假类型s
    private String nd;//年度

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public List<String> getYhids() {
        return yhids;
    }

    public void setYhids(List<String> yhids) {
        this.yhids = yhids;
    }

    public List<String> getQjlxs() {
        return qjlxs;
    }

    public void setQjlxs(List<String> qjlxs) {
        this.qjlxs = qjlxs;
    }
}
