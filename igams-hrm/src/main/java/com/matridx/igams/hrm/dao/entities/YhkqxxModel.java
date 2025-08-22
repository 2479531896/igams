package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "YhkqxxModel")
public class YhkqxxModel extends BaseModel {
    //用户ID
    private String yhid;
    //备注
    private String bz;
    private String rq;
    private String kqid;
    //出勤时间
    private String cqsj;
    //退勤时间
    private String tqsj;
    //外部程序id
    private String wbcxid;
    private String qjkssj;//请假开始时间
    private String qjjssj;//请假结束时间
    private String qjsc;//请假时长
    private String qjlx;//请假类型
    private String cqzt;//出勤状态
    private String gzsc;//工作时长
    private String qjcs;//请假次数
    private String jbkssj;//加班开始时间
    private String jbjssj;//加班结束时间
    private String sc;//加班时长
    private String sffdjr;//是否法定假日
    private String hsfs;//核算方式
    private String ycqts;//应出勤天数
    private String xxts;//休息天数
    private String cdcs;//迟到次数
    private String cdsc;//迟到时长
    private String kgts;//旷工天数
    private String ztcs;//早退次数
    private String ztsc;//早退时长
    private String sbqkcs;//上班缺卡次数
    private String xbqkcs;//下班缺卡次数
    private String wcsc;//外出时长
    private String ccsc;//出差时长
    private String jbzsc;//加班总时长
    private String xxrjb;//休息日加班
    private String gzrjb;//工作日加班
    private String jjrjb;//节假日加班
    private String kqjg;//	考勤结
    private String cqts;//出勤天数
    private String kqlx;//考勤类型
    private String kqz;//考勤组
    private String btje;//补贴金额
    private String bcid;//班次id
    private String bcglid;//班次管理id
    private String btglid;//补贴管理id

    public String getBtje() {
        return btje;
    }

    public void setBtje(String btje) {
        this.btje = btje;
    }

    public String getBcid() {
        return bcid;
    }

    public void setBcid(String bcid) {
        this.bcid = bcid;
    }

    public String getBcglid() {
        return bcglid;
    }

    public void setBcglid(String bcglid) {
        this.bcglid = bcglid;
    }

    public String getBtglid() {
        return btglid;
    }

    public void setBtglid(String btglid) {
        this.btglid = btglid;
    }

    public String getKqlx() {
        return kqlx;
    }

    public void setKqlx(String kqlx) {
        this.kqlx = kqlx;
    }

    public String getKqz() {
        return kqz;
    }

    public void setKqz(String kqz) {
        this.kqz = kqz;
    }

    public String getQjkssj() {
        return qjkssj;
    }

    public void setQjkssj(String qjkssj) {
        this.qjkssj = qjkssj;
    }

    public String getQjjssj() {
        return qjjssj;
    }

    public void setQjjssj(String qjjssj) {
        this.qjjssj = qjjssj;
    }

    public String getQjsc() {
        return qjsc;
    }

    public void setQjsc(String qjsc) {
        this.qjsc = qjsc;
    }

    public String getQjlx() {
        return qjlx;
    }

    public void setQjlx(String qjlx) {
        this.qjlx = qjlx;
    }

    public String getCqzt() {
        return cqzt;
    }

    public void setCqzt(String cqzt) {
        this.cqzt = cqzt;
    }

    public String getGzsc() {
        return gzsc;
    }

    public void setGzsc(String gzsc) {
        this.gzsc = gzsc;
    }

    public String getQjcs() {
        return qjcs;
    }

    public void setQjcs(String qjcs) {
        this.qjcs = qjcs;
    }

    public String getJbkssj() {
        return jbkssj;
    }

    public void setJbkssj(String jbkssj) {
        this.jbkssj = jbkssj;
    }

    public String getJbjssj() {
        return jbjssj;
    }

    public void setJbjssj(String jbjssj) {
        this.jbjssj = jbjssj;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSffdjr() {
        return sffdjr;
    }

    public void setSffdjr(String sffdjr) {
        this.sffdjr = sffdjr;
    }

    public String getHsfs() {
        return hsfs;
    }

    public void setHsfs(String hsfs) {
        this.hsfs = hsfs;
    }

    public String getYcqts() {
        return ycqts;
    }

    public void setYcqts(String ycqts) {
        this.ycqts = ycqts;
    }

    public String getXxts() {
        return xxts;
    }

    public void setXxts(String xxts) {
        this.xxts = xxts;
    }

    public String getCdcs() {
        return cdcs;
    }

    public void setCdcs(String cdcs) {
        this.cdcs = cdcs;
    }

    public String getCdsc() {
        return cdsc;
    }

    public void setCdsc(String cdsc) {
        this.cdsc = cdsc;
    }

    public String getKgts() {
        return kgts;
    }

    public void setKgts(String kgts) {
        this.kgts = kgts;
    }

    public String getZtcs() {
        return ztcs;
    }

    public void setZtcs(String ztcs) {
        this.ztcs = ztcs;
    }

    public String getZtsc() {
        return ztsc;
    }

    public void setZtsc(String ztsc) {
        this.ztsc = ztsc;
    }

    public String getSbqkcs() {
        return sbqkcs;
    }

    public void setSbqkcs(String sbqkcs) {
        this.sbqkcs = sbqkcs;
    }

    public String getXbqkcs() {
        return xbqkcs;
    }

    public void setXbqkcs(String xbqkcs) {
        this.xbqkcs = xbqkcs;
    }

    public String getWcsc() {
        return wcsc;
    }

    public void setWcsc(String wcsc) {
        this.wcsc = wcsc;
    }

    public String getCcsc() {
        return ccsc;
    }

    public void setCcsc(String ccsc) {
        this.ccsc = ccsc;
    }

    public String getJbzsc() {
        return jbzsc;
    }

    public void setJbzsc(String jbzsc) {
        this.jbzsc = jbzsc;
    }

    public String getXxrjb() {
        return xxrjb;
    }

    public void setXxrjb(String xxrjb) {
        this.xxrjb = xxrjb;
    }

    public String getGzrjb() {
        return gzrjb;
    }

    public void setGzrjb(String gzrjb) {
        this.gzrjb = gzrjb;
    }

    public String getJjrjb() {
        return jjrjb;
    }

    public void setJjrjb(String jjrjb) {
        this.jjrjb = jjrjb;
    }

    public String getKqjg() {
        return kqjg;
    }

    public void setKqjg(String kqjg) {
        this.kqjg = kqjg;
    }

    public String getCqts() {
        return cqts;
    }

    public void setCqts(String cqts) {
        this.cqts = cqts;
    }

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }

    public String getCqsj() {
        return cqsj;
    }

    public void setCqsj(String cqsj) {
        this.cqsj = cqsj;
    }

    public String getTqsj() {
        return tqsj;
    }

    public void setTqsj(String tqsj) {
        this.tqsj = tqsj;
    }

    public String getKqid() {
        return kqid;
    }

    public void setKqid(String kqid) {
        this.kqid = kqid;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }


    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
