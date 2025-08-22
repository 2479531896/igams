package com.matridx.igams.sample.dao.entities;


import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "YbllglModel")
public class YbllglModel extends BaseBasicModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2730214557003292939L;
	//领料id
    private String llid;
    //领料人
    private String llr;
    //发料人
    private String flr;
    //部门
    private String bm;
    //申请日期
    private String sqrq;
    //备注
    private String bz;
    //状态
    private String zt;
    //领料单号
    private String lldh;

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
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

    public String getLldh() {
        return lldh;
    }

    public void setLldh(String lldh) {
        this.lldh = lldh;
    }
}
