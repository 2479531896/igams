package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Set;

@Alias(value="BioXpxxDto")
public class BioXpxxDto extends BioXpxxModel {

    //已审核数
    private String yshs;
    //对应的文库有送检ID的/文库总数
    private String clinical_Total;
    //文库已发报告/已审核
    private String reportSent_Reviewed;
    //实验室名称
    private String sys;
    //测序仪名称
    private String sequencer;
    //文库编号
    private String wkbh;
    //患者姓名
    private String hzxm;
    //cnvjg已经审核/总数
    private String cny_Reviewed;
    //范围搜索的开始时间
    private String kssj;
    //范围搜索的结束时间
    private String jssj;
	List<WkcxDto> wkck;
    //传送前端的时间列表
    List<TimeDto> sjlist;
    //实验室ID
    private String sysid;
    private String fcsid;
    //芯片类型，送检区分
    private String sjqf;
    //送检IDS
    private Set<String> sjids;
    //是否权限筛选
    private String  sjqxsx;
    //送检ID
    private String sjid;
    //用于个人清单判断lrry [yhid,ddid,wxid]
    private List<String> userids;
    private List<String> xpms;//芯片名s
    private List<String> sjhbs;//伙伴限制
    private List<String> jcdws;
    private String pdbj;//判断标记，判断tmngs还是mngs
    private String filter;

    public String getPdbj() {
        return pdbj;
    }

    public void setPdbj(String pdbj) {
        this.pdbj = pdbj;
    }

    public List<String> getJcdws() {
        return jcdws;
    }

    public void setJcdws(List<String> jcdws) {
        this.jcdws = jcdws;
    }

    public List<String> getSjhbs() {
        return sjhbs;
    }

    public void setSjhbs(List<String> sjhbs) {
        this.sjhbs = sjhbs;
    }

    public List<String> getXpms() {
        return xpms;
    }

    public void setXpms(List<String> xpms) {
        this.xpms = xpms;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getSjqxsx() {
        return sjqxsx;
    }

    public void setSjqxsx(String sjqxsx) {
        this.sjqxsx = sjqxsx;
    }

    public Set<String> getSjids() {
        return sjids;
    }

    public void setSjids(Set<String> sjids) {
        this.sjids = sjids;
    }

    public String getSjqf() {
        return sjqf;
    }

    public void setSjqf(String sjqf) {
        this.sjqf = sjqf;
    }

    public String getFcsid() {
        return fcsid;
    }

    public void setFcsid(String fcsid) {
        this.fcsid = fcsid;
    }

    public void setWkbh(String wkbh) {
        this.wkbh = wkbh;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public List<TimeDto> getSjlist() {
        return sjlist;
    }

    public void setSjlist(List<TimeDto> sjlist) {
        this.sjlist = sjlist;
    }

    public List<WkcxDto> getWkck() {
        return wkck;
    }

    public void setWkck(List<WkcxDto> wkck) {
        this.wkck = wkck;
    }
    public String getClinical_Total() {
        return clinical_Total;
    }

    public void setClinical_Total(String clinical_Total) {
        this.clinical_Total = clinical_Total;
    }

    public String getReportSent_Reviewed() {
        return reportSent_Reviewed;
    }

    public void setReportSent_Reviewed(String reportSent_Reviewed) {
        this.reportSent_Reviewed = reportSent_Reviewed;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getSequencer() {
        return sequencer;
    }

    public void setSequencer(String sequencer) {
        this.sequencer = sequencer;
    }

    public String getWkbh() {
        return wkbh;
    }



    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getCny_Reviewed() {
        return cny_Reviewed;
    }

    public void setCny_Reviewed(String cny_Reviewed) {
        this.cny_Reviewed = cny_Reviewed;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getYshs() {
        return yshs;
    }

    public void setYshs(String yshs) {
        this.yshs = yshs;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
