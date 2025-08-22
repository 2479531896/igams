package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "ZsxxModel")
public class ZsxxModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String zsid;
    private String mc;
    private String zs;
    private String glsrst;
    private String taxids;
    private String knwzly;
    private String type;
    private String vfdbfl;
    private String vfid;

    public String getZsid() {
        return zsid;
    }

    public void setZsid(String zsid) {
        this.zsid = zsid;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getGlsrst() {
        return glsrst;
    }

    public void setGlsrst(String glsrst) {
        this.glsrst = glsrst;
    }

    public String getTaxids() {
        return taxids;
    }

    public void setTaxids(String taxids) {
        this.taxids = taxids;
    }

    public String getKnwzly() {
        return knwzly;
    }

    public void setKnwzly(String knwzly) {
        this.knwzly = knwzly;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVfdbfl() {
        return vfdbfl;
    }

    public void setVfdbfl(String vfdbfl) {
        this.vfdbfl = vfdbfl;
    }

    public String getVfid() {
        return vfid;
    }

    public void setVfid(String vfid) {
        this.vfid = vfid;
    }
}
