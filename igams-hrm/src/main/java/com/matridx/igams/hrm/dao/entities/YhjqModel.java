package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;
/**
 * @author:JYK
 */
@Alias(value = "YhjqModel")
public class YhjqModel extends BaseModel {
    private String yhjqid;//用户假期id
    private String yhid;//用户id
    private String jqze;//假期总额
    private String dw;//单位 0小时，1天
    private String jqlx;//假期类型
    private String yyed;//已用额度
    private String syed;//剩余额度
    private String nd;//年度
    private String yxq;//有效期
    private String yeqksj;//余额清空时间
    private String kssj;//开始时间
    private String ddyyed;//钉钉已用额度
    private String ddsyed;//钉钉剩余额度

    public String getDdze() {
        return ddze;
    }

    public void setDdze(String ddze) {
        this.ddze = ddze;
    }

    private String ddze;//钉钉总额

    private static final long serialVersionUID = 1L;

    public String getYhjqid() {
        return yhjqid;
    }

    public void setYhjqid(String yhjqid) {
        this.yhjqid = yhjqid;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getJqze() {
        return jqze;
    }

    public void setJqze(String jqze) {
        this.jqze = jqze;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getJqlx() {
        return jqlx;
    }

    public void setJqlx(String jqlx) {
        this.jqlx = jqlx;
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

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getYeqksj() {
        return yeqksj;
    }

    public void setYeqksj(String yeqksj) {
        this.yeqksj = yeqksj;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
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
}
