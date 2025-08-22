package com.matridx.igams.hrm.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="GrksmxDto")
public class GrksmxDto  extends GrksmxModel {
    //题目内容
    private String tmnr;
    //答案
    private String da;
    //得分
    private String fs;
    //姓名
    private String xm;
    //培训标题
    private String pxbt;
    //总分
    private String zf;
    //钉钉ID
    private String ddid;
    private String SqlParam;//导出关联标记位//所选择的字段
    //题目类型
    private String tmlx;
    //选项集合
    private List<KsmxDto> xxlist;
    //机构名称
    private String jgmc;
    //用户名
    private String yhm;
    //地区
    private String dq;
    //答案解析
    private String dajx;
    //题目类型名称
    private String tmlxmc;
    //整体考试开始时间
    private String ztkskssj;
    //整体考试结束时间
	//通过分数
    private String tgfs;
    //所属公司
    private String ssgs;
    //是否合格
    private String sfhg;
    //最低分
    private String minscore;
    //最高分
    private String maxscore;
    //是否合格多
    private String[] sfhgs;
    //考试开始时间
    private String kssjstart;
    //考试结束时间
    private String kssjend;
    private String[] ssgss;

    public String[] getSsgss() {
        return ssgss;
    }

    public void setSsgss(String[] ssgss) {
        this.ssgss = ssgss;
    }

    public String getKssjstart() {
        return kssjstart;
    }

    public void setKssjstart(String kssjstart) {
        this.kssjstart = kssjstart;
    }

    public String getKssjend() {
        return kssjend;
    }

    public void setKssjend(String kssjend) {
        this.kssjend = kssjend;
    }

    public String getMinscore() {
        return minscore;
    }

    public void setMinscore(String minscore) {
        this.minscore = minscore;
    }

    public String getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(String maxscore) {
        this.maxscore = maxscore;
    }

    public String[] getSfhgs() {
        return sfhgs;
    }

    public void setSfhgs(String[] sfhgs) {
        this.sfhgs = sfhgs;
    }

    public String getSfhg() {
        return sfhg;
    }

    public void setSfhg(String sfhg) {
        this.sfhg = sfhg;
    }

    public String getSsgs() {
        return ssgs;
    }

    public void setSsgs(String ssgs) {
        this.ssgs = ssgs;
    }

    public String getTgfs() {
        return tgfs;
    }

    public void setTgfs(String tgfs) {
        this.tgfs = tgfs;
    }
    private String ztksjssj;

    public String getZtkskssj() {
        return ztkskssj;
    }

    public void setZtkskssj(String ztkskssj) {
        this.ztkskssj = ztkskssj;
    }

    public String getZtksjssj() {
        return ztksjssj;
    }

    public void setZtksjssj(String ztksjssj) {
        this.ztksjssj = ztksjssj;
    }

    public String getTmlxmc() {
        return tmlxmc;
    }

    public void setTmlxmc(String tmlxmc) {
        this.tmlxmc = tmlxmc;
    }

    public String getDajx() {
        return dajx;
    }

    public void setDajx(String dajx) {
        this.dajx = dajx;
    }

    public String getDq() {
        return dq;
    }

    public void setDq(String dq) {
        this.dq = dq;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public List<KsmxDto> getXxlist() {
        return xxlist;
    }

    public void setXxlist(List<KsmxDto> xxlist) {
        this.xxlist = xxlist;
    }

    public String getTmlx() {
        return tmlx;
    }

    public void setTmlx(String tmlx) {
        this.tmlx = tmlx;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getPxbt() {
        return pxbt;
    }

    public void setPxbt(String pxbt) {
        this.pxbt = pxbt;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getTmnr() {
        return tmnr;
    }

    public void setTmnr(String tmnr) {
        this.tmnr = tmnr;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }


    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
