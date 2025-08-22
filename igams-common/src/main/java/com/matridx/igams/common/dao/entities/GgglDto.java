package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value = "GgglDto")
public class GgglDto extends GgglModel {
    private String fbrmc;//发布人名称
    //附件IDS
    private List<String> fjids;
    private String lrrymc;//录入人员名称
    private String rdxsmc;//热度显示名称
    private String sfjz;//是否截止

    public String getSfjz() {
        return sfjz;
    }

    public void setSfjz(String sfjz) {
        this.sfjz = sfjz;
    }

    public String getRdxsmc() {
        return rdxsmc;
    }

    public void setRdxsmc(String rdxsmc) {
        this.rdxsmc = rdxsmc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getFbrmc() {
        return fbrmc;
    }

    public void setFbrmc(String fbrmc) {
        this.fbrmc = fbrmc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
