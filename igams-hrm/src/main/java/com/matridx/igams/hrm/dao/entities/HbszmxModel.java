package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author WYX
 * @version 1.0
 * @className HbszmxModel
 * @description TODO
 * @date 10:09 2022/12/30
 **/
@Alias(value = "HbszmxModel")
public class HbszmxModel extends BaseModel {

    private String hbmxid;    //红包设置明细id
    private String hbszid;    //红包设置id
    private String je;        //金额
    private String sl;        //数量
    private String sysl;    //剩余数量
    private String zdf;     //最低分
    private String sftz;    //是否通知
    private String tznr;    //通知内容
    private String zgf;     //最高分
    //倍数
    private String bs;

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getHbmxid() {
        return hbmxid;
    }

    public void setHbmxid(String hbmxid) {
        this.hbmxid = hbmxid;
    }

    public String getHbszid() {
        return hbszid;
    }

    public void setHbszid(String hbszid) {
        this.hbszid = hbszid;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getSysl() {
        return sysl;
    }

    public void setSysl(String sysl) {
        this.sysl = sysl;
    }

    public String getZdf() {
        return zdf;
    }

    public void setZdf(String zdf) {
        this.zdf = zdf;
    }

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

    public String getZgf() {
        return zgf;
    }

    public void setZgf(String zgf) {
        this.zgf = zgf;
    }

    private static final long serialVersionUID = 1L;
}
