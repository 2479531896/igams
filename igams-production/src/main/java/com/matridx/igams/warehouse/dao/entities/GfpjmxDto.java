package com.matridx.igams.warehouse.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author jld
 */
@Alias(value="GfpjmxDto")
public class GfpjmxDto extends GfpjmxModel{
    private static final long serialVersionUID = 1L;
    private String wlmc;
    private String wlbm;
    private String gg;

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }
}
