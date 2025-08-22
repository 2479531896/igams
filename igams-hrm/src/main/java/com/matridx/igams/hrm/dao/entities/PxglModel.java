package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="PxglModel")
public class PxglModel extends BaseBasicModel {
    //培训id
    private String pxid;
    //培训类别
    private String pxlb;
    //培训子类别
    private String pxzlb;
    //培训标题
    private String pxbt;
    //培训内容
    private String pxnr;
    //过期时间
    private String gqsj;
    //是否过期
    private String sfgq;
    //业务类型
    private String ywlx;
    //视频路径
    private String spwjlj;
    //是否测试
    private String sfcs;
    //确认人员
    private String qrry;
    //查看答案标记
    private String ckdabj;
    //当场重考标记
    private String dcckbj;
    //回退标记
    private String htbj;
    //跳转方式标记
    private String tzfsbj;
    //视频标记
    private String spbj;
    //所属公司
    private String ssgs;
//视频下载
    private String spxz;
    //状态
    private String zt;
    //部门
    private String bm;
    //到期提醒
    private String dqtx;
	//是否发送红包
    private String sffshb;
    //红包设置id
    private String hbszid;
    //课时
    private String ks;
    //培训方式
    private String pxfs;
    //是否限制签到
    private String sfxzqd;
    //年度培训ID
    private String ndpxid;
    //是否公开
    private String sfgk;

    public String getSfgk() {
        return sfgk;
    }

    public void setSfgk(String sfgk) {
        this.sfgk = sfgk;
    }

    public String getNdpxid() {
        return ndpxid;
    }

    public void setNdpxid(String ndpxid) {
        this.ndpxid = ndpxid;
    }

    public String getSfxzqd() {
        return sfxzqd;
    }

    public void setSfxzqd(String sfxzqd) {
        this.sfxzqd = sfxzqd;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getPxfs() {
        return pxfs;
    }

    public void setPxfs(String pxfs) {
        this.pxfs = pxfs;
    }

    public String getSffshb() {
        return sffshb;
    }

    public void setSffshb(String sffshb) {
        this.sffshb = sffshb;
    }

    public String getHbszid() {
        return hbszid;
    }

    public void setHbszid(String hbszid) {
        this.hbszid = hbszid;
    }
    public String getDqtx() {
        return dqtx;
    }

    public void setDqtx(String dqtx) {
        this.dqtx = dqtx;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    //年份
    private String year;

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getSpxz() {
        return spxz;
    }

    public void setSpxz(String spxz) {
        this.spxz = spxz;
    }
    public String getSsgs() {
        return ssgs;
    }

    public void setSsgs(String ssgs) {
        this.ssgs = ssgs;
    }

    public String getCkdabj() {
        return ckdabj;
    }

    public void setCkdabj(String ckdabj) {
        this.ckdabj = ckdabj;
    }

    public String getDcckbj() {
        return dcckbj;
    }

    public void setDcckbj(String dcckbj) {
        this.dcckbj = dcckbj;
    }

    public String getHtbj() {
        return htbj;
    }

    public void setHtbj(String htbj) {
        this.htbj = htbj;
    }

    public String getTzfsbj() {
        return tzfsbj;
    }

    public void setTzfsbj(String tzfsbj) {
        this.tzfsbj = tzfsbj;
    }

    public String getSpbj() {
        return spbj;
    }

    public void setSpbj(String spbj) {
        this.spbj = spbj;
    }

    public String getQrry() {
        return qrry;
    }

    public void setQrry(String qrry) {
        this.qrry = qrry;
    }

    public String getSfcs() {
        return sfcs;
    }

    public void setSfcs(String sfcs) {
        this.sfcs = sfcs;
    }

    public String getSpwjlj() {
        return spwjlj;
    }

    public void setSpwjlj(String spwjlj) {
        this.spwjlj = spwjlj;
    }

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    public String getPxlb() {
        return pxlb;
    }

    public void setPxlb(String pxlb) {
        this.pxlb = pxlb;
    }

    public String getPxzlb() {
        return pxzlb;
    }

    public void setPxzlb(String pxzlb) {
        this.pxzlb = pxzlb;
    }

    public String getPxbt() {
        return pxbt;
    }

    public void setPxbt(String pxbt) {
        this.pxbt = pxbt;
    }

    public String getPxnr() {
        return pxnr;
    }

    public void setPxnr(String pxnr) {
        this.pxnr = pxnr;
    }

    public String getGqsj() {
        return gqsj;
    }

    public void setGqsj(String gqsj) {
        this.gqsj = gqsj;
    }

    public String getSfgq() {
        return sfgq;
    }

    public void setSfgq(String sfgq) {
        this.sfgq = sfgq;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
