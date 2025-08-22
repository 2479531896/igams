package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "JqffjlModel")
public class JqffjlModel extends BaseModel {
    private String ffjlid;//发放记录id
    private String jqlx;//假期类型
    private String kssj;//开始时间
    private String yxq;//有效期
    private String fffs;//发放方式
    private String nd;//年度
    private String ffszid;//发放设置id
    private String sc;//时长
    private String dw;//单位 0 小时，1天
    private String yhid;//用户id
    private String jeze;//假期总额
    private String yyed;//已用额度
    private String syed;//剩余额度
    private String ddze;//钉钉总额
    private String ddyyed;//钉钉已用额度
    private String ddsyed;//钉钉剩余额度
    private String yhjqid;//用户假期id
    private static final long serialVersionUID = 1L;

    public String getFfjlid() {
        return ffjlid;
    }

    public void setFfjlid(String ffjlid) {
        this.ffjlid = ffjlid;
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

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getFffs() {
        return fffs;
    }

    public void setFffs(String fffs) {
        this.fffs = fffs;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getFfszid() {
        return ffszid;
    }

    public void setFfszid(String ffszid) {
        this.ffszid = ffszid;
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

    public String getJeze() {
        return jeze;
    }

    public void setJeze(String jeze) {
        this.jeze = jeze;
    }

    public String getYyed() {
        return yyed;
    }

    public void setYyed(String yyed) {
        this.yyed = yyed;
    }

    public String getSyed() {
        return syed;
    }

    public void setSyed(String syed) {
        this.syed = syed;
    }

    public String getDdze() {
        return ddze;
    }

    public void setDdze(String ddze) {
        this.ddze = ddze;
    }

    public String getDdyyed() {
        return ddyyed;
    }

    public void setDdyyed(String ddyyed) {
        this.ddyyed = ddyyed;
    }

    public String getDdsyed() {
        return ddsyed;
    }

    public void setDdsyed(String ddsyed) {
        this.ddsyed = ddsyed;
    }

    public String getYhjqid() {
        return yhjqid;
    }

    public void setYhjqid(String yhjqid) {
        this.yhjqid = yhjqid;
    }
}
