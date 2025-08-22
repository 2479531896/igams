package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="DdbxmxModel")
public class DdbxmxModel extends BaseModel {
    //报销明细id
    private String bxmxid;
    //钉钉报销id
    private String ddbxid;
    //金额
    private String je;
    //付款方名称
    private String fkfmc;
    //付款方开户行
    private String fkfkhh;
    //付款方银行账号
    private String fkfyhzh;
    //借款事由
    private String jksy;
    //最晚支付日期
    private String zwzfrq;
    //使用日期
    private String syrq;
    //预计归还日期
    private String yjghrq;
    //u8关联id
    private String u8glid;
    //收款方名称
    private String skfmc;
    //收款方开户行
    private String skfkhh;
    //收款方银行账号
    private String skfyhzh;

    public String getSkfmc() {
        return skfmc;
    }

    public void setSkfmc(String skfmc) {
        this.skfmc = skfmc;
    }

    public String getSkfkhh() {
        return skfkhh;
    }

    public void setSkfkhh(String skfkhh) {
        this.skfkhh = skfkhh;
    }

    public String getSkfyhzh() {
        return skfyhzh;
    }

    public void setSkfyhzh(String skfyhzh) {
        this.skfyhzh = skfyhzh;
    }

    public String getBxmxid() {
        return bxmxid;
    }

    public void setBxmxid(String bxmxid) {
        this.bxmxid = bxmxid;
    }

    public String getDdbxid() {
        return ddbxid;
    }

    public void setDdbxid(String ddbxid) {
        this.ddbxid = ddbxid;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getFkfmc() {
        return fkfmc;
    }

    public void setFkfmc(String fkfmc) {
        this.fkfmc = fkfmc;
    }

    public String getFkfkhh() {
        return fkfkhh;
    }

    public void setFkfkhh(String fkfkhh) {
        this.fkfkhh = fkfkhh;
    }

    public String getFkfyhzh() {
        return fkfyhzh;
    }

    public void setFkfyhzh(String fkfyhzh) {
        this.fkfyhzh = fkfyhzh;
    }

    public String getJksy() {
        return jksy;
    }

    public void setJksy(String jksy) {
        this.jksy = jksy;
    }

    public String getZwzfrq() {
        return zwzfrq;
    }

    public void setZwzfrq(String zwzfrq) {
        this.zwzfrq = zwzfrq;
    }

    public String getSyrq() {
        return syrq;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public String getYjghrq() {
        return yjghrq;
    }

    public void setYjghrq(String yjghrq) {
        this.yjghrq = yjghrq;
    }

    public String getU8glid() {
        return u8glid;
    }

    public void setU8glid(String u8glid) {
        this.u8glid = u8glid;
    }
    private static final long serialVersionUID = 1L;

}
