package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias("MrzkModel")
public class MrzkModel extends BaseBasicModel {


    //每日质控ID
    private String mrzkid;
    //芯片ID（芯片信息表的ID）
    private String xpid;

    //芯片名
    private String xpm;
    //检测单位
    private String jcdw;
    //文库日期
    private String wkrq;
    //类型（NC还是PC
    private String zklx;
    //sjid
    private String sjid;
    //标本编号
    private String ybbh;
    //文库编号
    private String wkbm;
    //物种id
    private String wzid;
    //数值
    private String sz;

    public String getMrzkid() {
        return mrzkid;
    }

    public void setMrzkid(String mrzkid) {
        this.mrzkid = mrzkid;
    }

    public String getXpid() {
        return xpid;
    }

    public void setXpid(String xpid) {
        this.xpid = xpid;
    }

    public String getXpm() {
        return xpm;
    }

    public void setXpm(String xpm) {
        this.xpm = xpm;
    }

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getWkrq() {
        return wkrq;
    }

    public void setWkrq(String wkrq) {
        this.wkrq = wkrq;
    }

    public String getZklx() {
        return zklx;
    }

    public void setZklx(String zklx) {
        this.zklx = zklx;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getWkbm() {
        return wkbm;
    }

    public void setWkbm(String wkbm) {
        this.wkbm = wkbm;
    }

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    public String getSz() {
        return sz;
    }

    public void setSz(String sz) {
        this.sz = sz;
    }
}
