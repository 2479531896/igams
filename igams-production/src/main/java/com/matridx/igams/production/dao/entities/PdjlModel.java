package com.matridx.igams.production.dao.entities;


import com.matridx.igams.common.dao.entities.BaseBasicModel;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("PdjlModel")
public class PdjlModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    //派单记录id
    private String pdjlid;
    //派单人员id
    private String pdryid;
    //派单状态
    private String pdzt;
    //售后反馈id
    private String shfkid;

    public String getPdjlid() {
        return pdjlid;
    }

    public void setPdjlid(String pdjlid) {
        this.pdjlid = pdjlid;
    }

    public String getPdryid() {
        return pdryid;
    }

    public void setPdryid(String pdryid) {
        this.pdryid = pdryid;
    }

    public String getPdzt() {
        return pdzt;
    }

    public void setPdzt(String pdzt) {
        this.pdzt = pdzt;
    }

    public String getShfkid() {
        return shfkid;
    }

    public void setShfkid(String shfkid) {
        this.shfkid = shfkid;
    }

}
