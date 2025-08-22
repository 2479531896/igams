package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QmngsndzxxjlModel")
public class QmngsndzxxjlModel extends BaseModel{
	//Q-mNGS脓毒症记录id
	private String qmngsndzjlid;
	//记录日期
	private String jlrq;
	//hrmax
	private String hrmax;
	//mapmax
	private String mapmax;
	//sapmax
	private String sapmax;
	//rrmax
	private String rrmax;
	//null
	private String tmax;
	//机械通气
	private String jxtq;
	//Q-mNGS患者信息
	private String qmngshzid;
	//gcs
	private String gcs;
	//null
	private String crrt;
	//血管活性药
	private String xghxy;
	//送检前是否使用抗菌药物
	private String sjqsfsykjyw;
	//抗菌药种
	private String kjywzl;
	//是否外周血Q-mNGS
	private String sfwzxqmngs;
	//外周血Q-mNGS结果
	private String wzxqmngsjg;
	//局灶Q-mNGS
	private String jzqmngs;
	//是否局灶Q-mNGS
	private String sfjzqmngs;
	//局灶Q-mNGS结果
	private String jzqmngsjg;
	//是否外周血培养
	private String fswzxpy;
	//外周血培养结果
	private String wzxpyjg;
	//局灶培养
	private String jzpy;
	//是否局灶培养
	private String sfjzpy;
	//血培养结果
	private String jzpyjg;
	//是否T-sport
	private String sftsport;
	//T-sport结果
	private String tsportjg;
	//是否G实验
	private String sfgsy;
	//G实验结果
	private String gsyjg;
	//是否GM实验
	private String sfgmsy;
	//是否CMVPCR
	private String sfcmvpcr;
	//CMVPCR结果
	private String cmvpcrjg;
	//是否EBVPCR
	private String sfebvpcr;
	//EBVPCR结果
	private String ebvpcrjg;
	//其他1
	private String qt1;
	//是否其他1
	private String sfqt1;
	//胸部CT
	private String xbct;
	//CT结果描述
	private String ctjgms;
	//CT结果
	private String ctjg;
	//其他1结果
	private String qt1jg;
	//第二个其他结果
	private String qt2jg;
	//记录第几天
	private String jldjt;
	//记录人员
	private String jlry;
	//其他3
	private String qt3;
	//是否其他3
	private String sfqt3;
	//其他3结果3
	private String qt3jg;
	//其他2
	private String qt2;
	//是否其他2
	private String sfqt2;
	private String gmsyjg;
	//Q-mNGS结果：是否致病微生物1
	private String sfzbwsw11;
	//Q-mNGS结果：冰原指数1
	private String bfzs11;
	//Q-mNGS结果：是否致病微生物2
	private String sfzbwsw12;
	//Q-mNGS结果：冰原指数2
	private String bfzs12;
	//其他培养种类及结果：是否致病微生物1
	private String sfzbwsw21;
	//其他培养种类及结果：是否致病微生物2
	private String sfzbwsw22;
	//其他培养种类及结果：是否致病微生物3
	private String sfzbwsw23;
	//其他培养种类及结果：是否致病微生物4
	private String sfzbwsw24;
	//局灶培养部位2
	private String jzpy2;
	//局灶培养结果2
	private String jzpyjg2;
	//局灶培养部位3
	private String jzpy3;
	//局灶培养结果3
	private String jzpyjg3;
	//其他结果
	private String qtjg;
	//'送检时培养结果阳性'
	private String sjspyjgyx;
	//'送检时标本类型'
	private String sjsbblx;
	//'送检时培养结果'
	private String sjspyjg;
	//局灶培养部位4
	private String jzpy4;
	//局灶培养结果4
	private String jzpyjg4;
	private String sjsj;
	private String sjsj2;
	private String sjsj3;
	private String sjsj4;
	private String jgsj;
	private String jgsj2;
	private String jgsj3;
	private String jgsj4;
	//感染相关临床诊疗判定
	private String grxglclxpd;

	public String getGrxglclxpd() {
		return grxglclxpd;
	}

	public void setGrxglclxpd(String grxglclxpd) {
		this.grxglclxpd = grxglclxpd;
	}

	public String getJzpy4() {
		return jzpy4;
	}

	public void setJzpy4(String jzpy4) {
		this.jzpy4 = jzpy4;
	}

