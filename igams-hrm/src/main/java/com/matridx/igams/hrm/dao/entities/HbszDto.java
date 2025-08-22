package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="HbszDto")
public class HbszDto extends HbszModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 266688237510823978L;
	//通知群名称
    private String tzqmc;
    //录入人员名称
    private String lrrymc;
    //红包设置明细json
    private String hbszmx_json;
    //修改前红包设置明细json
    private String xgqhbszmx_json;
    //任务ids
    private String rwids;
    //模糊查询
    private String entire;
    //培训id
    private String pxid;
    //得分
    private String df;
    //个人考试id
    private String grksid;
    private String Webhook;
//样式名称
    private String ysmc;
    //红包封面
    private String tpfm;
    //人员id
    private String ryid;
    //钉钉头像路径
    private String ddtxlj;
    //工作id
    private String gzid;
    //钉钉id
    private String ddid;

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getGzid() {
        return gzid;
    }

    public void setGzid(String gzid) {
        this.gzid = gzid;
    }

    public String getDdtxlj() {
        return ddtxlj;
    }

    public void setDdtxlj(String ddtxlj) {
        this.ddtxlj = ddtxlj;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

    public String getTpfm() {
        return tpfm;
    }

    public void setTpfm(String tpfm) {
        this.tpfm = tpfm;
    }

    public String getYsmc() {
        return ysmc;
    }

    public void setYsmc(String ysmc) {
        this.ysmc = ysmc;
    }
    public String getWebhook() {
        return Webhook;
    }

    public void setWebhook(String webhook) {
        Webhook = webhook;
    }

    public String getGrksid() {
        return grksid;
    }

    public void setGrksid(String grksid) {
        this.grksid = grksid;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }
    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getRwids() {
        return rwids;
    }

    public void setRwids(String rwids) {
        this.rwids = rwids;
    }

    public String getXgqhbszmx_json() {
        return xgqhbszmx_json;
    }

    public void setXgqhbszmx_json(String xgqhbszmx_json) {
        this.xgqhbszmx_json = xgqhbszmx_json;
    }

    public String getHbszmx_json() {
        return hbszmx_json;
    }

    public void setHbszmx_json(String hbszmx_json) {
        this.hbszmx_json = hbszmx_json;
    }

    public String getTzqmc() {
        return tzqmc;
    }

    public void setTzqmc(String tzqmc) {
        this.tzqmc = tzqmc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }
}
