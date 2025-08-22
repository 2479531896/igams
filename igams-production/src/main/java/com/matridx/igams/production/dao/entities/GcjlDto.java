package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "GcjlDto")
public class GcjlDto extends GcjlModel {
    //观察人员
    private String gcry;
    //上次观察日期
    private String scgcrq;

    public String getScgcrq() {
        return scgcrq;
    }

    public void setScgcrq(String scgcrq) {
        this.scgcrq = scgcrq;
    }

    public String getGcry() {
        return gcry;
    }

    public void setGcry(String gcry) {
        this.gcry = gcry;
    }

    private static final long serialVersionUID = 1L;
}