	public String getJzpyjg4() {
		return jzpyjg4;
	}

	public void setJzpyjg4(String jzpyjg4) {
		this.jzpyjg4 = jzpyjg4;
	}

	public String getSjsj() {
		return sjsj;
	}

	public void setSjsj(String sjsj) {
		this.sjsj = sjsj;
	}

	public String getSjsj2() {
		return sjsj2;
	}

	public void setSjsj2(String sjsj2) {
		this.sjsj2 = sjsj2;
	}

	public String getSjsj3() {
		return sjsj3;
	}

	public void setSjsj3(String sjsj3) {
		this.sjsj3 = sjsj3;
	}

	public String getSjsj4() {
		return sjsj4;
	}

	public void setSjsj4(String sjsj4) {
		this.sjsj4 = sjsj4;
	}

	public String getJgsj() {
		return jgsj;
	}

	public void setJgsj(String jgsj) {
		this.jgsj = jgsj;
	}

	public String getJgsj2() {
		return jgsj2;
	}

	public void setJgsj2(String jgsj2) {
		this.jgsj2 = jgsj2;
	}

	public String getJgsj3() {
		return jgsj3;
	}

	public void setJgsj3(String jgsj3) {
		this.jgsj3 = jgsj3;
	}

	public String getJgsj4() {
		return jgsj4;
	}

	public void setJgsj4(String jgsj4) {
		this.jgsj4 = jgsj4;
	}

	public String getSjspyjgyx() {
		return sjspyjgyx;
	}

	public void setSjspyjgyx(String sjspyjgyx) {
		this.sjspyjgyx = sjspyjgyx;
	}

	public String getSjsbblx() {
		return sjsbblx;
	}

	public void setSjsbblx(String sjsbblx) {
		this.sjsbblx = sjsbblx;
	}

	public String getSjspyjg() {
		return sjspyjg;
	}

	public void setSjspyjg(String sjspyjg) {
		this.sjspyjg = sjspyjg;
	}

	public String getJzpy2() {
		return jzpy2;
	}

	public void setJzpy2(String jzpy2) {
		this.jzpy2 = jzpy2;
	}

	public String getJzpyjg2() {
		return jzpyjg2;
	}

	public void setJzpyjg2(String jzpyjg2) {
		this.jzpyjg2 = jzpyjg2;
	}

	public String getJzpy3() {
		return jzpy3;
	}

	public void setJzpy3(String jzpy3) {
		this.jzpy3 = jzpy3;
	}

	public String getJzpyjg3() {
		return jzpyjg3;
	}

	public void setJzpyjg3(String jzpyjg3) {
		this.jzpyjg3 = jzpyjg3;
	}

	public String getQtjg() {
		return qtjg;
	}

	public void setQtjg(String qtjg) {
		this.qtjg = qtjg;
	}

	public String getSfzbwsw11() {
		return sfzbwsw11;
	}

	public void setSfzbwsw11(String sfzbwsw11) {
		this.sfzbwsw11 = sfzbwsw11;
	}

	public String getBfzs11() {
		return bfzs11;
	}

	public void setBfzs11(String bfzs11) {
		this.bfzs11 = bfzs11;
	}

	public String getSfzbwsw12() {
		return sfzbwsw12;
	}

	public void setSfzbwsw12(String sfzbwsw12) {
		this.sfzbwsw12 = sfzbwsw12;
	}

	public String getBfzs12() {
		return bfzs12;
	}

	public void setBfzs12(String bfzs12) {
		this.bfzs12 = bfzs12;
	}

	public String getSfzbwsw21() {
		return sfzbwsw21;
	}

	public void setSfzbwsw21(String sfzbwsw21) {
		this.sfzbwsw21 = sfzbwsw21;
	}

	public String getSfzbwsw22() {
		return sfzbwsw22;
	}

	public void setSfzbwsw22(String sfzbwsw22) {
		this.sfzbwsw22 = sfzbwsw22;
	}

	public String getSfzbwsw23() {
		return sfzbwsw23;
	}

	public void setSfzbwsw23(String sfzbwsw23) {
		this.sfzbwsw23 = sfzbwsw23;
	}

	public String getSfzbwsw24() {
		return sfzbwsw24;
	}

	public void setSfzbwsw24(String sfzbwsw24) {
		this.sfzbwsw24 = sfzbwsw24;
	}

