package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjtssqDto")
public class SjtssqDto extends SjtssqModel{

    // 全部(查询条件)
    private String entire;
    //申请项目名称
    private String sqxmmc;
    //样本编号
    private String ybbh;
    //内部编码
    private String nbbm;
    //患者姓名
    private String hzxm;
    //性别
    private String xb;
    //单位简称
    private String dwjc;
    //检测项目名称
    private String jcxmmc;
    //检测子项目名称
    private String jczxmmc;
    //标本类型
    private String yblxmc;
    //报告日期
    private String bgrq;
    //合作伙伴
    private String db;
    //性别名称
    private String xbmc;
    //检索-标本类型
    private String[] yblxs;
    //检索-用途
    private String[] jcxms;
    //检索-用途
    private String[] sqxms;
    //申请开始日期
    private String sqrqstart;
    //申请结束日期
    private String sqrqend;
    //报告开始日期
    private String bgrqstart;
    //报告结束日期
    private String bgrqend;
    //状态[多]
    private List<String> zts;
    private String limitColumns;
    //年龄
    private String nl;
    //判断是否是个人清单 single_flag=1 为个人清单
    private String load_flag;
    //钉钉id
    private String ddid;
    //申请项目代码
    private String sqxmdm;
    //申请子项目
    private String sqzxmmc;
    //申请子项目代码
    private String sqzxmdm;
    //检测项目参数代码
    private String jcxmdm;
    //检测子项目参数代码
    private String jczxmdm;
    //申请原因名称
    private String sqyymc;
    //检索
    private String[] sqyys;
    //审批人ID
    private String sprid;
    //审批人姓名
    private String sprxm;
    //审批人用户名
    private String spryhm;
    //审批人角色ID
    private String sprjsid;
    //审批人角色名称
    private String sprjsmc;
    //检测单位限制
    private List<String> jcdwxz;
    //检测单位名称
    private String jcdwmc;
    //检测单位
    private String[] jcdws;
    //真实姓名
    private String zsxm;
    //接收日期
    private String jsrq;
    private String sqlParam;
    //导出提交人
    private String lrrymc;
    //岗位名称
    private String gwmc;
    public String getGwmc() {
        return gwmc;
    }
    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }
    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String[] getJcdws() {
        return jcdws;
    }

    public void setJcdws(String[] jcdws) {
        this.jcdws = jcdws;
    }

    public String getJcdwmc() {
        return jcdwmc;
    }

    public void setJcdwmc(String jcdwmc) {
        this.jcdwmc = jcdwmc;
    }

    public List<String> getJcdwxz() {
        return jcdwxz;
    }

    public void setJcdwxz(List<String> jcdwxz) {
        this.jcdwxz = jcdwxz;
    }

    public String getSprjsid() {
        return sprjsid;
    }

    public void setSprjsid(String sprjsid) {
        this.sprjsid = sprjsid;
    }

    public String getSprjsmc() {
        return sprjsmc;
    }

    public void setSprjsmc(String sprjsmc) {
        this.sprjsmc = sprjsmc;
    }

    public String getSprid() {
        return sprid;
    }

    public void setSprid(String sprid) {
        this.sprid = sprid;
    }

    public String getSprxm() {
        return sprxm;
    }

    public void setSprxm(String sprxm) {
        this.sprxm = sprxm;
    }

    public String getSpryhm() {
        return spryhm;
    }

    public void setSpryhm(String spryhm) {
        this.spryhm = spryhm;
    }

    public String getSprddid() {
        return sprddid;
    }

    public void setSprddid(String sprddid) {
        this.sprddid = sprddid;
    }

    //审批人钉钉ID
    private String sprddid;


    public String[] getSqyys() {
        return sqyys;
    }

    public void setSqyys(String[] sqyys) {
        this.sqyys = sqyys;
    }

    public String getSqyymc() {
        return sqyymc;
    }

    public void setSqyymc(String sqyymc) {
        this.sqyymc = sqyymc;
    }

    public String getJcxmdm() {
        return jcxmdm;
    }

    public void setJcxmdm(String jcxmdm) {
        this.jcxmdm = jcxmdm;
    }

    public String getSqzxmdm() {
        return sqzxmdm;
    }

    public void setSqzxmdm(String sqzxmdm) {
        this.sqzxmdm = sqzxmdm;
    }

    public String getSqzxmmc() {
        return sqzxmmc;
    }

    public void setSqzxmmc(String sqzxmmc) {
        this.sqzxmmc = sqzxmmc;
    }

    public String getSqxmdm() {
        return sqxmdm;
    }

    public void setSqxmdm(String sqxmdm) {
        this.sqxmdm = sqxmdm;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getLoad_flag() {
        return load_flag;
    }

    public void setLoad_flag(String load_flag) {
        this.load_flag = load_flag;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getLimitColumns() {
        return limitColumns;
    }

    public void setLimitColumns(String limitColumns) {
        this.limitColumns = limitColumns;
    }

    public List<String> getZts() {
        return zts;
    }

    public void setZts(List<String> zts) {
        this.zts = zts;
    }

    public String[] getYblxs() {
        return yblxs;
    }

    public void setYblxs(String[] yblxs) {
        this.yblxs = yblxs;
        for(int i=0;i<this.yblxs.length;i++)
        {
            this.yblxs[i] = this.yblxs[i].replace("'", "");
        }
    }

    public String[] getJcxms() {
        return jcxms;
    }

    public void setJcxms(String[] jcxms) {
        this.jcxms = jcxms;
        for(int i=0;i<this.jcxms.length;i++)
        {
            this.jcxms[i] = this.jcxms[i].replace("'", "");
        }
    }

    public String[] getSqxms() {
        return sqxms;
    }

    public void setSqxms(String[] sqxms) {
        this.sqxms = sqxms;
        for(int i=0;i<this.sqxms.length;i++)
        {
            this.sqxms[i] = this.sqxms[i].replace("'", "");
        }
    }

    public String getSqrqstart() {
        return sqrqstart;
    }

    public void setSqrqstart(String sqrqstart) {
        this.sqrqstart = sqrqstart;
    }

    public String getSqrqend() {
        return sqrqend;
    }

    public void setSqrqend(String sqrqend) {
        this.sqrqend = sqrqend;
    }

    public String getBgrqstart() {
        return bgrqstart;
    }

    public void setBgrqstart(String bgrqstart) {
        this.bgrqstart = bgrqstart;
    }

    public String getBgrqend() {
        return bgrqend;
    }

    public void setBgrqend(String bgrqend) {
        this.bgrqend = bgrqend;
    }

    public String getXbmc() {
        return xbmc;
    }

    public void setXbmc(String xbmc) {
        this.xbmc = xbmc;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getDwjc() {
        return dwjc;
    }

    public void setDwjc(String dwjc) {
        this.dwjc = dwjc;
    }

    public String getJcxmmc() {
        return jcxmmc;
    }

    public void setJcxmmc(String jcxmmc) {
        this.jcxmmc = jcxmmc;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public String getBgrq() {
        return bgrq;
    }

    public void setBgrq(String bgrq) {
        this.bgrq = bgrq;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getSqxmmc() {
        return sqxmmc;
    }

    public void setSqxmmc(String sqxmmc) {
        this.sqxmmc = sqxmmc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getJczxmmc() {
        return jczxmmc;
    }

    public void setJczxmmc(String jczxmmc) {
        this.jczxmmc = jczxmmc;
    }

    public String getJczxmdm() {
        return jczxmdm;
    }

    public void setJczxmdm(String jczxmdm) {
        this.jczxmdm = jczxmdm;
    }
}
