package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhYbxxDto")
public class ZdhYbxxDto extends ZdhYbxxModel{
    private static final long serialVersionUID = 1L;
    private String lckssj;
    private String lcjssj;
    private String zlckssj;
    private String zlcjssj;
    private String sjmc;
    private String ph;
    private String yl;
    private String lc;
    private String zlc;
    private String ybmxid;
    private String ybsjid;
    private String sj;
	//实验状态，用于筛选，这里1代表实验中，2代表实验结束
    private String syzt;
	private String cxnr;
    private String kssjstart;
    private String kssjend;
    private String jssjstart;
    private String jssjend;
    private List<String> jcdws;
    private List<String> jclxs;
    private List<String> zts;

    private List<String> lcs;

    private List<String> zlcs;

    private List<String> lcmcs;

    private List<String> lcxhs;

    private List<String>zlcmcs;

    private List<String>zlcxhs;
    //检测类型名称
    private String jclxmc;
    //开始时间 yyyy-mm-dd hh24:mi
    private String format_kssj;
    //结束时间 yyyy-mm-dd hh24:mi
    private String format_jssj;
    //提取流程开始时间 yyyy-mm-dd hh24:mi
    private String format_tqlckssj;
    //提取流程结束时间 yyyy-mm-dd hh24:mi
    private String format_tqlcjssj;
    //建库流程开始时间 yyyy-mm-dd hh24:mi
    private String format_jklckssj;
    //建库流程结束时间 yyyy-mm-dd hh24:mi
    private String format_jklcjssj;
    //检测单位名称
    private String jcdwmc;



    public List<String> getJcdws() {
        return jcdws;
    }

    public void setJcdws(List<String> jcdws) {
        this.jcdws = jcdws;
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

    public String getJssjstart() {
        return jssjstart;
    }

    public void setJssjstart(String jssjstart) {
        this.jssjstart = jssjstart;
    }

    public String getJssjend() {
        return jssjend;
    }

    public void setJssjend(String jssjend) {
        this.jssjend = jssjend;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    @Override
    public String getYbmxid() {
        return ybmxid;
    }

    @Override
    public void setYbmxid(String ybmxid) {
        this.ybmxid = ybmxid;
    }

    public String getYbsjid() {
        return ybsjid;
    }

    public void setYbsjid(String ybsjid) {
        this.ybsjid = ybsjid;
    }

	public String getLc() {
        return lc;
    }

	public void setLc(String lc) {
        this.lc = lc;
    }

    public String getZlc() {
        return zlc;
    }

    public void setZlc(String zlc) {
        this.zlc = zlc;
    }

    public String getLckssj() {
        return lckssj;
    }

    public void setLckssj(String lckssj) {
        this.lckssj = lckssj;
    }

    public String getLcjssj() {
        return lcjssj;
    }

    public void setLcjssj(String lcjssj) {
        this.lcjssj = lcjssj;
    }

    public String getZlckssj() {
        return zlckssj;
    }

    public void setZlckssj(String zlckssj) {
        this.zlckssj = zlckssj;
    }

    public String getZlcjssj() {
        return zlcjssj;
    }

    public void setZlcjssj(String zlcjssj) {
        this.zlcjssj = zlcjssj;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getYl() {
        return yl;
    }

    public void setYl(String yl) {
        this.yl = yl;
    }
	public String getCxnr() {
        return cxnr;
    }

    public void setCxnr(String cxnr) {
        this.cxnr = cxnr;
    }

    public String getSyzt() {
        return syzt;
    }

    public void setSyzt(String syzt) {
        this.syzt = syzt;
    }

    public String getJclxmc() {
        return jclxmc;
    }

    public void setJclxmc(String jclxmc) {
        this.jclxmc = jclxmc;
    }

    public String getFormat_kssj() {
        return format_kssj;
    }

    public void setFormat_kssj(String format_kssj) {
        this.format_kssj = format_kssj;
    }

    public String getFormat_jssj() {
        return format_jssj;
    }

    public void setFormat_jssj(String format_jssj) {
        this.format_jssj = format_jssj;
    }

    public String getFormat_tqlckssj() {
        return format_tqlckssj;
    }

    public void setFormat_tqlckssj(String format_tqlckssj) {
        this.format_tqlckssj = format_tqlckssj;
    }

    public String getFormat_tqlcjssj() {
        return format_tqlcjssj;
    }

    public void setFormat_tqlcjssj(String format_tqlcjssj) {
        this.format_tqlcjssj = format_tqlcjssj;
    }

    public String getFormat_jklckssj() {
        return format_jklckssj;
    }

    public void setFormat_jklckssj(String format_jklckssj) {
        this.format_jklckssj = format_jklckssj;
    }

    public String getFormat_jklcjssj() {
        return format_jklcjssj;
    }

    public void setFormat_jklcjssj(String format_jklcjssj) {
        this.format_jklcjssj = format_jklcjssj;
    }

    public String getJcdwmc() {
        return jcdwmc;
    }

    public void setJcdwmc(String jcdwmc) {
        this.jcdwmc = jcdwmc;
    }

    public List<String> getJclxs() {
        return jclxs;
    }

    public void setJclxs(List<String> jclxs) {
        this.jclxs = jclxs;
    }

    public List<String> getZts() {
        return zts;
    }

    public void setZts(List<String> zts) {
        this.zts = zts;
    }

    public List<String> getZlcxhs() {
        return zlcxhs;
    }

    public void setZlcxhs(List<String> zlcxhs) {
        this.zlcxhs = zlcxhs;
    }

    public List<String> getZlcmcs() {
        return zlcmcs;
    }

    public void setZlcmcs(List<String> zlcmcs) {
        this.zlcmcs = zlcmcs;
    }

    public List<String> getLcxhs() {
        return lcxhs;
    }

    public void setLcxhs(List<String> lcxhs) {
        this.lcxhs = lcxhs;
    }

    public List<String> getLcmcs() {
        return lcmcs;
    }

    public void setLcmcs(List<String> lcmcs) {
        this.lcmcs = lcmcs;
    }

    public List<String> getZlcs() {
        return zlcs;
    }

    public void setZlcs(List<String> zlcs) {
        this.zlcs = zlcs;
    }

    public List<String> getLcs() {
        return lcs;
    }

    public void setLcs(List<String> lcs) {
        this.lcs = lcs;
    }
}
