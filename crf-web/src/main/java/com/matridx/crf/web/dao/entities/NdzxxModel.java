package com.matridx.crf.web.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="NdzxxModel")
public class NdzxxModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    //患者id
    private String hzid;
    //患者姓名
    private String hzxm;
    //住院号
    private String zyh;
    //纳入研究编号
    private String nryjbh;
    //纳入时间
    private String nrsj;
    //年龄
    private String nl;
    //性别
    private String xb;
    //就诊科室
    private String jzks;
    //病人类别
    private String brlb;
    //既往合并症
    private String jwhbz;
    //感染部位
    private String grbw;
    //发病前多少天存在抗菌药物暴露史（关联系统参数表）
    private String kjywbls;
    //其他感染部位
    private String qtgrbw;
    //入院时间
    private String rysj;
    //所属医院
    private String ssyy;
    //记录者名称
    private String jlzmc;
    //记录日期
    private String jlrq;
    //记录开始日期
    private String jlrqstart;
    //记录结束日期
    private String jlrqend;
    //入院时间开始日期
    private String rysjstart;
    //入院时间结束日期
    private String rysjend;
    //纳入时间开始日期
    private String nrsjstart;
    //纳入时间结束日期
    private String nrsjend;
    //诊断脓毒症第几日
    private String jldjt;
    //HRmax
    private String hrmax;
    //MAPmax
    private String mapmax;
    //SAPmax
    private String sapmax;
    //RRmax
    private String rrmax;
    //Tmax
    private String tmax;
    //机械通气
    private String jxtq;
    //血管活性药
    private String xghxy;
    //CRRT
    private String crrt;
    //GCS
    private String gcs;
    //乳酸
    private String rs;
    //钾离子
    private String k;
    //PaCO2
    private String paco2;
    //钠离子
    private String na;
    //钙离子
    private String ca;
    //PH
    private String ph;
    //PaO2
    private String pao2;
    //Abe
    private String abe;
    //HCO3-
    private String hco3;
    //FiO2
    private String fio2;
    //CRP
    private String crp;
    //PCT
    private String pct;
    //IL-6
    private String il6;
    //IL-10
    private String il10;
    //IL-1B
    private String il1b;
    //尿素氮
    private String nsd;
    //白蛋白
    private String bdb;
    //肌酐
    private String jg;
    //总胆红素
    private String zdhs;
    //WBC
    private String wbc;
    //PLT
    private String plt;
    //L count
    private String lcount;
    //送检前是否使用抗菌药物
    private String sjqsfsykjyw;
    //抗菌药种类
    private String kjywzl;
    //McfDNA
    private String mcfdna;
    //McfDNA结果
    private String mcfdnajg;
    //血培养
    private String xpy;
    //血培养结果
    private String xpyjg;
    //痰培养
    private String tpy;
    //痰培养结果
    private String tpyjg;
    //痰涂片
    private String ttp;
    //痰涂片结果
    private String ttpjg;
    //腹水涂片
    private String fstp;
    //腹水涂片结果
    private String fstpjg;
    //腹水培养
    private String fspy;
    //腹水培养结果
    private String fspyjg;
    //第一个其他
    private String qtf;
    //第一个其他结果
    private String qtfjg;
    //第二个其他
    private String qtt;
    //第二个其他结果
    private String qttjg;
    //是否抗菌药物降阶梯治疗
    private String kjywjjtzl;
    //抗菌药物治疗总疗程
    private String kjzlc;
    //血管活性药物停用时间
    private String xghxytysj;
    //CRRT停用时间
    private String crrttysj;
    //呼吸机停用时间
    private String hxjtysj;
    //出ICU时间
    private String cicusj;
    //出ICU状态
    private String cicuzt;
    //出院时间
    private String cysj;
    //出院状态
    private String cyzt;
    //脓毒症记录id
    private String ndzjlid;
    //新建记录时间
    private String xjsj;
    //操作人，新增
    private String tjr;
    //修改人，操作人，
    private String xgr;
    //亚组
    private String yz;
    //IL-17
    private String il17;
    //TNF-ɑ
    private String tnfa;
    //INF-ɣ
    private String infy;
    //C3
    private String c3;
    //C4
    private String c4;
    //CD4Th
    private String cd4th;
    //中性粒细胞绝对值/淋巴细胞绝对值(10^9L)
    private String zxxbjdz;
    //淋巴细胞绝对值(10^9L)
    private String lbxbjdz;
    //apache2
    private String apache2;
    //sofapf
    private String sofapf;
    //nryjbhpx
    private String nryjbhpx;

    public String getNryjbhpx() {
        return nryjbhpx;
    }

    public void setNryjbhpx(String nryjbhpx) {
        this.nryjbhpx = nryjbhpx;
    }

    public String getTnfa() {
        return tnfa;
    }

    public void setTnfa(String tnfa) {
        this.tnfa = tnfa;
    }

    public String getInfy() {
        return infy;
    }

    public void setInfy(String infy) {
        this.infy = infy;
    }

    public String getApache2() {
        return apache2;
    }

    public void setApache2(String apache2) {
        this.apache2 = apache2;
    }

    public String getSofapf() {
        return sofapf;
    }

    public void setSofapf(String sofapf) {
        this.sofapf = sofapf;
    }

    public String getZxxbjdz() {
        return zxxbjdz;
    }

    public void setZxxbjdz(String zxxbjdz) {
        this.zxxbjdz = zxxbjdz;
    }

    public String getLbxbjdz() {
        return lbxbjdz;
    }

    public void setLbxbjdz(String lbxbjdz) {
        this.lbxbjdz = lbxbjdz;
    }

    public String getIl17() {
        return il17;
    }

    public void setIl17(String il17) {
        this.il17 = il17;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getCd4th() {
        return cd4th;
    }

    public void setCd4th(String cd4th) {
        this.cd4th = cd4th;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public String getXgr() {
        return xgr;
    }

    public void setXgr(String xgr) {
        this.xgr = xgr;
    }

    public String getXjsj() {
        return xjsj;
    }

    public void setXjsj(String xjsj) {
        this.xjsj = xjsj;
    }

    public String getTjr() {
        return tjr;
    }

    public void setTjr(String tjr) {
        this.tjr = tjr;
    }

    public String getNdzjlid() {
		return ndzjlid;
	}

	public void setNdzjlid(String ndzjlid) {
		this.ndzjlid = ndzjlid;
	}

    public String getKjywjjtzl() {
        return kjywjjtzl;
    }

    public void setKjywjjtzl(String kjywjjtzl) {
        this.kjywjjtzl = kjywjjtzl;
    }

    public String getKjzlc() {
        return kjzlc;
    }

    public void setKjzlc(String kjzlc) {
        this.kjzlc = kjzlc;
    }

    public String getXghxytysj() {
        return xghxytysj;
    }

    public void setXghxytysj(String xghxytysj) {
        this.xghxytysj = xghxytysj;
    }

    public String getCrrttysj() {
        return crrttysj;
    }

    public void setCrrttysj(String crrttysj) {
        this.crrttysj = crrttysj;
    }

    public String getHxjtysj() {
        return hxjtysj;
    }

    public void setHxjtysj(String hxjtysj) {
        this.hxjtysj = hxjtysj;
    }

    public String getCicusj() {
        return cicusj;
    }

    public void setCicusj(String cicusj) {
        this.cicusj = cicusj;
    }

    public String getCicuzt() {
        return cicuzt;
    }

    public void setCicuzt(String cicuzt) {
        this.cicuzt = cicuzt;
    }

    public String getCysj() {
        return cysj;
    }

    public void setCysj(String cysj) {
        this.cysj = cysj;
    }

    public String getCyzt() {
        return cyzt;
    }

    public void setCyzt(String cyzt) {
        this.cyzt = cyzt;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getPaco2() {
        return paco2;
    }

    public void setPaco2(String paco2) {
        this.paco2 = paco2;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getPao2() {
        return pao2;
    }

    public void setPao2(String pao2) {
        this.pao2 = pao2;
    }

    public String getAbe() {
        return abe;
    }

    public void setAbe(String abe) {
        this.abe = abe;
    }

    public String getHco3() {
        return hco3;
    }

    public void setHco3(String hco3) {
        this.hco3 = hco3;
    }

    public String getFio2() {
        return fio2;
    }

    public void setFio2(String fio2) {
        this.fio2 = fio2;
    }

    public String getCrp() {
        return crp;
    }

    public void setCrp(String crp) {
        this.crp = crp;
    }

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }

    public String getIl6() {
        return il6;
    }

    public void setIl6(String il6) {
        this.il6 = il6;
    }

    public String getIl10() {
        return il10;
    }

    public void setIl10(String il10) {
        this.il10 = il10;
    }

    public String getIl1b() {
        return il1b;
    }

    public void setIl1b(String il1b) {
        this.il1b = il1b;
    }

    public String getNsd() {
        return nsd;
    }

    public void setNsd(String nsd) {
        this.nsd = nsd;
    }

    public String getBdb() {
        return bdb;
    }

    public void setBdb(String bdb) {
        this.bdb = bdb;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getZdhs() {
        return zdhs;
    }

    public void setZdhs(String zdhs) {
        this.zdhs = zdhs;
    }

    public String getWbc() {
        return wbc;
    }

    public void setWbc(String wbc) {
        this.wbc = wbc;
    }

    public String getPlt() {
        return plt;
    }

    public void setPlt(String plt) {
        this.plt = plt;
    }

    public String getLcount() {
        return lcount;
    }

    public void setLcount(String lcount) {
        this.lcount = lcount;
    }

    public String getSjqsfsykjyw() {
        return sjqsfsykjyw;
    }

    public void setSjqsfsykjyw(String sjqsfsykjyw) {
        this.sjqsfsykjyw = sjqsfsykjyw;
    }

    public String getKjywzl() {
        return kjywzl;
    }

    public void setKjywzl(String kjywzl) {
        this.kjywzl = kjywzl;
    }

    public String getMcfdna() {
        return mcfdna;
    }

    public void setMcfdna(String mcfdna) {
        this.mcfdna = mcfdna;
    }

    public String getMcfdnajg() {
        return mcfdnajg;
    }

    public void setMcfdnajg(String mcfdnajg) {
        this.mcfdnajg = mcfdnajg;
    }

    public String getXpy() {
        return xpy;
    }

    public void setXpy(String xpy) {
        this.xpy = xpy;
    }

    public String getXpyjg() {
        return xpyjg;
    }

    public void setXpyjg(String xpyjg) {
        this.xpyjg = xpyjg;
    }

    public String getTpy() {
        return tpy;
    }

    public void setTpy(String tpy) {
        this.tpy = tpy;
    }

    public String getTpyjg() {
        return tpyjg;
    }

    public void setTpyjg(String tpyjg) {
        this.tpyjg = tpyjg;
    }

    public String getTtp() {
        return ttp;
    }

    public void setTtp(String ttp) {
        this.ttp = ttp;
    }

    public String getTtpjg() {
        return ttpjg;
    }

    public void setTtpjg(String ttpjg) {
        this.ttpjg = ttpjg;
    }

    public String getFstp() {
        return fstp;
    }

    public void setFstp(String fstp) {
        this.fstp = fstp;
    }

    public String getFstpjg() {
        return fstpjg;
    }

    public void setFstpjg(String fstpjg) {
        this.fstpjg = fstpjg;
    }

    public String getFspy() {
        return fspy;
    }

    public void setFspy(String fspy) {
        this.fspy = fspy;
    }

    public String getFspyjg() {
        return fspyjg;
    }

    public void setFspyjg(String fspyjg) {
        this.fspyjg = fspyjg;
    }

    public String getQtf() {
        return qtf;
    }

    public void setQtf(String qtf) {
        this.qtf = qtf;
    }

    public String getQtfjg() {
        return qtfjg;
    }

    public void setQtfjg(String qtfjg) {
        this.qtfjg = qtfjg;
    }

    public String getQtt() {
        return qtt;
    }

    public void setQtt(String qtt) {
        this.qtt = qtt;
    }

    public String getQttjg() {
        return qttjg;
    }

    public void setQttjg(String qttjg) {
        this.qttjg = qttjg;
    }

    public String getJlrq() {
        return jlrq;
    }

    public void setJlrq(String jlrq) {
        this.jlrq = jlrq;
    }

    public String getJlrqstart() {
        return jlrqstart;
    }

    public void setJlrqstart(String jlrqstart) {
        this.jlrqstart = jlrqstart;
    }

    public String getJlrqend() {
        return jlrqend;
    }

    public void setJlrqend(String jlrqend) {
        this.jlrqend = jlrqend;
    }

    public String getRysjstart() {
        return rysjstart;
    }

    public void setRysjstart(String rysjstart) {
        this.rysjstart = rysjstart;
    }

    public String getRysjend() {
        return rysjend;
    }

    public void setRysjend(String rysjend) {
        this.rysjend = rysjend;
    }

    public String getNrsjstart() {
        return nrsjstart;
    }

    public void setNrsjstart(String nrsjstart) {
        this.nrsjstart = nrsjstart;
    }

    public String getNrsjend() {
        return nrsjend;
    }

    public void setNrsjend(String nrsjend) {
        this.nrsjend = nrsjend;
    }

    public String getJldjt() {
        return jldjt;
    }

    public void setJldjt(String jldjt) {
        this.jldjt = jldjt;
    }

    public String getHrmax() {
        return hrmax;
    }

    public void setHrmax(String hrmax) {
        this.hrmax = hrmax;
    }

    public String getMapmax() {
        return mapmax;
    }

    public void setMapmax(String mapmax) {
        this.mapmax = mapmax;
    }

    public String getSapmax() {
        return sapmax;
    }

    public void setSapmax(String sapmax) {
        this.sapmax = sapmax;
    }

    public String getRrmax() {
        return rrmax;
    }

    public void setRrmax(String rrmax) {
        this.rrmax = rrmax;
    }

    public String getTmax() {
        return tmax;
    }

    public void setTmax(String tmax) {
        this.tmax = tmax;
    }

    public String getJxtq() {
        return jxtq;
    }

    public void setJxtq(String jxtq) {
        this.jxtq = jxtq;
    }

    public String getXghxy() {
        return xghxy;
    }

    public void setXghxy(String xghxy) {
        this.xghxy = xghxy;
    }

    public String getCrrt() {
        return crrt;
    }

    public void setCrrt(String crrt) {
        this.crrt = crrt;
    }

    public String getGcs() {
        return gcs;
    }

    public void setGcs(String gcs) {
        this.gcs = gcs;
    }

    public String getHzid() {
        return hzid;
    }

    public void setHzid(String hzid) {
        this.hzid = hzid;
    }

    public String getHzxm() {
        return hzxm;
    }

    public void setHzxm(String hzxm) {
        this.hzxm = hzxm;
    }

    public String getZyh() {
        return zyh;
    }

    public void setZyh(String zyh) {
        this.zyh = zyh;
    }

    public String getNryjbh() {
        return nryjbh;
    }

    public void setNryjbh(String nryjbh) {
        this.nryjbh = nryjbh;
    }

    public String getNrsj() {
        return nrsj;
    }

    public void setNrsj(String nrsj) {
        this.nrsj = nrsj;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getJzks() {
        return jzks;
    }

    public void setJzks(String jzks) {
        this.jzks = jzks;
    }

    public String getBrlb() {
        return brlb;
    }

    public void setBrlb(String brlb) {
        this.brlb = brlb;
    }

    public String getJwhbz() {
        return jwhbz;
    }

    public void setJwhbz(String jwhbz) {
        this.jwhbz = jwhbz;
    }

    public String getGrbw() {
        return grbw;
    }

    public void setGrbw(String grbw) {
        this.grbw = grbw;
    }

    public String getKjywbls() {
        return kjywbls;
    }

    public void setKjywbls(String kjywbls) {
        this.kjywbls = kjywbls;
    }

    public String getQtgrbw() {
        return qtgrbw;
    }

    public void setQtgrbw(String qtgrbw) {
        this.qtgrbw = qtgrbw;
    }

    public String getRysj() {
        return rysj;
    }

    public void setRysj(String rysj) {
        this.rysj = rysj;
    }

    public String getSsyy() {
        return ssyy;
    }

    public void setSsyy(String ssyy) {
        this.ssyy = ssyy;
    }

    public String getJlzmc() {
        return jlzmc;
    }

    public void setJlzmc(String jlzmc) {
        this.jlzmc = jlzmc;
    }
}
