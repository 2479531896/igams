package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "JpmxDto")
public class JpmxDto extends JpmxModel {

    //附件ID
    private String fjid;
    //文件名
    private String wjm;
	//通知群token
    private String Webhook;
    //通知群名称
    private String tzqmc;
    //群消息图片封面
    private String tpfm;
    //日期差
    private long rqc;
    //文件路径
    private String wjlj;
    //通知群参数扩展1
    private String tzqcskz1;
    //复制标记
    private String copyflag;

    public String getCopyflag() {
        return copyflag;
    }

    public void setCopyflag(String copyflag) {
        this.copyflag = copyflag;
    }

    public String getTzqcskz1() {
        return tzqcskz1;
    }

    public void setTzqcskz1(String tzqcskz1) {
        this.tzqcskz1 = tzqcskz1;
    }
    public String getWjlj() {
        return wjlj;
    }

    public void setWjlj(String wjlj) {
        this.wjlj = wjlj;
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

    public String getTzqmc() {
        return tzqmc;
    }

    public void setTzqmc(String tzqmc) {
        this.tzqmc = tzqmc;
    }

    public String getWjm() {
        return wjm;
    }

    public void setWjm(String wjm) {
        this.wjm = wjm;
    }

    public String getFjid() {
        return fjid;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
