package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="DdbxglModel")
public class DdbxglModel extends BaseModel {
    //钉钉报销id
    private String ddbxid;
    //审批实例id
    private String spslid;
    //所属公司
    private String ssgs;
    //审批人
    private String spr;
    //申请人
    private String sqr;
    //总金额
    private String zje;
    //部门
    private String bm;
    //U8关联id
    private String u8glid;
    //凭证编号
    private String pzbh;
    //同步数据
    private String tbsj;
    //备注
    private String bz;
    //摘要
    private String zy;
    //报销类型
    private String bxlx;
    //会议类型
    private String hylx;

    public String getHylx() {
        return hylx;
    }

    public void setHylx(String hylx) {
        this.hylx = hylx;
    }

    public String getDdbxid() {
        return ddbxid;
    }

    public void setDdbxid(String ddbxid) {
        this.ddbxid = ddbxid;
    }

    public String getSpslid() {
        return spslid;
    }

    public void setSpslid(String spslid) {
        this.spslid = spslid;
    }

    public String getSsgs() {
        return ssgs;
    }

    public void setSsgs(String ssgs) {
        this.ssgs = ssgs;
    }

    public String getSpr() {
        return spr;
    }

    public void setSpr(String spr) {
        this.spr = spr;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getZje() {
        return zje;
    }

    public void setZje(String zje) {
        this.zje = zje;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getU8glid() {
        return u8glid;
    }

    public void setU8glid(String u8glid) {
        this.u8glid = u8glid;
    }

    public String getPzbh() {
        return pzbh;
    }

    public void setPzbh(String pzbh) {
        this.pzbh = pzbh;
    }

    public String getTbsj() {
        return tbsj;
    }

    public void setTbsj(String tbsj) {
        this.tbsj = tbsj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getBxlx() {
        return bxlx;
    }

    public void setBxlx(String bxlx) {
        this.bxlx = bxlx;
    }
    private static final long serialVersionUID = 1L;
}
