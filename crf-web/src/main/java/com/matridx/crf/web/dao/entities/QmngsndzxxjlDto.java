package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="QmngsndzxxjlDto")
public class QmngsndzxxjlDto extends QmngsndzxxjlModel{
	private List<QmngsdmxqDto> dmxqs;
	private List<QmngsshDto> shs;
	private List<QmngsyzzbDto> yzzbs;
	private List<QmngsxcgDto> xcgs;
	//导出关联字段
	private String SqlParam;
	private String xmsx;
	//患者试验随机号
	private String hzsjh;
	//病历号
	private String blh;
	//医疗中心编号
	private String ylzxbh;
	//医疗中心名称
	private String ylzxmc;
	//患者入组时间
	private String hzrzsj;
	//填写者姓名
	private String txhzxm;
	//联系电话
	private String lxdh;
	//填写日期
	private String txrq;
	//是否成人
	private String sfcr;
	//是否发热等
	private String sfrr;
	//sofa评分大于2
	private String pfdye;
	//是否签署同意书
	private String sfqstys;
	//是否拒绝治疗
	private String sfjjzl;
	//是否肿瘤或者自身免疫病
	private String sfzlhzzsmyjb;
	//孕妇或小于18
	private String yfhxys;
	//未签署知情同意书
	private String wqszqtys;
	//年龄
	private String nl;
	//性别
	private String xb;
	//既往合并症
	private String jwhbz;
	//感染部位
	private String grbw;
	//入院时间
	private String rysj;
	//出院时间
	private String cysj;
	//第一次Q-mNGS
	private String dycqmngs;
	//第一次结果
	private String dycjg;
	//是否判断为责任治病微生物
	private String sfpdwzrzbwsw;
	//第二次Q-mNGS
	private String decqmngs;
	//第二次结果
	private String decjg;
	//送检时间
	private String sjsj;
	//标本类型局灶
	private String bblxjz;
	//标本类型
	private String bblx;
	//72小时感染相关临床疗效判定
	private String grxglclxpd;
	//是否抗菌降阶梯治疗
	private String sfkjjjtzl;
	//抗菌治疗总疗程
	private String kjzlzlc;
	//血管活性药物停用时间
	private String xghxytysj;
	//CRRT停用时间
	private String crrttysj;
	//呼吸机停用时间 ，拔管停止时间
	private String hxjtysj;
	//出ICU时间
	private String cicusj;
	//出ICU状态
	private String cicuzt;
	//转归出院时间
	private String zgcysj;
	//出院状态
	private String cyzt;
	//感染相关诊断
	private String grxgzd;
	//7天感染相关临床疗效判定
	private String stgrxglclxpd;
	//患者随机分组结果
	private String sjfzjg;
	//暴露史
	private String qtgrbw;
	private String kjywjjtzl;
	private String kjzlc;
	private String rzsj;
	//抗菌药物开始时间
	private String kjywkssj;
	//抗菌药物停止时间
	private String kjywtzsj;
	//血管活性药物开始时间
	private String xghxykssj;
	//CRRT开始时间
	private String crrtkssj;
	//呼吸机开始时间
	private String hxjkssj;
	//入ICU时间
	private String ricusj;
	//转归入院时间
	private String zgrysj;
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
	//tnfa
	private String tnfa;
	//c3
	private String c3;
	//c4
	private String c4;
	//cd4t
	private String cd4t;
	//尿素氮
	private String nsd;
	//白蛋白
	private String bdb;
	//肌酐
	private String jg;
	//总胆红素
	private String zdhs;
	//gm实验
	private String gm;
	//g实验
	private String g;
	//tspot
	private String tspot;
	//WBC
	private String wbc;
	//PLT
	private String plt;
	//neu
	private String neu;
	//ly
	private String ly;
	//hb
	private String hb;
	//alt
	private String alt;

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
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

	public String getTnfa() {
		return tnfa;
	}

