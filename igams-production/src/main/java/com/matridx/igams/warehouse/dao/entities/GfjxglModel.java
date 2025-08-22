package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="GfjxglModel")
public class GfjxglModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String gfjxid;
    private String khxz;
    private String xse;
    private String khkssj;
    private String khjssj;
    private String jlbh;
    private String gysid;
    private String sgcp;
    private String zykh;
    private String ddslid;
    private String df;
    private String mf;
    private String dfl;
    private String pj;
    private String jl;
    private String zt;
    private String sybmshr;

    public String getSybmshr() {
        return sybmshr;
    }

    public void setSybmshr(String sybmshr) {
        this.sybmshr = sybmshr;
    }

    public String getGfjxid() {
        return gfjxid;
    }

    public void setGfjxid(String gfjxid) {
        this.gfjxid = gfjxid;
    }

    public String getKhxz() {
        return khxz;
    }

    public void setKhxz(String khxz) {
        this.khxz = khxz;
    }

    public String getXse() {
        return xse;
    }

    public void setXse(String xse) {
        this.xse = xse;
    }

    public String getKhkssj() {
        return khkssj;
    }

    public void setKhkssj(String khkssj) {
        this.khkssj = khkssj;
    }

    public String getKhjssj() {
        return khjssj;
    }

    public void setKhjssj(String khjssj) {
        this.khjssj = khjssj;
    }

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getGysid() {
        return gysid;
    }

    public void setGysid(String gysid) {
        this.gysid = gysid;
    }

    public String getSgcp() {
        return sgcp;
    }

    public void setSgcp(String sgcp) {
        this.sgcp = sgcp;
    }

    public String getZykh() {
        return zykh;
    }

    public void setZykh(String zykh) {
        this.zykh = zykh;
    }

    @Override
    public String getDdslid() {
        return ddslid;
    }

    @Override
    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }

    public String getMf() {
        return mf;
    }

    public void setMf(String mf) {
        this.mf = mf;
    }

    public String getDfl() {
        return dfl;
    }

    public void setDfl(String dfl) {
        this.dfl = dfl;
    }

    public String getPj() {
        return pj;
    }

    public void setPj(String pj) {
        this.pj = pj;
    }

    public String getJl() {
        return jl;
    }

    public void setJl(String jl) {
        this.jl = jl;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
}
