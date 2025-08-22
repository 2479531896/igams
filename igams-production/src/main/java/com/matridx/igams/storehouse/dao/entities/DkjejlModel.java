package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("DkjejlModel")
public class DkjejlModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String dkjlid;//到款记录id
    private String dky;//到款月
    private String dkje;//到款金额
    private String ywid;//业务id
    private String lb;//类别
    private String ywlx;//业务类型
    private String fywid;//父业务id

    public String getFywid() {
        return fywid;
    }

    public void setFywid(String fywid) {
        this.fywid = fywid;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getYwid() {
        return ywid;
    }

    public void setYwid(String ywid) {
        this.ywid = ywid;
    }

    public String getDkjlid() {
        return dkjlid;
    }

    public void setDkjlid(String dkjlid) {
        this.dkjlid = dkjlid;
    }

    public String getDky() {
        return dky;
    }

    public void setDky(String dky) {
        this.dky = dky;
    }

    public String getDkje() {
        return dkje;
    }

    public void setDkje(String dkje) {
        this.dkje = dkje;
    }
}
