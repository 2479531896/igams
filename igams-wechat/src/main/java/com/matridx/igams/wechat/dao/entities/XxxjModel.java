package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias("XxxjModel")
public class XxxjModel extends BaseBasicModel {



    //小结ID
    private String xjid;

    //小结类型
    private String xjlx;

    //小结名称
    private String xjmc;

    //小结内容
    private String xjnr;

    public String getXjid() {
        return xjid;
    }

    public void setXjid(String xjid) {
        this.xjid = xjid;
    }

    public String getXjlx() {
        return xjlx;
    }

    public void setXjlx(String xjlx) {
        this.xjlx = xjlx;
    }

    public String getXjmc() {
        return xjmc;
    }

    public void setXjmc(String xjmc) {
        this.xjmc = xjmc;
    }

    public String getXjnr() {
        return xjnr;
    }

    public void setXjnr(String xjnr) {
        this.xjnr = xjnr;
    }
}
