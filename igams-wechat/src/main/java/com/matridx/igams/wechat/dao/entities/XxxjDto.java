package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="XxxjDto")
public class XxxjDto extends XxxjModel{

    private String lrrymc;

    private String lrsjstart;

    private String lrsjend;

    private String xjlxmc;

    private List<String> xjlxs;

    private List<String> ids;
    //附件ID复数
    private List<String> fjids;



    public String getLrsjend() {
        return lrsjend;
    }

    public void setLrsjend(String lrsjend) {
        this.lrsjend = lrsjend;
    }

    public String getLrsjstart() {
        return lrsjstart;
    }

    public void setLrsjstart(String lrsjstart) {
        this.lrsjstart = lrsjstart;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getXjlxmc() {
        return xjlxmc;
    }

    public void setXjlxmc(String xjlxmc) {
        this.xjlxmc = xjlxmc;
    }

    public List<String> getXjlxs() {
        return xjlxs;
    }

    public void setXjlxs(List<String> xjlxs) {
        this.xjlxs = xjlxs;
    }

    @Override
    public List<String> getIds() {
        return ids;
    }

    @Override
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }
}
