package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JnsjbgjlModel")
public class JnsjbgjlModel extends BaseModel{
	//艰难梭菌报告记录id
	private String jnsjbgid;
	//艰难梭菌患者id
	private String jnsjhzid;
	//病例入组单位
	private String blrzdw;
	//科室
	private String ks;
	//病例入组编号
	private String blrzbh;
	//住院号
	private String zyh;
	//填表时间
	private String tbsj;
	//就诊类型
	private String jzlx;
	//是否腹泻   (是：1    否：0)
	private String sffx;
	//腹泻频率   (4-5次：0    5-6次：1    6次以上：2)
	private String fxpl;
	//是否腹胀   (是：1    否：0)
	private String sffz;
	//是否有中毒性巨结肠   (是：1    否：0)
	private String sfyzdxjjc;
	//是否肠梗阻   (是：1    否：0)
	private String sfcgz;
	//是否低血压   (是：1    否：0)
	private String sfdxy;
	//是否休克   (是：1    否：0)
	private String sfxk;
	//大便性状
	private String dbxz;
	//粪红细胞
	private String fhxb;
	//粪白细胞
	private String fbxb;
	//粪酵母样真菌
	private String fjmyzj;
	//粪隐血
	private String fyx;
	//转铁蛋白
	private String ztdb;
	//钙卫蛋白
	private String gwdb;
	//WBC
	private String wbc;
	//N(%)
	private String n;
	//L(%)
	private String l;
	//HCT(%)
	private String hct;
	//Hb(g/L)
	private String hb;
	//Plt
	private String plt;
	//ALB(g/L)
	private String alb;
	//CK(U/L)
	private String ck;
	//TBIL(umol/L)
	private String tbil;
	//BUN(umol/L)
	private String bun;
	//Cr(umol/L)
	private String cr;
	//ESR(mm/hr)
	private String esr;
	//CRP(mg/L)
	private String crp;
	//PCT(ng/ml)
	private String pct;
	//是否有伪膜性肠炎
	private String sfywmxcy;
	//是否治疗CDI
	private String sfzlcdi;
	//30天内是否死亡
	private String sfsw;
	//mlst分型
	private String mlst;
	//tcda
	private String tcda;
	//tcdb
	private String tcdb;
	//ctdc
	private String ctdc;
	//ctdd
	private String ctdd;
	//核糖体分型
	private String httfx;
	//美罗培南
	private String mlpn;
	//万古霉素
	private String wgms;
	//利奈唑胺
	private String lnza;
	//甲硝唑
	private String jxz;
	//莫西沙星
	private String mxsx;
	//替考拉宁
	private String tkln;
	//利福昔明
	private String lfxm;
	//达托霉素
	private String dtms;
	//克林霉素
	private String klms;

	public String getKlms() {
		return klms;
	}

	public void setKlms(String klms) {
		this.klms = klms;
	}

	public String getMlst() {
		return mlst;
	}

	public void setMlst(String mlst) {
		this.mlst = mlst;
	}

	public String getTcda() {
		return tcda;
	}

	public void setTcda(String tcda) {
		this.tcda = tcda;
	}

	public String getTcdb() {
		return tcdb;
	}

	public void setTcdb(String tcdb) {
		this.tcdb = tcdb;
	}

	public String getCtdc() {
		return ctdc;
	}

	public void setCtdc(String ctdc) {
		this.ctdc = ctdc;
	}

	public String getCtdd() {
		return ctdd;
	}

	public void setCtdd(String ctdd) {
		this.ctdd = ctdd;
	}

	public String getHttfx() {
		return httfx;
	}

	public void setHttfx(String httfx) {
		this.httfx = httfx;
	}

	public String getMlpn() {
		return mlpn;
	}

	public void setMlpn(String mlpn) {
		this.mlpn = mlpn;
	}

	public String getWgms() {
		return wgms;
	}

	public void setWgms(String wgms) {
		this.wgms = wgms;
	}

	public String getLnza() {
		return lnza;
	}

	public void setLnza(String lnza) {
		this.lnza = lnza;
	}

	public String getJxz() {
		return jxz;
	}

	public void setJxz(String jxz) {
		this.jxz = jxz;
	}

	public String getMxsx() {
		return mxsx;
	}

	public void setMxsx(String mxsx) {
		this.mxsx = mxsx;
	}

	public String getTkln() {
		return tkln;
	}

	public void setTkln(String tkln) {
		this.tkln = tkln;
	}

	public String getLfxm() {
		return lfxm;
	}

	public void setLfxm(String lfxm) {
		this.lfxm = lfxm;
	}

	public String getDtms() {
		return dtms;
	}

	public void setDtms(String dtms) {
		this.dtms = dtms;
	}