	//Q-mNGS脓毒症记录id
	public String getQmngsndzjlid() {
		return qmngsndzjlid;
	}
	public void setQmngsndzjlid(String qmngsndzjlid){
		this.qmngsndzjlid = qmngsndzjlid;
	}
	//记录日期
	public String getJlrq() {
		return jlrq;
	}
	public void setJlrq(String jlrq){
		this.jlrq = jlrq;
	}
	//hrmax
	public String getHrmax() {
		return hrmax;
	}
	public void setHrmax(String hrmax){
		this.hrmax = hrmax;
	}
	//mapmax
	public String getMapmax() {
		return mapmax;
	}
	public void setMapmax(String mapmax){
		this.mapmax = mapmax;
	}
	//sapmax
	public String getSapmax() {
		return sapmax;
	}
	public void setSapmax(String sapmax){
		this.sapmax = sapmax;
	}
	//rrmax
	public String getRrmax() {
		return rrmax;
	}
	public void setRrmax(String rrmax){
		this.rrmax = rrmax;
	}
	//null
	public String getTmax() {
		return tmax;
	}
	public void setTmax(String tmax){
		this.tmax = tmax;
	}
	//机械通气
	public String getJxtq() {
		return jxtq;
	}
	public void setJxtq(String jxtq){
		this.jxtq = jxtq;
	}
	//Q-mNGS患者信息
	public String getQmngshzid() {
		return qmngshzid;
	}
	public void setQmngshzid(String qmngshzid){
		this.qmngshzid = qmngshzid;
	}
	//gcs
	public String getGcs() {
		return gcs;
	}
	public void setGcs(String gcs){
		this.gcs = gcs;
	}
	//null
	public String getCrrt() {
		return crrt;
	}
	public void setCrrt(String crrt){
		this.crrt = crrt;
	}
	//血管活性药
	public String getXghxy() {
		return xghxy;
	}
	public void setXghxy(String xghxy){
		this.xghxy = xghxy;
	}
	//送检前是否使用抗菌药物
	public String getSjqsfsykjyw() {
		return sjqsfsykjyw;
	}
	public void setSjqsfsykjyw(String sjqsfsykjyw){
		this.sjqsfsykjyw = sjqsfsykjyw;
	}
	//抗菌药种
	public String getKjywzl() {
		return kjywzl;
	}
	public void setKjywzl(String kjywzl){
		this.kjywzl = kjywzl;
	}
	//是否外周血Q-mNGS
	public String getSfwzxqmngs() {
		return sfwzxqmngs;
	}
	public void setSfwzxqmngs(String sfwzxqmngs){
		this.sfwzxqmngs = sfwzxqmngs;
	}
	//外周血Q-mNGS结果
	public String getWzxqmngsjg() {
		return wzxqmngsjg;
	}
	public void setWzxqmngsjg(String wzxqmngsjg){
		this.wzxqmngsjg = wzxqmngsjg;
	}
	//局灶Q-mNGS
	public String getJzqmngs() {
		return jzqmngs;
	}
	public void setJzqmngs(String jzqmngs){
		this.jzqmngs = jzqmngs;
	}
	//是否局灶Q-mNGS
	public String getSfjzqmngs() {
		return sfjzqmngs;
	}
	public void setSfjzqmngs(String sfjzqmngs){
		this.sfjzqmngs = sfjzqmngs;
	}
	//局灶Q-mNGS结果
	public String getJzqmngsjg() {
		return jzqmngsjg;
	}
	public void setJzqmngsjg(String jzqmngsjg){
		this.jzqmngsjg = jzqmngsjg;
	}
	//是否外周血培养
	public String getFswzxpy() {
		return fswzxpy;
	}
	public void setFswzxpy(String fswzxpy){
		this.fswzxpy = fswzxpy;
	}
	//外周血培养结果
	public String getWzxpyjg() {
		return wzxpyjg;
	}
	public void setWzxpyjg(String wzxpyjg){
		this.wzxpyjg = wzxpyjg;
	}
	//局灶培养
	public String getJzpy() {
		return jzpy;
	}
	public void setJzpy(String jzpy){
		this.jzpy = jzpy;
	}
	//是否局灶培养
	public String getSfjzpy() {
		return sfjzpy;
	}
	public void setSfjzpy(String sfjzpy){
		this.sfjzpy = sfjzpy;
	}
	//血培养结果
	public String getJzpyjg() {
		return jzpyjg;
	}
	public void setJzpyjg(String jzpyjg){
		this.jzpyjg = jzpyjg;
	}
	//是否T-sport
	public String getSftsport() {
		return sftsport;
	}
	public void setSftsport(String sftsport){
		this.sftsport = sftsport;
	}
	//T-sport结果
	public String getTsportjg() {
		return tsportjg;
	}
	public void setTsportjg(String tsportjg){
		this.tsportjg = tsportjg;
	}
	//是否G实验
	public String getSfgsy() {
		return sfgsy;
	}
	public void setSfgsy(String sfgsy){
		this.sfgsy = sfgsy;
	}
	//G实验结果
	public String getGsyjg() {
		return gsyjg;
	}
	public void setGsyjg(String gsyjg){
		this.gsyjg = gsyjg;
	}
	//是否GM实验
	public String getSfgmsy() {
		return sfgmsy;
	}
	public void setSfgmsy(String sfgmsy){
		this.sfgmsy = sfgmsy;
	}
	//是否CMVPCR
	public String getSfcmvpcr() {
		return sfcmvpcr;
	}
	public void setSfcmvpcr(String sfcmvpcr){
		this.sfcmvpcr = sfcmvpcr;
	}
	//CMVPCR结果
	public String getCmvpcrjg() {
		return cmvpcrjg;
	}
	public void setCmvpcrjg(String cmvpcrjg){
		this.cmvpcrjg = cmvpcrjg;
	}
	//是否EBVPCR
	public String getSfebvpcr() {
		return sfebvpcr;
	}
	public void setSfebvpcr(String sfebvpcr){
		this.sfebvpcr = sfebvpcr;
	}
	//EBVPCR结果
	public String getEbvpcrjg() {
		return ebvpcrjg;
	}
	public void setEbvpcrjg(String ebvpcrjg){
		this.ebvpcrjg = ebvpcrjg;
	}
	//其他1
	public String getQt1() {
		return qt1;
	}
	public void setQt1(String qt1){
		this.qt1 = qt1;
	}
	//是否其他1
	public String getSfqt1() {
		return sfqt1;
	}
	public void setSfqt1(String sfqt1){
		this.sfqt1 = sfqt1;
	}
	//胸部CT
	public String getXbct() {
		return xbct;
	}
	public void setXbct(String xbct){
		this.xbct = xbct;
	}
	//CT结果描述
	public String getCtjgms() {
		return ctjgms;
	}
	public void setCtjgms(String ctjgms){
		this.ctjgms = ctjgms;
	}
	//CT结果
	public String getCtjg() {
		return ctjg;
	}
	public void setCtjg(String ctjg){
		this.ctjg = ctjg;
	}
	//其他1结果