	public void setTnfa(String tnfa) {
		this.tnfa = tnfa;
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

	public String getCd4t() {
		return cd4t;
	}

	public void setCd4t(String cd4t) {
		this.cd4t = cd4t;
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

	public String getGm() {
		return gm;
	}

	public void setGm(String gm) {
		this.gm = gm;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getTspot() {
		return tspot;
	}

	public void setTspot(String tspot) {
		this.tspot = tspot;
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

	public String getNeu() {
		return neu;
	}

	public void setNeu(String neu) {
		this.neu = neu;
	}

	public String getLy() {
		return ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public String getHb() {
		return hb;
	}

	public void setHb(String hb) {
		this.hb = hb;
	}

	public String getBlh() {
		return blh;
	}

	public void setBlh(String blh) {
		this.blh = blh;
	}

	public String getHxjkssj() {
		return hxjkssj;
	}

	public void setHxjkssj(String hxjkssj) {
		this.hxjkssj = hxjkssj;
	}

	public String getKjywkssj() {
		return kjywkssj;
	}

	public void setKjywkssj(String kjywkssj) {
		this.kjywkssj = kjywkssj;
	}

	public String getKjywtzsj() {
		return kjywtzsj;
	}

	public void setKjywtzsj(String kjywtzsj) {
		this.kjywtzsj = kjywtzsj;
	}

	public String getXghxykssj() {
		return xghxykssj;
	}

	public void setXghxykssj(String xghxykssj) {
		this.xghxykssj = xghxykssj;
	}

	public String getCrrtkssj() {
		return crrtkssj;
	}

	public void setCrrtkssj(String crrtkssj) {
		this.crrtkssj = crrtkssj;
	}

	public String getRicusj() {
		return ricusj;
	}

	public void setRicusj(String ricusj) {
		this.ricusj = ricusj;
	}

	public String getZgrysj() {
		return zgrysj;
	}

	public void setZgrysj(String zgrysj) {
		this.zgrysj = zgrysj;
	}

	public String getKjzlc() {
		return kjzlc;
	}
	public void setKjzlc(String kjzlc) {
		this.kjzlc = kjzlc;
	}
	public String getKjywjjtzl() {
		return kjywjjtzl;
	}
	public void setKjywjjtzl(String kjywjjtzl) {
		this.kjywjjtzl = kjywjjtzl;
	}
	//患者姓名
	public String getXmsx() {
		return xmsx;
	}
	public void setXmsx(String xmsx){
		this.xmsx = xmsx;
	}
	//患者试验随机号
	public String getHzsjh() {
		return hzsjh;
	}
	public void setHzsjh(String hzsjh){
		this.hzsjh = hzsjh;
	}
	//医疗中心编号
	public String getYlzxbh() {
		return ylzxbh;
	}
	public void setYlzxbh(String ylzxbh){
		this.ylzxbh = ylzxbh;
	}
	//医疗中心名称
	public String getYlzxmc() {
		return ylzxmc;
	}
	public void setYlzxmc(String ylzxmc){
		this.ylzxmc = ylzxmc;
	}
	//患者入组时间
	public String getHzrzsj() {
		return hzrzsj;
	}
	public void setHzrzsj(String hzrzsj){
		this.hzrzsj = hzrzsj;
	}
	//填写者姓名
	public String getTxhzxm() {
		return txhzxm;
	}
	public void setTxhzxm(String txhzxm){
		this.txhzxm = txhzxm;
	}
	//联系电话
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh){
		this.lxdh = lxdh;
	}
	//填写日期
	public String getTxrq() {
		return txrq;
	}
	public void setTxrq(String txrq){
		this.txrq = txrq;
	}
	//是否成人
	public String getSfcr() {
		return sfcr;
	}
	public void setSfcr(String sfcr){
		this.sfcr = sfcr;
	}
	//是否发热等
	public String getSfrr() {
		return sfrr;
	}
	public void setSfrr(String sfrr){
		this.sfrr = sfrr;
	}
	//sofa评分大于2
	public String getPfdye() {
		return pfdye;
	}
	public void setPfdye(String pfdye){
		this.pfdye = pfdye;
	}
	//是否签署同意书
	public String getSfqstys() {
		return sfqstys;
	}
	public void setSfqstys(String sfqstys){
		this.sfqstys = sfqstys;
	}
	//是否拒绝治疗
	public String getSfjjzl() {
		return sfjjzl;
	}
	public void setSfjjzl(String sfjjzl){
		this.sfjjzl = sfjjzl;
	}
	//是否肿瘤或者自身免疫病
	public String getSfzlhzzsmyjb() {
		return sfzlhzzsmyjb;
	}
	public void setSfzlhzzsmyjb(String sfzlhzzsmyjb){
		this.sfzlhzzsmyjb = sfzlhzzsmyjb;
	}
	//孕妇或小于18
	public String getYfhxys() {
		return yfhxys;
	}
	public void setYfhxys(String yfhxys){
		this.yfhxys = yfhxys;
	}
	//未签署知情同意书
	public String getWqszqtys() {
		return wqszqtys;
	}
	public void setWqszqtys(String wqszqtys){
		this.wqszqtys = wqszqtys;
	}
	//年龄
	public String getNl() {
		return nl;
	}
	public void setNl(String nl){
		this.nl = nl;
	}
	//性别
	public String getXb() {
		return xb;
	}
	public void setXb(String xb){
		this.xb = xb;
	}
	//既往合并症
	public String getJwhbz() {
		return jwhbz;
	}
	public void setJwhbz(String jwhbz){
		this.jwhbz = jwhbz;
	}
	//感染部位
	public String getGrbw() {
		return grbw;
	}
	public void setGrbw(String grbw){
		this.grbw = grbw;
	}
	//入院时间
	public String getRysj() {
		return rysj;
	}
	public void setRysj(String rysj){
		this.rysj = rysj;
	}
	//出院时间
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj){
		this.cysj = cysj;
	}
	//第一次Q-mNGS
	public String getDycqmngs() {
		return dycqmngs;
	}
	public void setDycqmngs(String dycqmngs){
		this.dycqmngs = dycqmngs;
	}
	//第一次结果
	public String getDycjg() {
		return dycjg;
	}
	public void setDycjg(String dycjg){
		this.dycjg = dycjg;
	}
	//是否判断为责任治病微生物
	public String getSfpdwzrzbwsw() {
		return sfpdwzrzbwsw;
	}
	public void setSfpdwzrzbwsw(String sfpdwzrzbwsw){
		this.sfpdwzrzbwsw = sfpdwzrzbwsw;
	}
	//第二次Q-mNGS
	public String getDecqmngs() {
		return decqmngs;
	}
	public void setDecqmngs(String decqmngs){
		this.decqmngs = decqmngs;
	}
	//第二次结果
	public String getDecjg() {
		return decjg;
	}
	public void setDecjg(String decjg){
		this.decjg = decjg;
	}
	//送检时间
	public String getSjsj() {
		return sjsj;
	}
	public void setSjsj(String sjsj){
		this.sjsj = sjsj;
	}
	//标本类型局灶
	public String getBblxjz() {
		return bblxjz;
	}
	public void setBblxjz(String bblxjz){
		this.bblxjz = bblxjz;
	}
	//标本类型
	public String getBblx() {
		return bblx;
	}
	public void setBblx(String bblx){
		this.bblx = bblx;
	}
	//72小时感染相关临床疗效判定
	public String getGrxglclxpd() {
		return grxglclxpd;
	}
	public void setGrxglclxpd(String grxglclxpd){
		this.grxglclxpd = grxglclxpd;
	}
	//是否抗菌降阶梯治疗
	public String getSfkjjjtzl() {
		return sfkjjjtzl;
	}
	public void setSfkjjjtzl(String sfkjjjtzl){
		this.sfkjjjtzl = sfkjjjtzl;
	}
	//抗菌治疗总疗程
	public String getKjzlzlc() {
		return kjzlzlc;
	}
	public void setKjzlzlc(String kjzlzlc){
		this.kjzlzlc = kjzlzlc;
	}
	//血管活性药物停用时间
	public String getXghxytysj() {
		return xghxytysj;
	}
	public void setXghxytysj(String xghxytysj){
		this.xghxytysj = xghxytysj;
	}
	//CRRT停用时间
	public String getCrrttysj() {
		return crrttysj;
	}
	public void setCrrttysj(String crrttysj){
		this.crrttysj = crrttysj;
	}
	//呼吸机停用时间
	public String getHxjtysj() {
		return hxjtysj;
	}
	public void setHxjtysj(String hxjtysj){
		this.hxjtysj = hxjtysj;
	}
	//出ICU时间
	public String getCicusj() {
		return cicusj;
	}
	public void setCicusj(String cicusj){
		this.cicusj = cicusj;
	}
	//出ICU状态
	public String getCicuzt() {
		return cicuzt;
	}
	public void setCicuzt(String cicuzt){
		this.cicuzt = cicuzt;
	}
	//转归出院时间
	public String getZgcysj() {
		return zgcysj;
	}
	public void setZgcysj(String zgcysj){
		this.zgcysj = zgcysj;
	}
	//出院状态
	public String getCyzt() {
		return cyzt;
	}
	public void setCyzt(String cyzt){
		this.cyzt = cyzt;
	}
	//感染相关诊断
	public String getGrxgzd() {
		return grxgzd;
	}
	public void setGrxgzd(String grxgzd){
		this.grxgzd = grxgzd;
	}
	//7天感染相关临床疗效判定
	public String getStgrxglclxpd() {
		return stgrxglclxpd;
	}
	public void setStgrxglclxpd(String stgrxglclxpd){
		this.stgrxglclxpd = stgrxglclxpd;
	}

	public String getSjfzjg() {
		return sjfzjg;
	}

	public void setSjfzjg(String sjfzjg) {
		this.sjfzjg = sjfzjg;
	}

	public String getQtgrbw() {
		return qtgrbw;
	}

	public void setQtgrbw(String qtgrbw) {
		this.qtgrbw = qtgrbw;
	}

	public String getRzsj() {
		return rzsj;
	}

	public void setRzsj(String rzsj) {
		this.rzsj = rzsj;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public List<QmngsdmxqDto> getDmxqs() {
		return dmxqs;
	}

	public void setDmxqs(List<QmngsdmxqDto> dmxqs) {
		this.dmxqs = dmxqs;
	}

	public List<QmngsshDto> getShs() {
		return shs;
	}

	public void setShs(List<QmngsshDto> shs) {
		this.shs = shs;
	}

	public List<QmngsyzzbDto> getYzzbs() {
		return yzzbs;
	}

	public void setYzzbs(List<QmngsyzzbDto> yzzbs) {
		this.yzzbs = yzzbs;
	}

	public List<QmngsxcgDto> getXcgs() {
		return xcgs;
	}

	public void setXcgs(List<QmngsxcgDto> xcgs) {
		this.xcgs = xcgs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
