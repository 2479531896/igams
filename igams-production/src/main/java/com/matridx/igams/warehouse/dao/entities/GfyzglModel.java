package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="GfyzglModel")
public class GfyzglModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    private String gfyzid;
    private String gysid;
    private String cbpj;
    private String sqr;
    private String sybmshr;
    private String yzshsj;
    private String yzshpj;
    private String jl;
    private String sqzt;
    private String yzzt;
    private String sqddslid;
    private String yzddslid;
    private String jlbh;
    private String yzsqsj;
    private String sfwc;

    public String getSfwc() {
        return sfwc;
    }

    public void setSfwc(String sfwc) {
        this.sfwc = sfwc;
    }

    public String getYzsqsj() {
        return yzsqsj;
    }

    public void setYzsqsj(String yzsqsj) {
        this.yzsqsj = yzsqsj;
    }

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getGfyzid() {
        return gfyzid;
    }

    public void setGfyzid(String gfyzid) {
        this.gfyzid = gfyzid;
    }

    public String getGysid() {
        return gysid;
    }

    public void setGysid(String gysid) {
        this.gysid = gysid;
    }

    public String getCbpj() {
        return cbpj;
    }

    public void setCbpj(String cbpj) {
        this.cbpj = cbpj;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getSybmshr() {
        return sybmshr;
    }

    public void setSybmshr(String sybmshr) {
        this.sybmshr = sybmshr;
    }

    public String getYzshsj() {
        return yzshsj;
    }

    public void setYzshsj(String yzshsj) {
        this.yzshsj = yzshsj;
    }

    public String getYzshpj() {
        return yzshpj;
    }

    public void setYzshpj(String yzshpj) {
        this.yzshpj = yzshpj;
    }

    public String getJl() {
        return jl;
    }

    public void setJl(String jl) {
        this.jl = jl;
    }

    public String getSqzt() {
        return sqzt;
    }

    public void setSqzt(String sqzt) {
        this.sqzt = sqzt;
    }

    public String getYzzt() {
        return yzzt;
    }

    public void setYzzt(String yzzt) {
        this.yzzt = yzzt;
    }

    public String getSqddslid() {
        return sqddslid;
    }

    public void setSqddslid(String sqddslid) {
        this.sqddslid = sqddslid;
    }

    public String getYzddslid() {
        return yzddslid;
    }

    public void setYzddslid(String yzddslid) {
        this.yzddslid = yzddslid;
    }
}
