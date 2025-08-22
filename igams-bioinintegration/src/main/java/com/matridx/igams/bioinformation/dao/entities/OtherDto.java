package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value = "OtherDto")
public class OtherDto extends OtherModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5988619449059129508L;
	private String sjid;
    private String hzxm;
    private String nl;
    private String dh;
    private String sjys;
    private String ysdh;
    private String ybbh;
    private String cyrq;
    private String sjdwmc;
    private String ksmc;
    private String yblx;
    private String lczz;
    private String yblxmc;
    private String ybtj;
    private String jqyy;
    private String qqjcmc;
    private String qqzd;
    private String jcxmmc;
    private String ybztmc;
    private String lczzmc;
    private String nbbm;
    private String yblxdm;
    private String yhid;
    private String yhm;
    private String zsxm;
    private String grouping;
    private String mrfz;
    private String xb;
    private String csmc;
    private List<OtherDto> pcr;
    private String bz;
    private String bgrq;
    private String jsrq;
    private String total;
    private String dwxdbj;
    private String jcdw;
    //用于个人清单判断lrry [yhid,ddid,wxid]
    private List<String> userids;
    private String jsid;
    //合作伙伴集合
    private List<String> sjhbs;
    private String hbid;
    private String hbmc;
    //角色检测单位限制
    private List<String> jcdwxz;
    private String dqjs;
    private String zyid;
    private String czdm;
    private String db;
    private String jlmc;
    private String ct1;
    private String ct2;
    private String mngsfxwcsj;
    private String oncofxwcsj;
    private String wkbm;
    private String nldw;

    private String mngsfxwcsj_start;

    private String mngsfxwcsj_end;

    private String wknd;

    private String dlnd;

    public String getDlnd() {
        return dlnd;
    }

    public void setDlnd(String dlnd) {
        this.dlnd = dlnd;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getZyid() {
        return zyid;
    }

    public void setZyid(String zyid) {
        this.zyid = zyid;
    }

    public String getCzdm() {
        return czdm;
    }

    public void setCzdm(String czdm) {
        this.czdm = czdm;
    }

    public String getDqjs() {
        return dqjs;
    }

    public void setDqjs(String dqjs) {
        this.dqjs = dqjs;
    }

    public List<String> getJcdwxz() {
        return jcdwxz;
    }

    public void setJcdwxz(List<String> jcdwxz) {
        this.jcdwxz = jcdwxz;
    }

    public String getHbmc() {
        return hbmc;
    }

    public void setHbmc(String hbmc) {
        this.hbmc = hbmc;
    }

    public String getHbid() {
        return hbid;
    }

    public void setHbid(String hbid) {
        this.hbid = hbid;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    public String getJsid() {
        return jsid;
    }

    public void setJsid(String jsid) {
        this.jsid = jsid;
    }

    public List<String> getSjhbs() {
        return sjhbs;
    }

    public void setSjhbs(List<String> sjhbs) {
        this.sjhbs = sjhbs;
    }

    public String getDwxdbj() {
        return dwxdbj;
    }

    public void setDwxdbj(String dwxdbj) {
        this.dwxdbj = dwxdbj;
    }

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBgrq() {
        return bgrq;
    }

    public void setBgrq(String bgrq) {
        this.bgrq = bgrq;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }



    public String getCsmc() {
        return csmc;
    }

    public void setCsmc(String csmc) {
        this.csmc = csmc;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
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

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public String getMrfz() {
        return mrfz;
    }

    public void setMrfz(String mrfz) {
        this.mrfz = mrfz;
    }

    public String getYblxdm() {
        return yblxdm;
    }

    public void setYblxdm(String yblxdm) {
        this.yblxdm = yblxdm;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getSjys() {
        return sjys;
    }

    public void setSjys(String sjys) {
        this.sjys = sjys;
    }

    public String getYsdh() {
        return ysdh;
    }

    public void setYsdh(String ysdh) {
        this.ysdh = ysdh;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getCyrq() {
        return cyrq;
    }

    public void setCyrq(String cyrq) {
        this.cyrq = cyrq;
    }

    public String getSjdwmc() {
        return sjdwmc;
    }

    public void setSjdwmc(String sjdwmc) {
        this.sjdwmc = sjdwmc;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public String getLczz() {
        return lczz;
    }

    public void setLczz(String lczz) {
        this.lczz = lczz;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public String getYbtj() {
        return ybtj;
    }

    public void setYbtj(String ybtj) {
        this.ybtj = ybtj;
    }

    public String getJqyy() {
        return jqyy;
    }

    public void setJqyy(String jqyy) {
        this.jqyy = jqyy;
    }

    public String getQqjcmc() {
        return qqjcmc;
    }

    public void setQqjcmc(String qqjcmc) {
        this.qqjcmc = qqjcmc;
    }

    public String getQqzd() {
        return qqzd;
    }

    public void setQqzd(String qqzd) {
        this.qqzd = qqzd;
    }

    public String getJcxmmc() {
        return jcxmmc;
    }

    public void setJcxmmc(String jcxmmc) {
        this.jcxmmc = jcxmmc;
    }

    public String getYbztmc() {
        return ybztmc;
    }

    public void setYbztmc(String ybztmc) {
        this.ybztmc = ybztmc;
    }

    public String getLczzmc() {
        return lczzmc;
    }

    public void setLczzmc(String lczzmc) {
        this.lczzmc = lczzmc;
    }

    public String getJlmc() {
        return jlmc;
    }

    public void setJlmc(String jlmc) {
        this.jlmc = jlmc;
    }

    public String getCt1() {
        return ct1;
    }

    public void setCt1(String ct1) {
        this.ct1 = ct1;
    }

    public String getCt2() {
        return ct2;
    }

    public void setCt2(String ct2) {
        this.ct2 = ct2;
    }

    public List<OtherDto> getPcr() {
        return pcr;
    }

    public void setPcr(List<OtherDto> pcr) {
        this.pcr = pcr;
    }

    public String getMngsfxwcsj() {
        return mngsfxwcsj;
    }

    public void setMngsfxwcsj(String mngsfxwcsj) {
        this.mngsfxwcsj = mngsfxwcsj;
    }

    public String getOncofxwcsj() {
        return oncofxwcsj;
    }

    public void setOncofxwcsj(String oncofxwcsj) {
        this.oncofxwcsj = oncofxwcsj;
    }

    public String getWkbm() {
        return wkbm;
    }

    public void setWkbm(String wkbm) {
        this.wkbm = wkbm;
    }

    public String getNldw() {
        return nldw;
    }

    public void setNldw(String nldw) {
        this.nldw = nldw;
    }

    public String getMngsfxwcsj_start() {
        return mngsfxwcsj_start;
    }

    public void setMngsfxwcsj_start(String mngsfxwcsj_start) {
        this.mngsfxwcsj_start = mngsfxwcsj_start;
    }

    public String getMngsfxwcsj_end() {
        return mngsfxwcsj_end;
    }

    public void setMngsfxwcsj_end(String mngsfxwcsj_end) {
        this.mngsfxwcsj_end = mngsfxwcsj_end;
    }

    public String getWknd() {
        return wknd;
    }

    public void setWknd(String wknd) {
        this.wknd = wknd;
    }
}
