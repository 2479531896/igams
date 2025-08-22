package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author jld
 */
@Alias(value="GfpjbModel")
public class GfpjbModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    private String gfpjid;
    private String gysid;
    private String gfyzid;
    private String zzwjqk;
    private String sybmshr;
    private String zlqk;
    private String jgqk;
    private String ghzqqk;
    private String xcshqk;
    private String sqr;
    private String jl;
    private String zt;
    private String ddslid;
    private String jlbh;
    private String pjsj;

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getPjsj() {
        return pjsj;
    }

    public void setPjsj(String pjsj) {
        this.pjsj = pjsj;
    }

    public String getGfpjid() {
        return gfpjid;
    }

    public void setGfpjid(String gfpjid) {
        this.gfpjid = gfpjid;
    }

    public String getGysid() {
        return gysid;
    }

    public void setGysid(String gysid) {
        this.gysid = gysid;
    }

    public String getGfyzid() {
        return gfyzid;
    }

    public void setGfyzid(String gfyzid) {
        this.gfyzid = gfyzid;
    }

    public String getZzwjqk() {
        return zzwjqk;
    }

    public void setZzwjqk(String zzwjqk) {
        this.zzwjqk = zzwjqk;
    }

    public String getSybmshr() {
        return sybmshr;
    }

    public void setSybmshr(String sybmshr) {
        this.sybmshr = sybmshr;
    }

    public String getZlqk() {
        return zlqk;
    }

    public void setZlqk(String zlqk) {
        this.zlqk = zlqk;
    }

    public String getJgqk() {
        return jgqk;
    }

    public void setJgqk(String jgqk) {
        this.jgqk = jgqk;
    }

    public String getGhzqqk() {
        return ghzqqk;
    }

    public void setGhzqqk(String ghzqqk) {
        this.ghzqqk = ghzqqk;
    }

    public String getXcshqk() {
        return xcshqk;
    }

    public void setXcshqk(String xcshqk) {
        this.xcshqk = xcshqk;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
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

    public String getDdslid() {
        return ddslid;
    }

    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }
}
