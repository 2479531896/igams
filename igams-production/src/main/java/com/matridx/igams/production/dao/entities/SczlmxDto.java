package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("SczlmxDto")
public class SczlmxDto extends SczlmxModel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String cpdm;
    private String zldh;
    private String xqdh;
    private String wlbm;
    private String wlmc;
    private String gg;
    private String scs;
    private String jldw;
    //可引用数量
    private String kyysl;
    //引用数量标记
    private String yyslbj;
    //录入时间开始
    private String lrsjstart;
    //录入时间结束
    private String lrsjend;
    //录入时间开始 月
    private String lrsjMstart;
    //录入时间结束
    private String lrsjMend;
    //录入时间开始 年
    private String lrsjYstart;
    //录入时间结束
    private String lrsjYend;
    //条件
    private String tj;
    //周期
    private String zq;
    //日期
    private String rq;
    //条数
    private String count;
    //检索日期范围复数
    private List<String> rqs;
    //年月日
    private String method;
    //类型
    private String lx;
    private String dhsl;//到货数量
    private String scgs;//生产工时
    private String zjgs;//质检工时
    private String sjwcsj;//实际完成时间
    private String sjwctime;
    private String sfcs;//是否超时
    private String ztmc;
    private String kcl;//库存量
    private String flag;
    private String entire;
    private String cpbh;//产品编号
    private String jhcl;//计划产量
    private String bz;//备注
    private String bctj;//保存条件

    public String getBctj() {
        return bctj;
    }

    public void setBctj(String bctj) {
        this.bctj = bctj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCpbh() {
        return cpbh;
    }

    public void setCpbh(String cpbh) {
        this.cpbh = cpbh;
    }

    public String getJhcl() {
        return jhcl;
    }

    public void setJhcl(String jhcl) {
        this.jhcl = jhcl;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDhsl() {
        return dhsl;
    }

    public void setDhsl(String dhsl) {
        this.dhsl = dhsl;
    }

    public String getScgs() {
        return scgs;
    }

    public void setScgs(String scgs) {
        this.scgs = scgs;
    }

    public String getZjgs() {
        return zjgs;
    }

    public void setZjgs(String zjgs) {
        this.zjgs = zjgs;
    }

    public String getSjwcsj() {
        return sjwcsj;
    }

    public void setSjwcsj(String sjwcsj) {
        this.sjwcsj = sjwcsj;
    }

    public String getSjwctime() {
        return sjwctime;
    }

    public void setSjwctime(String sjwctime) {
        this.sjwctime = sjwctime;
    }

    public String getSfcs() {
        return sfcs;
    }

    public void setSfcs(String sfcs) {
        this.sfcs = sfcs;
    }

    public String getZtmc() {
        return ztmc;
    }

    public void setZtmc(String ztmc) {
        this.ztmc = ztmc;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getRqs() {
        return rqs;
    }

    public void setRqs(List<String> rqs) {
        this.rqs = rqs;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getZq() {
        return zq;
    }

    public void setZq(String zq) {
        this.zq = zq;
    }

    public String getLrsjMstart() {
        return lrsjMstart;
    }

    public void setLrsjMstart(String lrsjMstart) {
        this.lrsjMstart = lrsjMstart;
    }

    public String getLrsjMend() {
        return lrsjMend;
    }

    public void setLrsjMend(String lrsjMend) {
        this.lrsjMend = lrsjMend;
    }

    public String getLrsjYstart() {
        return lrsjYstart;
    }

    public void setLrsjYstart(String lrsjYstart) {
        this.lrsjYstart = lrsjYstart;
    }

    public String getLrsjYend() {
        return lrsjYend;
    }

    public void setLrsjYend(String lrsjYend) {
        this.lrsjYend = lrsjYend;
    }

    public String getTj() {
        return tj;
    }

    public void setTj(String tj) {
        this.tj = tj;
    }

    public String getLrsjstart() {
        return lrsjstart;
    }

    public void setLrsjstart(String lrsjstart) {
        this.lrsjstart = lrsjstart;
    }

    public String getLrsjend() {
        return lrsjend;
    }

    public void setLrsjend(String lrsjend) {
        this.lrsjend = lrsjend;
    }

    public String getYyslbj() {
        return yyslbj;
    }

    public void setYyslbj(String yyslbj) {
        this.yyslbj = yyslbj;
    }

    public String getKyysl() {
        return kyysl;
    }

    public void setKyysl(String kyysl) {
        this.kyysl = kyysl;
    }

    public String getZldh() {
        return zldh;
    }

    public void setZldh(String zldh) {
        this.zldh = zldh;
    }

    public String getXqdh() {
        return xqdh;
    }

    public void setXqdh(String xqdh) {
        this.xqdh = xqdh;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getCpdm() {
        return cpdm;
    }

    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }
}
