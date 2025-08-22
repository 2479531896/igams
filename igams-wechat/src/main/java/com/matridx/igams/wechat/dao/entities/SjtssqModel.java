package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjtssqModel")
public class SjtssqModel extends BaseBasicModel {

    //特殊申请id
    private String tssqid;
    //送检id
    private String sjid;
    //申请项目
    private String sqxm;
    //申请子项目
    private String sqzxm;
    //处理标记
    private String clbj;
    //备注
    private String bz;
    //状态
    private String zt;
    //申请原因
    private String sqyy;
    //其他申请原因
    private String qtsqyy;
    //钉钉实例ID
    private String ddslid;
    //通过时间
    private String tgsj;
    //驳回理由
    private String bhly;
    //检测项目ID
    private String jcxmid;
    //检测子项目ID
    private String jczxmid;

    public String getJcxmid() {
        return jcxmid;
    }

    public void setJcxmid(String jcxmid) {
        this.jcxmid = jcxmid;
    }

    public String getJczxmid() {
        return jczxmid;
    }

    public void setJczxmid(String jczxmid) {
        this.jczxmid = jczxmid;
    }

    public String getBhly() {
        return bhly;
    }

    public void setBhly(String bhly) {
        this.bhly = bhly;
    }

    public String getTgsj() {
        return tgsj;
    }

    public void setTgsj(String tgsj) {
        this.tgsj = tgsj;
    }

    public String getDdslid() {
        return ddslid;
    }

    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }

    public String getSqyy() {
        return sqyy;
    }

    public void setSqyy(String sqyy) {
        this.sqyy = sqyy;
    }

    public String getQtsqyy() {
        return qtsqyy;
    }

    public void setQtsqyy(String qtsqyy) {
        this.qtsqyy = qtsqyy;
    }

    public String getSqzxm() {
        return sqzxm;
    }

    public void setSqzxm(String sqzxm) {
        this.sqzxm = sqzxm;
    }

    public String getTssqid() {
        return tssqid;
    }

    public void setTssqid(String tssqid) {
        this.tssqid = tssqid;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getSqxm() {
        return sqxm;
    }

    public void setSqxm(String sqxm) {
        this.sqxm = sqxm;
    }

    public String getClbj() {
        return clbj;
    }

    public void setClbj(String clbj) {
        this.clbj = clbj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
