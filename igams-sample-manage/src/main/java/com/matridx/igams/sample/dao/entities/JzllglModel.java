package com.matridx.igams.sample.dao.entities;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzllglModel")
public class JzllglModel extends BaseBasicModel {
    private String llid;//领料id
    private String lldh;//领料单号
    private String llr;//领料人
    private String flr;//发料人
    private String bm;//部门
    private String sqrq;//申请日期
    private String bz;//备注
    private String zt;//状态
    private String ckzt;//出库状态

    public String getCkzt() {
        return ckzt;
    }

    public void setCkzt(String ckzt) {
        this.ckzt = ckzt;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getLldh() {
        return lldh;
    }

    public void setLldh(String lldh) {
        this.lldh = lldh;
    }

    public String getLlr() {
        return llr;
    }

    public void setLlr(String llr) {
        this.llr = llr;
    }

    public String getFlr() {
        return flr;
    }

    public void setFlr(String flr) {
        this.flr = flr;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
        /**
         *
         */
        private static final long serialVersionUID = 1L;
}
