package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NdzxxjlModel")
public class NdzxxjlModel extends BaseModel{
	//脓毒症记录id
	private String ndzjlid;
	//患者id
	private String hzid;
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
	//null
	private String crrt;
	//null
	private String gcs;
	//血管活性药
	private String xghxy;
	//APACHE2
	private String apache2;
	//SOFA评分
	private String sofapf;
	//送检前是否使用抗菌药物
	private String sjqsfsykjyw;
	//抗菌药种
	private String kjywzl;
	//null
	private String mcfdna;
	//血培养
	private String xpy;
	//痰涂片
	private String ttp;
	//痰培养
	private String tpy;
	//腹水涂片
	private String fstp;
	//腹水培养
	private String fspy;
	//第一个其他
	private String qtf;
	//第二个其他
	private String qtt;
	//McfDNA结果
	private String mcfdnajg;
	//血培养结果
	private String xpyjg;
	//痰涂片结果
	private String ttpjg;
	//痰培养结果


	private String tpyjg;
	//腹水涂片结果
	private String fstpjg;
	//腹水培养结果
	private String fspyjg;
	//第一个其他结果
	private String qtfjg;
	//第二个其他结果
	private String qttjg;
	//记录第几天
	private String jldjt;
	//记录人员
	private String jlry;
	//新建记录时间
	private String xjsj;
	//操作人，新增
	private String tjr;
	//修改人
	private String xgr;
	//脓毒症记录id
	public String getNdzjlid() {
		return ndzjlid;
	}
	public void setNdzjlid(String ndzjlid){
		this.ndzjlid = ndzjlid;
	}
	//患者id
	public String getHzid() {
		return hzid;
	}
	public void setHzid(String hzid){
		this.hzid = hzid;
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
	//null
	public String getCrrt() {
		return crrt;
	}
	public void setCrrt(String crrt){
		this.crrt = crrt;
	}
	//null
	public String getGcs() {
		return gcs;
	}
	public void setGcs(String gcs){
		this.gcs = gcs;
	}
	//血管活性药
	public String getXghxy() {
		return xghxy;
	}
	public void setXghxy(String xghxy){
		this.xghxy = xghxy;
	}
	//APACHE2
	public String getApache2() {
		return apache2;
	}
	public void setApache2(String apache2){
		this.apache2 = apache2;
	}
	//SOFA评分
	public String getSofapf() {
		return sofapf;
	}
	public void setSofapf(String sofapf){
		this.sofapf = sofapf;
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
	//null
	public String getMcfdna() {
		return mcfdna;
	}
	public void setMcfdna(String mcfdna){
		this.mcfdna = mcfdna;
	}
	//血培养
	public String getXpy() {
		return xpy;
	}
	public void setXpy(String xpy){
		this.xpy = xpy;
	}
	//痰涂片
	public String getTtp() {
		return ttp;
	}
	public void setTtp(String ttp){
		this.ttp = ttp;
	}
	//痰培养
	public String getTpy() {
		return tpy;
	}
	public void setTpy(String tpy){
		this.tpy = tpy;
	}
	//腹水涂片
	public String getFstp() {
		return fstp;
	}
	public void setFstp(String fstp){
		this.fstp = fstp;
	}
	//腹水培养
	public String getFspy() {
		return fspy;
	}
	public void setFspy(String fspy){
		this.fspy = fspy;
	}
	//第一个其他
	public String getQtf() {
		return qtf;
	}
	public void setQtf(String qtf){
		this.qtf = qtf;
	}
	//第二个其他
	public String getQtt() {
		return qtt;
	}
	public void setQtt(String qtt){
		this.qtt = qtt;
	}
	//McfDNA结果
	public String getMcfdnajg() {
		return mcfdnajg;
	}
	public void setMcfdnajg(String mcfdnajg){
		this.mcfdnajg = mcfdnajg;
	}
	//血培养结果
	public String getXpyjg() {
		return xpyjg;
	}
	public void setXpyjg(String xpyjg){
		this.xpyjg = xpyjg;
	}
	//痰涂片结果
	public String getTtpjg() {
		return ttpjg;
	}
	public void setTtpjg(String ttpjg){
		this.ttpjg = ttpjg;
	}
	//痰培养结果


	public String getTpyjg() {
		return tpyjg;
	}
	public void setTpyjg(String tpyjg){
		this.tpyjg = tpyjg;
	}
	//腹水涂片结果
	public String getFstpjg() {
		return fstpjg;
	}
	public void setFstpjg(String fstpjg){
		this.fstpjg = fstpjg;
	}
	//腹水培养结果
	public String getFspyjg() {
		return fspyjg;
	}
	public void setFspyjg(String fspyjg){
		this.fspyjg = fspyjg;
	}
	//第一个其他结果
	public String getQtfjg() {
		return qtfjg;
	}
	public void setQtfjg(String qtfjg){
		this.qtfjg = qtfjg;
	}
	//第二个其他结果
	public String getQttjg() {
		return qttjg;
	}
	public void setQttjg(String qttjg){
		this.qttjg = qttjg;
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
	//新建记录时间
	public String getXjsj() {
		return xjsj;
	}
	public void setXjsj(String xjsj){
		this.xjsj = xjsj;
	}
	//操作人，新增
	public String getTjr() {
		return tjr;
	}
	public void setTjr(String tjr){
		this.tjr = tjr;
	}
	//修改人
	public String getXgr() {
		return xgr;
	}
	public void setXgr(String xgr){
		this.xgr = xgr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
