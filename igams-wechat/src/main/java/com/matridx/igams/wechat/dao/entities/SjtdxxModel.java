package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "SjtdxxModel")
public class SjtdxxModel extends BaseModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String sjtdid;//送检团单ID
    private String jdsj;//接单时间
    private String ygqjsj;//预估取件时间
    private String qxjdyy;//取消接单原因
    private String qjsj;//取件时间
    private String jsfs;//寄送方式；基础数据
    private String gldh;//关联单号
    private String bc;//班次
    private String dddd;//到达地点
    private String yjddsj;//预计到达时间
    private String qjbz;//取件备注
    private String sdsj;//送达时间
    private String wlfy;//物流费用
    private String wlzt;//物流状态；00：未派单  10：已派单 20：已接单 30：已取件 40：已送达
    private String jdbz;//接单备注
    private String sdbz;//送达备注
    private String yjsdsj;//预计送达时间
    private String jdr;//接单人
    private String jcdw;//检测单位

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getSjtdid() {
        return sjtdid;
    }

    public void setSjtdid(String sjtdid) {
        this.sjtdid = sjtdid;
    }

    public String getJdsj() {
        return jdsj;
    }

    public void setJdsj(String jdsj) {
        this.jdsj = jdsj;
    }

    public String getYgqjsj() {
        return ygqjsj;
    }

    public void setYgqjsj(String ygqjsj) {
        this.ygqjsj = ygqjsj;
    }

    public String getQxjdyy() {
        return qxjdyy;
    }

    public void setQxjdyy(String qxjdyy) {
        this.qxjdyy = qxjdyy;
    }

    public String getQjsj() {
        return qjsj;
    }

    public void setQjsj(String qjsj) {
        this.qjsj = qjsj;
    }

    public String getJsfs() {
        return jsfs;
    }

    public void setJsfs(String jsfs) {
        this.jsfs = jsfs;
    }

    public String getGldh() {
        return gldh;
    }

    public void setGldh(String gldh) {
        this.gldh = gldh;
    }

    public String getBc() {
        return bc;
    }

    public void setBc(String bc) {
        this.bc = bc;
    }

    public String getDddd() {
        return dddd;
    }

    public void setDddd(String dddd) {
        this.dddd = dddd;
    }

    public String getYjddsj() {
        return yjddsj;
    }

    public void setYjddsj(String yjddsj) {
        this.yjddsj = yjddsj;
    }

    public String getQjbz() {
        return qjbz;
    }

    public void setQjbz(String qjbz) {
        this.qjbz = qjbz;
    }

    public String getSdsj() {
        return sdsj;
    }

    public void setSdsj(String sdsj) {
        this.sdsj = sdsj;
    }

    public String getWlfy() {
        return wlfy;
    }

    public void setWlfy(String wlfy) {
        this.wlfy = wlfy;
    }

    public String getWlzt() {
        return wlzt;
    }

    public void setWlzt(String wlzt) {
        this.wlzt = wlzt;
    }

    public String getJdbz() {
        return jdbz;
    }

    public void setJdbz(String jdbz) {
        this.jdbz = jdbz;
    }

    public String getSdbz() {
        return sdbz;
    }

    public void setSdbz(String sdbz) {
        this.sdbz = sdbz;
    }

    public String getYjsdsj() {
        return yjsdsj;
    }

    public void setYjsdsj(String yjsdsj) {
        this.yjsdsj = yjsdsj;
    }

    public String getJdr() {
        return jdr;
    }

    public void setJdr(String jdr) {
        this.jdr = jdr;
    }
}
