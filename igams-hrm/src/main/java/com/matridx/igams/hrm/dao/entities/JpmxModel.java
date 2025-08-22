package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JpmxModel")
public class JpmxModel extends BaseModel {

    //奖品明细id
    private String jpmxid;
    //奖品管理id
    private String jpglid;
    //数量
    private String sl;
    //倍数
    private String bs;
    //奖品名称
    private String jpmc;
    //序号
    private String xh;
    //剩余数量
    private String sysl;
    //是否通知
    private String sftz;
    //通知内容
    private String tznr;

    public String getSftz() {
        return sftz;
    }

    public void setSftz(String sftz) {
        this.sftz = sftz;
    }

    public String getTznr() {
        return tznr;
    }

    public void setTznr(String tznr) {
        this.tznr = tznr;
    }

    public String getJpmxid() {
        return jpmxid;
    }

    public void setJpmxid(String jpmxid) {
        this.jpmxid = jpmxid;
    }

    public String getJpglid() {
        return jpglid;
    }

    public void setJpglid(String jpglid) {
        this.jpglid = jpglid;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getJpmc() {
        return jpmc;
    }

    public void setJpmc(String jpmc) {
        this.jpmc = jpmc;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getSysl() {
        return sysl;
    }

    public void setSysl(String sysl) {
        this.sysl = sysl;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
