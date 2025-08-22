package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "YhkqxxDto")
public class YhkqxxDto extends YhkqxxModel {
    private String rqstart;
    private String rqend;
    private String cqsjstart;
    private String cqsjend;
    private String tqsjstart;
    private String tqsjend;
    private String dkrq;
    private String sqlParam;
    private String yhmc;
    private String yyhid;
    private String jgmc;
    private String spid;
    private String qjlxmc;
    private String jbyy;
    private String yhm;//用户名
    private String zsxm;//真实姓名
    private String rzrq;//入职日期
    private String zc;//职称
    private String entire;//查询
    private String ddid;//钉钉id
    private String sfqq;//是否全勤
    private String btmc;//补贴名称
    private String bcmc;//班次名称
    private String procInstId;//关联审批实例id
    private String bcmc_t;//补贴加班班次名称
    private String sbdksj;//班次上班打卡时间
    private String xbdksj;//班次下班打卡时间
    private String bzsc;//标准时长
    private String bzsj;//标准时间
    private String dzkssj;//递增开始时间
    private String sjxs;//事假(小时)
    private String bjxs;//病假(小时)
    private String njxs;//年假(小时)
    private String txxs;//调休(小时)
    private String hjts;//婚假(天)
    private String cjts;//产假(天)
    private String hljts;//护理假(天)
    private String cjjts;//产检假(天)
    private String sjts;//丧假(天)
    private String yejts;//育儿假(天)
    private String dsznphfmjts;//独生子女陪护父母假(天)

    public String getSjxs() {
        return sjxs;
    }

    public void setSjxs(String sjxs) {
        this.sjxs = sjxs;
    }

    public String getBjxs() {
        return bjxs;
    }

    public void setBjxs(String bjxs) {
        this.bjxs = bjxs;
    }

    public String getNjxs() {
        return njxs;
    }

    public void setNjxs(String njxs) {
        this.njxs = njxs;
    }

    public String getTxxs() {
        return txxs;
    }

    public void setTxxs(String txxs) {
        this.txxs = txxs;
    }

    public String getHjts() {
        return hjts;
    }

    public void setHjts(String hjts) {
        this.hjts = hjts;
    }

    public String getCjts() {
        return cjts;
    }

    public void setCjts(String cjts) {
        this.cjts = cjts;
    }

    public String getHljts() {
        return hljts;
    }

    public void setHljts(String hljts) {
        this.hljts = hljts;
    }

    public String getCjjts() {
        return cjjts;
    }

    public void setCjjts(String cjjts) {
        this.cjjts = cjjts;
    }

    public String getSjts() {
        return sjts;
    }

    public void setSjts(String sjts) {
        this.sjts = sjts;
    }

    public String getYejts() {
        return yejts;
    }

    public void setYejts(String yejts) {
        this.yejts = yejts;
    }

    public String getDsznphfmjts() {
        return dsznphfmjts;
    }

    public void setDsznphfmjts(String dsznphfmjts) {
        this.dsznphfmjts = dsznphfmjts;
    }

    public String getSbdksj() {
        return sbdksj;
    }

    public void setSbdksj(String sbdksj) {
        this.sbdksj = sbdksj;
    }

    public String getXbdksj() {
        return xbdksj;
    }

    public void setXbdksj(String xbdksj) {
        this.xbdksj = xbdksj;
    }

    public String getBzsc() {
        return bzsc;
    }

    public void setBzsc(String bzsc) {
        this.bzsc = bzsc;
    }

    public String getBzsj() {
        return bzsj;
    }

    public void setBzsj(String bzsj) {
        this.bzsj = bzsj;
    }

    public String getDzkssj() {
        return dzkssj;
    }

    public void setDzkssj(String dzkssj) {
        this.dzkssj = dzkssj;
    }

    public String getBcmc_t() {
        return bcmc_t;
    }

    public void setBcmc_t(String bcmc_t) {
        this.bcmc_t = bcmc_t;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getBtmc() {
        return btmc;
    }

    public void setBtmc(String btmc) {
        this.btmc = btmc;
    }

    public String getBcmc() {
        return bcmc;
    }

    public void setBcmc(String bcmc) {
        this.bcmc = bcmc;
    }

    public String getSfqq() {
        return sfqq;
    }

    public void setSfqq(String sfqq) {
        this.sfqq = sfqq;
    }

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getRzrq() {
        return rzrq;
    }

    public void setRzrq(String rzrq) {
        this.rzrq = rzrq;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getZsxm() {
        return zsxm;
    }

    public void setZsxm(String zsxm) {
        this.zsxm = zsxm;
    }

    public String getJbyy() {
        return jbyy;
    }

    public void setJbyy(String jbyy) {
        this.jbyy = jbyy;
    }


    public String getQjlxmc() {
        return qjlxmc;
    }

    public void setQjlxmc(String qjlxmc) {
        this.qjlxmc = qjlxmc;
    }


    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }


    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }


    public String getYyhid() {
        return yyhid;
    }

    public void setYyhid(String yyhid) {
        this.yyhid = yyhid;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.yhmc = yhmc;
    }

    public String getRqstart() {
        return rqstart;
    }

    public void setRqstart(String rqstart) {
        this.rqstart = rqstart;
    }

    public String getRqend() {
        return rqend;
    }

    public void setRqend(String rqend) {
        this.rqend = rqend;
    }

    public String getCqsjstart() {
        return cqsjstart;
    }

    public void setCqsjstart(String cqsjstart) {
        this.cqsjstart = cqsjstart;
    }

    public String getCqsjend() {
        return cqsjend;
    }

    public void setCqsjend(String cqsjend) {
        this.cqsjend = cqsjend;
    }

    public String getTqsjstart() {
        return tqsjstart;
    }

    public void setTqsjstart(String tqsjstart) {
        this.tqsjstart = tqsjstart;
    }

    public String getTqsjend() {
        return tqsjend;
    }

    public void setTqsjend(String tqsjend) {
        this.tqsjend = tqsjend;
    }

    public String getDkrq() {
        return dkrq;
    }

    public void setDkrq(String dkrq) {
        this.dkrq = dkrq;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
