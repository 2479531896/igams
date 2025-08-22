package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value = "ZlxyDto")
public class ZlxyDto extends ZlxyModel{
    private String gfbh;//供方编号
    private String gysmc;//供应商
    private String gfgllbmc;//供方管理类别
    private String cjsjstart;
    private String cjsjend;
    private String kssjstart;
    private String kssjend;
    private String dqsjstart;
    private String dqsjend;
    private String wlbm;//物料编码
    private String wlmc;//物料名称
    private String sccj;//生产厂家
    private String sjxmh;//涉及项目号
    private String jszb;//技术指标
    private String zlyq;//质量要求
    private String ysbz;//验收标准
    private String bctj;//保存条件
    private String[] gfgllbs;//供方管理类别多
    private String[] htxjs;//合同新旧多
    private String lrrymc;//录入人员
    private String xgrymc;//修改人员
    private String fzrmc;//负责人
    private String entire;
    private String zlxymx_json;//明细Json
    //附件ids
    private List<String> fjids;
    private String sqlParam;
    private String sprjsmc;//审批人角色名称
    private String sprjsid;//审批人角色id
    private String sqbm;//申请部门
    private String csrs;//抄送人
    private String gg;
    private String jldw;
    private String bzq;
    private String scs;
    private String ychh;
    private String ygysid;//原供应商id
    private String sfgq;//是否过期
    private String[] sjxmhs;
    private String index;
    private String[] szbjs;//双章标记
    private String htlxmc;//合同类型
    private String yyxybh;//引用协议编号

    public String getYyxybh() {
        return yyxybh;
    }

    public void setYyxybh(String yyxybh) {
        this.yyxybh = yyxybh;
    }

    public String getHtlxmc() {
        return htlxmc;
    }

    public void setHtlxmc(String htlxmc) {
        this.htlxmc = htlxmc;
    }

    public String[] getSzbjs() {
        return szbjs;
    }

    public void setSzbjs(String[] szbjs) {
        this.szbjs = szbjs;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String[] getSjxmhs() {
        return sjxmhs;
    }

    public void setSjxmhs(String[] sjxmhs) {
        this.sjxmhs = sjxmhs;
    }

    public String getSfgq() {
        return sfgq;
    }

    public void setSfgq(String sfgq) {
        this.sfgq = sfgq;
    }

    public String getYgysid() {
        return ygysid;
    }

    public void setYgysid(String ygysid) {
        this.ygysid = ygysid;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getBzq() {
        return bzq;
    }

    public void setBzq(String bzq) {
        this.bzq = bzq;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    public String getYchh() {
        return ychh;
    }

    public void setYchh(String ychh) {
        this.ychh = ychh;
    }

    public String getCsrs() {
        return csrs;
    }

    public void setCsrs(String csrs) {
        this.csrs = csrs;
    }

    public String getSqbm() {
        return sqbm;
    }

    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public String getSprjsmc() {
        return sprjsmc;
    }

    public void setSprjsmc(String sprjsmc) {
        this.sprjsmc = sprjsmc;
    }

    public String getSprjsid() {
        return sprjsid;
    }

    public void setSprjsid(String sprjsid) {
        this.sprjsid = sprjsid;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getZlxymx_json() {
        return zlxymx_json;
    }

    public void setZlxymx_json(String zlxymx_json) {
        this.zlxymx_json = zlxymx_json;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getFzrmc() {
        return fzrmc;
    }

    public void setFzrmc(String fzrmc) {
        this.fzrmc = fzrmc;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getXgrymc() {
        return xgrymc;
    }

    public void setXgrymc(String xgrymc) {
        this.xgrymc = xgrymc;
    }

    public String[] getGfgllbs() {
        return gfgllbs;
    }

    public void setGfgllbs(String[] gfgllbs) {
        this.gfgllbs = gfgllbs;
    }

    public String[] getHtxjs() {
        return htxjs;
    }

    public void setHtxjs(String[] htxjs) {
        this.htxjs = htxjs;
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

    public String getSccj() {
        return sccj;
    }

    public void setSccj(String sccj) {
        this.sccj = sccj;
    }

    public String getSjxmh() {
        return sjxmh;
    }

    public void setSjxmh(String sjxmh) {
        this.sjxmh = sjxmh;
    }

    public String getJszb() {
        return jszb;
    }

    public void setJszb(String jszb) {
        this.jszb = jszb;
    }

    public String getZlyq() {
        return zlyq;
    }

    public void setZlyq(String zlyq) {
        this.zlyq = zlyq;
    }

    public String getYsbz() {
        return ysbz;
    }

    public void setYsbz(String ysbz) {
        this.ysbz = ysbz;
    }

    public String getBctj() {
        return bctj;
    }

    public void setBctj(String bctj) {
        this.bctj = bctj;
    }

    public String getGfbh() {
        return gfbh;
    }

    public void setGfbh(String gfbh) {
        this.gfbh = gfbh;
    }

    public String getGysmc() {
        return gysmc;
    }

    public void setGysmc(String gysmc) {
        this.gysmc = gysmc;
    }

    public String getGfgllbmc() {
        return gfgllbmc;
    }

    public void setGfgllbmc(String gfgllbmc) {
        this.gfgllbmc = gfgllbmc;
    }

    public String getCjsjstart() {
        return cjsjstart;
    }

    public void setCjsjstart(String cjsjstart) {
        this.cjsjstart = cjsjstart;
    }

    public String getCjsjend() {
        return cjsjend;
    }

    public void setCjsjend(String cjsjend) {
        this.cjsjend = cjsjend;
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

    public String getDqsjstart() {
        return dqsjstart;
    }

    public void setDqsjstart(String dqsjstart) {
        this.dqsjstart = dqsjstart;
    }

    public String getDqsjend() {
        return dqsjend;
    }

    public void setDqsjend(String dqsjend) {
        this.dqsjend = dqsjend;
    }
}
