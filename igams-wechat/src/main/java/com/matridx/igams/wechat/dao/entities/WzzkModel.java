package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WzzkModel")
public class WzzkModel extends BaseBasicModel {

    //质控id
    private String zkid;

    //就是基础数据的检测单位
    private String jcdwid;
    //文库编号
    private String wkbh;
    //物种id
    private String wzid;
    //，均值
    private String jz;
    //，均值
    private String bzc;
    //开始日期
    private String ksrq;

    //结束日期
    private String jsrq;

    public String getZkid() {
        return zkid;
    }

    public void setZkid(String zkid) {
        this.zkid = zkid;
    }

    public String getJcdwid() {
        return jcdwid;
    }

    public void setJcdwid(String jcdwid) {
        this.jcdwid = jcdwid;
    }



    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    public String getJz() {
        return jz;
    }

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getBzc() {
        return bzc;
    }

    public void setBzc(String bzc) {
        this.bzc = bzc;
    }

    public String getKsrq() {
        return ksrq;
    }

    public void setKsrq(String ksrq) {
        this.ksrq = ksrq;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getWkbh() {
        return wkbh;
    }

    public void setWkbh(String wkbh) {
        this.wkbh = wkbh;
    }
}
