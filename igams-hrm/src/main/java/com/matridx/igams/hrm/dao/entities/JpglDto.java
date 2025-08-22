package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value = "JpglDto")
public class JpglDto extends JpglModel {
    //奖品类型名称
    private String jplxmc;
    //通知群名称
    private String tzqmc;
    //所属部门名称
    private String ssbmmc;
    //录入人员名称
    private String lrrymc;
    //奖品类型 多
    private String[] jplxs;
    //通知群 多
    private String[] tzqs;
    //奖品明细
    private String jpmx_json;
	//通知群token
    private String Webhook;
    //群消息图片封面
    private String tpfm;
    //日期差
    private long rqc;
    private String copyflag;//复制标记

    public String getCopyflag() {
        return copyflag;
    }

    public void setCopyflag(String copyflag) {
        this.copyflag = copyflag;
    }

    public long getRqc() {
        return rqc;
    }

    public void setRqc(long rqc) {
        this.rqc = rqc;
    }

    public String getTpfm() {
        return tpfm;
    }

    public void setTpfm(String tpfm) {
        this.tpfm = tpfm;
    }

    public String getWebhook() {
        return Webhook;
    }

    public void setWebhook(String webhook) {
        Webhook = webhook;
    }

    public String getJpmx_json() {
        return jpmx_json;
    }

    public void setJpmx_json(String jpmx_json) {
        this.jpmx_json = jpmx_json;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String[] getJplxs() {
        return jplxs;
    }

    public void setJplxs(String[] jplxs) {
        this.jplxs = jplxs;
    }

    public String[] getTzqs() {
        return tzqs;
    }

    public void setTzqs(String[] tzqs) {
        this.tzqs = tzqs;
    }

    public String getJplxmc() {
        return jplxmc;
    }

    public void setJplxmc(String jplxmc) {
        this.jplxmc = jplxmc;
    }

    public String getTzqmc() {
        return tzqmc;
    }

    public void setTzqmc(String tzqmc) {
        this.tzqmc = tzqmc;
    }

    public String getSsbmmc() {
        return ssbmmc;
    }

    public void setSsbmmc(String ssbmmc) {
        this.ssbmmc = ssbmmc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
