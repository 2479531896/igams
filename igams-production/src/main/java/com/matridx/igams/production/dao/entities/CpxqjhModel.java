package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias("CpxqjhModel")
public class CpxqjhModel  extends BaseBasicModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String cpxqid; //成品需求id
    private String xqdh;//需求单号
    private String sqbm;//申请部门
    private String xqrq;//需求日期
    private String yjyt;//预计用途
    private String bz;//备注
    private String zt;//状态
    private String shsj;//审核时间
    private String rkzt;//入库状态
    private String scps;//生产评审

    public String getScps() {
        return scps;
    }

    public void setScps(String scps) {
        this.scps = scps;
    }

    public String getRkzt() {
        return rkzt;
    }

    public void setRkzt(String rkzt) {
        this.rkzt = rkzt;
    }

    public String getShsj() {
        return shsj;
    }

    public void setShsj(String shsj) {
        this.shsj = shsj;
    }

    public String getCpxqid() {
        return cpxqid;
    }

    public void setCpxqid(String cpxqid) {
        this.cpxqid = cpxqid;
    }

    public String getXqdh() {
        return xqdh;
    }

    public void setXqdh(String xqdh) {
        this.xqdh = xqdh;
    }

    public String getSqbm() {
        return sqbm;
    }

    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public String getXqrq() {
        return xqrq;
    }

    public void setXqrq(String xqrq) {
        this.xqrq = xqrq;
    }

    public String getYjyt() {
        return yjyt;
    }

    public void setYjyt(String yjyt) {
        this.yjyt = yjyt;
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

}
