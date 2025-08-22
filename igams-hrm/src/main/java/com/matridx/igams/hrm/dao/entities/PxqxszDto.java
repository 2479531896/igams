package com.matridx.igams.hrm.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="PxqxszDto")
public class PxqxszDto extends PxqxszModel{
    //机构名称
    private String jgmc;
    //机构代码
    private String jgdm;
    private List<String> jgids;
    private String yhid;
    private String ddid;
    //培训ID
    private String pxid;
    //培训标题
    private String pxbt;
    //培训类别名称
    private String pxlbmc;
    //用户名
    private String yhm;

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    @Override
    public String getPxid() {
        return pxid;
    }

    @Override
    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    public String getPxbt() {
        return pxbt;
    }

    public void setPxbt(String pxbt) {
        this.pxbt = pxbt;
    }

    public String getPxlbmc() {
        return pxlbmc;
    }

    public void setPxlbmc(String pxlbmc) {
        this.pxlbmc = pxlbmc;
    }

    public List<String> getJgids() {
        return jgids;
    }

    public void setJgids(List<String> jgids) {
        this.jgids = jgids;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getJgdm() {
        return jgdm;
    }

    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
