package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "JqffszModel")
public class JqffszModel extends BaseModel {
    private String ffszid;//发放设置id
    private String jqlx;//假期类型
    private String kssj;//开始时间
    private String jssj;//结束时间
    private String jzrq;//截止日期
    private String jzlx;//截止类型（0本年  1次年）
    private String sc;//时长
    private String dw;//单位 0 小时，1天
    private String yhid;//用户id
    private String ljed;//累计额度
    private String nxzyed;//年限转移额度

    public String getLjed() {
        return ljed;
    }

    public void setLjed(String ljed) {
        this.ljed = ljed;
    }

    public String getNxzyed() {
        return nxzyed;
    }

    public void setNxzyed(String nxzyed) {
        this.nxzyed = nxzyed;
    }

    private static final long serialVersionUID = 1L;
    public String getFfszid() {
        return ffszid;
    }

    public void setFfszid(String ffszid) {
        this.ffszid = ffszid;
    }

    public String getJqlx() {
        return jqlx;
    }

    public void setJqlx(String jqlx) {
        this.jqlx = jqlx;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }

    public String getJzlx() {
        return jzlx;
    }

    public void setJzlx(String jzlx) {
        this.jzlx = jzlx;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }
}