	//艰难梭菌报告记录id
	public String getJnsjbgid() {
		return jnsjbgid;
	}
	public void setJnsjbgid(String jnsjbgid){
		this.jnsjbgid = jnsjbgid;
	}
	//艰难梭菌患者id
	public String getJnsjhzid() {
		return jnsjhzid;
	}
	public void setJnsjhzid(String jnsjhzid){
		this.jnsjhzid = jnsjhzid;
	}
	//病例入组单位
	public String getBlrzdw() {
		return blrzdw;
	}
	public void setBlrzdw(String blrzdw){
		this.blrzdw = blrzdw;
	}
	//科室
	public String getKs() {
		return ks;
	}
	public void setKs(String ks){
		this.ks = ks;
	}
	//病例入组编号
	public String getBlrzbh() {
		return blrzbh;
	}
	public void setBlrzbh(String blrzbh){
		this.blrzbh = blrzbh;
	}
	//住院号
	public String getZyh() {
		return zyh;
	}
	public void setZyh(String zyh){
		this.zyh = zyh;
	}
	//填表时间
	public String getTbsj() {
		return tbsj;
	}
	public void setTbsj(String tbsj){
		this.tbsj = tbsj;
	}
	//就诊类型
	public String getJzlx() {
		return jzlx;
	}
	public void setJzlx(String jzlx){
		this.jzlx = jzlx;
	}
	//是否腹泻
	public String getSffx() {
		return sffx;
	}
	public void setSffx(String sffx){
		this.sffx = sffx;
	}
	//腹泻频率
	public String getFxpl() {
		return fxpl;
	}
	public void setFxpl(String fxpl){
		this.fxpl = fxpl;
	}
	//是否腹胀
	public String getSffz() {
		return sffz;
	}
	public void setSffz(String sffz){
		this.sffz = sffz;
	}
	//是否有中毒性巨结肠
	public String getSfyzdxjjc() {
		return sfyzdxjjc;
	}
	public void setSfyzdxjjc(String sfyzdxjjc){
		this.sfyzdxjjc = sfyzdxjjc;
	}
	//是否肠梗阻
	public String getSfcgz() {
		return sfcgz;
	}
	public void setSfcgz(String sfcgz){
		this.sfcgz = sfcgz;
	}
	//是否低血压
	public String getSfdxy() {
		return sfdxy;
	}
	public void setSfdxy(String sfdxy){
		this.sfdxy = sfdxy;
	}
	//是否休克
	public String getSfxk() {
		return sfxk;
	}
	public void setSfxk(String sfxk){
		this.sfxk = sfxk;
	}
	//大便性状
	public String getDbxz() {
		return dbxz;
	}
	public void setDbxz(String dbxz){
		this.dbxz = dbxz;
	}
	//粪红细胞
	public String getFhxb() {
		return fhxb;
	}
	public void setFhxb(String fhxb){
		this.fhxb = fhxb;
	}
	//粪白细胞
	public String getFbxb() {
		return fbxb;
	}
	public void setFbxb(String fbxb){
		this.fbxb = fbxb;
	}
	//粪酵母样真菌
	public String getFjmyzj() {
		return fjmyzj;
	}
	public void setFjmyzj(String fjmyzj){
		this.fjmyzj = fjmyzj;
	}
	//粪隐血
	public String getFyx() {
		return fyx;
	}
	public void setFyx(String fyx){
		this.fyx = fyx;
	}
	//转铁蛋白
	public String getZtdb() {
		return ztdb;
	}
	public void setZtdb(String ztdb){
		this.ztdb = ztdb;
	}
	//钙卫蛋白
	public String getGwdb() {
		return gwdb;
	}
	public void setGwdb(String gwdb){
		this.gwdb = gwdb;
	}
	//WBC
	public String getWbc() {
		return wbc;
	}
	public void setWbc(String wbc){
		this.wbc = wbc;
	}
	//N(%)
	public String getN() {
		return n;
	}
	public void setN(String n){
		this.n = n;
	}
	//L(%)
	public String getL() {
		return l;
	}
	public void setL(String l){
		this.l = l;
	}
	//HCT(%)
	public String getHct() {
		return hct;
	}
	public void setHct(String hct){
		this.hct = hct;
	}
	//Hb(g/L)
	public String getHb() {
		return hb;
	}
	public void setHb(String hb){
		this.hb = hb;
	}
	//Plt
	public String getPlt() {
		return plt;
	}
	public void setPlt(String plt){
		this.plt = plt;
	}
	//ALB(g/L)
	public String getAlb() {
		return alb;
	}
	public void setAlb(String alb){
		this.alb = alb;
	}
	//CK(U/L)
	public String getCk() {
		return ck;
	}
	public void setCk(String ck){
		this.ck = ck;
	}
	//TBIL(umol/L)
	public String getTbil() {
		return tbil;
	}
	public void setTbil(String tbil){
		this.tbil = tbil;
	}
	//BUN(umol/L)
	public String getBun() {
		return bun;
	}
	public void setBun(String bun){
		this.bun = bun;
	}
	//Cr(umol/L)
	public String getCr() {
		return cr;
	}
	public void setCr(String cr){
		this.cr = cr;
	}
	//ESR(mm/hr)
	public String getEsr() {
		return esr;
	}
	public void setEsr(String esr){
		this.esr = esr;
	}
	//CRP(mg/L)
	public String getCrp() {
		return crp;
	}
	public void setCrp(String crp){
		this.crp = crp;
	}
	//PCT(ng/ml)
	public String getPct() {
		return pct;
	}
	public void setPct(String pct){
		this.pct = pct;
	}
	//是否有伪膜性肠炎
	public String getSfywmxcy() {
		return sfywmxcy;
	}
	public void setSfywmxcy(String sfywmxcy){
		this.sfywmxcy = sfywmxcy;
	}
	//是否治疗CDI
	public String getSfzlcdi() {
		return sfzlcdi;
	}
	public void setSfzlcdi(String sfzlcdi){
		this.sfzlcdi = sfzlcdi;
	}
	//30天内是否死亡
	public String getSfsw() {
		return sfsw;
	}
	public void setSfsw(String sfsw){
		this.sfsw = sfsw;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