	public String getQt1jg() {
		return qt1jg;
	}

	public void setQt1jg(String qt1jg) {
		this.qt1jg = qt1jg;
	}
	//记录第几天
	public String getJldjt() {
		return jldjt;
	}
	public void setJldjt(String jldjt){
		this.jldjt = jldjt;
	}
	//记录人员
	public String getJlry() {
		return jlry;
	}
	public void setJlry(String jlry){
		this.jlry = jlry;
	}
	//其他3
	public String getQt3() {
		return qt3;
	}
	public void setQt3(String qt3){
		this.qt3 = qt3;
	}
	//是否其他3
	public String getSfqt3() {
		return sfqt3;
	}
	public void setSfqt3(String sfqt3){
		this.sfqt3 = sfqt3;
	}
	//其他3结果3
	public String getQt3jg() {
		return qt3jg;
	}
	public void setQt3jg(String qt3jg){
		this.qt3jg = qt3jg;
	}
	//其他2
	public String getQt2() {
		return qt2;
	}
	public void setQt2(String qt2){
		this.qt2 = qt2;
	}
	//是否其他2
	public String getSfqt2() {
		return sfqt2;
	}
	public void setSfqt2(String sfqt2){
		this.sfqt2 = sfqt2;
	}
	public String getGmsyjg() {
		return gmsyjg;
	}

	public void setGmsyjg(String gmsyjg) {
		this.gmsyjg = gmsyjg;
	}
	public String getQt2jg() {
		return qt2jg;
	}

	public void setQt2jg(String qt2jg) {
		this.qt2jg = qt2jg;
	}


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
