package com.matridx.igams.wechat.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;


/**
 * 送检扩展信息(IgamsSjkzxx)实体类
 *
 * @author makejava
 * @since 2023-04-12 10:17:47
 */
@Alias(value="SjkzxxDto")
public class SjkzxxDto extends SjkzxxModel {

    private String hzxm;
    private String sjdwmc;
    private String yblxmc;
    private String nbbm;
    private String ybbh;
    private String jcdwmc;
    private String db;
    private List<String> jcdwxz;
    private String[] jcdws;
    private String[] jcxms;

    private String SqlParam;
    private String bbsdwdstart;
    private String bbsdwdend;
    //采样日期(开始)
    private String cyrqstart;
    //采样日期(结束)
    private String cyrqend;
    //开始日期
    private String jsrqstart;
    //结束日期
    private String jsrqend;
    //实验日期开始
    private String syrqstart;
    //实验日期结束
    private String syrqend;
    //开始日期
    private String bgrqstart;
    //结束日期
    private String bgrqend;
    private String cyrq;
    private String jsrq;
    private String syrq;
    private String dsyrq;
    private String qtsyrq;
    private String bgrq;

    private String bz;

    //判断是否是个人清单 single_flag=1 为个人清单  single_flag=0 or single_flag=null 为全部清单
    private String single_flag;
    //用于个人清单判断lrry [yhid,ddid,wxid]
    private List<String> userids;
    //合作伙伴集合
    private List<String> sjhbs;
    public String getSingle_flag() {
        return single_flag;
    }

    public void setSingle_flag(String single_flag) {
        this.single_flag = single_flag;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }
    public List<String> getSjhbs()
    {
        return sjhbs;
    }
    public void setSjhbs(List<String> sjhbs)
    {
        this.sjhbs = sjhbs;
    }
    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCyrq() {
        return cyrq;
    }

    public void setCyrq(String cyrq) {
        this.cyrq = cyrq;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getSyrq() {
        return syrq;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public String getDsyrq() {
        return dsyrq;
    }

    public void setDsyrq(String dsyrq) {
        this.dsyrq = dsyrq;
    }

    public String getQtsyrq() {
        return qtsyrq;
    }

    public void setQtsyrq(String qtsyrq) {
        this.qtsyrq = qtsyrq;
    }

    public String getBgrq() {
        return bgrq;
    }

    public void setBgrq(String bgrq) {
        this.bgrq = bgrq;
    }

    public String getCyrqstart() {
        return cyrqstart;
    }

    public void setCyrqstart(String cyrqstart) {
        this.cyrqstart = cyrqstart;
    }

    public String getCyrqend() {
        return cyrqend;
    }

    public void setCyrqend(String cyrqend) {
        this.cyrqend = cyrqend;
    }

    public String getJsrqstart() {
        return jsrqstart;
    }

    public void setJsrqstart(String jsrqstart) {
        this.jsrqstart = jsrqstart;
    }

    public String getJsrqend() {
        return jsrqend;
    }

    public void setJsrqend(String jsrqend) {
        this.jsrqend = jsrqend;
    }

    public String getSyrqstart() {
        return syrqstart;
    }

    public void setSyrqstart(String syrqstart) {
        this.syrqstart = syrqstart;
    }

    public String getSyrqend() {
        return syrqend;
    }

    public void setSyrqend(String syrqend) {
        this.syrqend = syrqend;
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

    public String getBbsdwdstart() {
        return bbsdwdstart;
    }

    public void setBbsdwdstart(String bbsdwdstart) {
        this.bbsdwdstart = bbsdwdstart;
    }

    public String getBbsdwdend() {
        return bbsdwdend;
    }

    public void setBbsdwdend(String bbsdwdend) {
        this.bbsdwdend = bbsdwdend;
    }

    public String getSqlParam() {
        return SqlParam;
    }

    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }

    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getSjdwmc() {
        return sjdwmc;
    }

    public void setSjdwmc(String sjdwmc) {
        this.sjdwmc = sjdwmc;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getJcdwmc() {
        return jcdwmc;
    }

    public void setJcdwmc(String jcdwmc) {
        this.jcdwmc = jcdwmc;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public List<String> getJcdwxz() {
        return jcdwxz;
    }

    public void setJcdwxz(List<String> jcdwxz) {
        this.jcdwxz = jcdwxz;
    }

    public String[] getJcdws()
    {
        return jcdws;
    }

    public void setJcdws(String[] jcdws)
    {
        this.jcdws = jcdws;
        for (int i = 0; i <jcdws.length; i++){
            this.jcdws[i]=this.jcdws[i].replace("'","");
        }
    }

    public String[] getJcxms() {
        return jcxms;
    }

    public void setJcxms(String[] jcxms) {
        this.jcxms = jcxms;
        for (int i = 0; i < jcxms.length; i++){
            this.jcxms[i]=this.jcxms[i].replace("'","");
        }
    }




    private static final long serialVersionUID = 1L;

}

