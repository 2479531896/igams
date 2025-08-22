package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CnvjgDto")
public class CnvjgDto extends CnvjgModel {
    //肿瘤判断名称
    private String shjgmc;
    //模型预测名称
    private String aijgmc;
    //肿瘤结果名称
    private String zszlmc;
    //判断原因名称
    private String pdyymc;
    //患者姓名
    private String hzxm;
    //前期诊断
    private String qqzd;
    //样本类型名称
    private String yblxmc;
    //送检单位
    private String sjdw;
    //科室
    private String ks;
    //cnv详情
    private String cnvxq;
    //cnv结果
    private String cnvjg;
    //录入开始时间
    private String lrsjstart;
    //录入结束时间
    private String lrsjend;
    //科室名称
    private String ksmc;
    //芯片ID
    private String xpid;
    //年龄
    private String nl;
    //疑似遗传病
    private String ysycb;
    //样本编号
    private String ybbh;
    //该文库是否无视文库类型出D+R病原体,0否，1是
    private String qxhbbg;
    //文库编号（多）
    private List<String> wkbhs;
    //芯片名
    private String xpm;
    private List<CnvjgxqDto> cnvjgxqDtos;
    private String cnv_json;
    private List<String> jcdws;
    private List<String> sjhbs;//伙伴限制
    private String cxkssjstart;//测序开始时间起
    private String cxkssjend;//测序开始时间止
    private String sysmc;//实验室名称
    private String sysid;//实验室ID
    private String jclb;//基础类别
    private String zt;
    private String wkzs;//文库总数
    private String yshs;//已审核数
    private String yfss;//已发送数
    private String oncozs;
    private String oncoqx;
    private String filter;
    private String rst;
    private String lxmc;
    private String sjid;
    private String nbbm;
    private String othersize;
    private String sfonco;

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public String getWkzs() {
        return wkzs;
    }

    public void setWkzs(String wkzs) {
        this.wkzs = wkzs;
    }

    public String getYshs() {
        return yshs;
    }

    public void setYshs(String yshs) {
        this.yshs = yshs;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getJclb() {
        return jclb;
    }

    public void setJclb(String jclb) {
        this.jclb = jclb;
    }

    public String getSysmc() {
        return sysmc;
    }

    public void setSysmc(String sysmc) {
        this.sysmc = sysmc;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getCxkssjstart() {
        return cxkssjstart;
    }

    public void setCxkssjstart(String cxkssjstart) {
        this.cxkssjstart = cxkssjstart;
    }

    public String getCxkssjend() {
        return cxkssjend;
    }

    public void setCxkssjend(String cxkssjend) {
        this.cxkssjend = cxkssjend;
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

    public String getCnv_json() {
        return cnv_json;
    }

    public void setCnv_json(String cnv_json) {
        this.cnv_json = cnv_json;
    }

    public List<CnvjgxqDto> getCnvjgxqDtos() {
        return cnvjgxqDtos;
    }

    public void setCnvjgxqDtos(List<CnvjgxqDto> cnvjgxqDtos) {
        this.cnvjgxqDtos = cnvjgxqDtos;
    }

    public String getXpm() {
        return xpm;
    }

    public void setXpm(String xpm) {
        this.xpm = xpm;
    }

    public List<String> getWkbhs() {
        return wkbhs;
    }

    public void setWkbhs(List<String> wkbhs) {
        this.wkbhs = wkbhs;
    }

    public String getQxhbbg() {
        return qxhbbg;
    }

    public void setQxhbbg(String qxhbbg) {
        this.qxhbbg = qxhbbg;
    }

    public String getYbbh() {
        return ybbh;
    }

    public void setYbbh(String ybbh) {
        this.ybbh = ybbh;
    }

    public String getYsycb() {
        return ysycb;
    }

    public void setYsycb(String ysycb) {
        this.ysycb = ysycb;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getXpid() {
        return xpid;
    }

    public void setXpid(String xpid) {
        this.xpid = xpid;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public String getShjgmc() {
        return shjgmc;
    }

    public void setShjgmc(String shjgmc) {
        this.shjgmc = shjgmc;
    }

    public String getAijgmc() {
        return aijgmc;
    }

    public void setAijgmc(String aijgmc) {
        this.aijgmc = aijgmc;
    }

    public String getZszlmc() {
        return zszlmc;
    }

    public void setZszlmc(String zszlmc) {
        this.zszlmc = zszlmc;
    }

    public String getPdyymc() {
        return pdyymc;
    }

    public void setPdyymc(String pdyymc) {
        this.pdyymc = pdyymc;
    }

    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getQqzd() {
        return qqzd;
    }

    public void setQqzd(String qqzd) {
        this.qqzd = qqzd;
    }

    public String getYblxmc() {
        return yblxmc;
    }

    public void setYblxmc(String yblxmc) {
        this.yblxmc = yblxmc;
    }

    public String getSjdw() {
        return sjdw;
    }

    public void setSjdw(String sjdw) {
        this.sjdw = sjdw;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getCnvxq() {
        return cnvxq;
    }

    public void setCnvxq(String cnvxq) {
        this.cnvxq = cnvxq;
    }

    public String getCnvjg() {
        return cnvjg;
    }

    public void setCnvjg(String cnvjg) {
        this.cnvjg = cnvjg;
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

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getOncoqx() {
        return oncoqx;
    }

    public void setOncoqx(String oncoqx) {
        this.oncoqx = oncoqx;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getYfss() {
        return yfss;
    }

    public void setYfss(String yfss) {
        this.yfss = yfss;
    }

    public String getOncozs() {
        return oncozs;
    }

    public void setOncozs(String oncozs) {
        this.oncozs = oncozs;
    }

    public String getLxmc() {
        return lxmc;
    }

    public void setLxmc(String lxmc) {
        this.lxmc = lxmc;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getNbbm() {
        return nbbm;
    }

    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }


    public String getOthersize() {
        return othersize;
    }

    public void setOthersize(String othersize) {
        this.othersize = othersize;
    }

    public String getSfonco() {
        return sfonco;
    }

    public void setSfonco(String sfonco) {
        this.sfonco = sfonco;
    }
}
