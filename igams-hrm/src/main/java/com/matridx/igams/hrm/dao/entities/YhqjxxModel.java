package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "YhqjxxModel")
public class YhqjxxModel extends BaseModel {
    private String yhqjid;//用户请假id
    private String kqid;//考勤id
    private String qjlx;//请假类型
    private String qjkssj;//请假开始时间
    private String qjjssj;//请假结束时间
    private String qjsc;//请假时长
    private String ddslid;//钉钉实例id
    private String zt;//状态
    private String xjslid;//销假实例id
    private String yhid;//用户id

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    private static final long serialVersionUID = 1L;
    public String getYhqjid() {
        return yhqjid;
    }

    public void setYhqjid(String yhqjid) {
        this.yhqjid = yhqjid;
    }

    public String getKqid() {
        return kqid;
    }

    public void setKqid(String kqid) {
        this.kqid = kqid;
    }

    public String getQjlx() {
        return qjlx;
    }

    public void setQjlx(String qjlx) {
        this.qjlx = qjlx;
    }

    public String getQjkssj() {
        return qjkssj;
    }

    public void setQjkssj(String qjkssj) {
        this.qjkssj = qjkssj;
    }

    public String getQjjssj() {
        return qjjssj;
    }

    public void setQjjssj(String qjjssj) {
        this.qjjssj = qjjssj;
    }

    public String getQjsc() {
        return qjsc;
    }

    public void setQjsc(String qjsc) {
        this.qjsc = qjsc;
    }

    public String getDdslid() {
        return ddslid;
    }

    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getXjslid() {
        return xjslid;
    }

    public void setXjslid(String xjslid) {
        this.xjslid = xjslid;
    }
}
