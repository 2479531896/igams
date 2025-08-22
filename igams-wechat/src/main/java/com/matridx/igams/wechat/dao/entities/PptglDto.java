package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="PptglDto")
public class PptglDto extends  PptglModel{

    private List<String> fjids;

    private List<String> ids;


    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    @Override
    public List<String> getIds() {
        return ids;
    }

    @Override
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
