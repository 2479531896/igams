package com.matridx.igams.storehouse.dao.entities;

/*
 *@date 2022年08月30日18:13
 *
 */

import org.apache.ibatis.type.Alias;

@Alias(value="ZjszDto")
public class ZjszDto extends ZjszModel {
    private String zjrymc;
    private String wlmc;

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getScph() {
        return scph;
    }

    public void setScph(String scph) {
        this.scph = scph;
    }

    private String scph;
    public String getZjrymc() {
        return zjrymc;
    }

    public void setZjrymc(String zjrymc) {
        this.zjrymc = zjrymc;
    }
}
