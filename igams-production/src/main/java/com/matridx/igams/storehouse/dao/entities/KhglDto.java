package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("KhglDto")
public class KhglDto extends KhglModel {
    //修改前客户简称
    private String xgqkhjc;
    //客户类别名称
    private String khlbmc;
    private List<String> fjids;
    //原客户代码
    private String ykhdm;

    public String getYkhdm() {
        return ykhdm;
    }

    public void setYkhdm(String ykhdm) {
        this.ykhdm = ykhdm;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getKhlbmc() {
        return khlbmc;
    }

    public void setKhlbmc(String khlbmc) {
        this.khlbmc = khlbmc;
    }

    public String getXgqkhjc() {
        return xgqkhjc;
    }

    public void setXgqkhjc(String xgqkhjc) {
        this.xgqkhjc = xgqkhjc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
