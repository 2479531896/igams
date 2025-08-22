package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HzxxModel")
public class HzxxModel extends BaseModel{
	//患者信息
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
	//就诊科室（关联系统参数表）
	private String jzks;
	//病人类别（关联系统参数表）
	private String brlb;
	//既往合并症（多选用逗号隔开，关联系统参数表）
	private String jwhbz;
	//感染部位（多选用逗号隔开，关联系统参数表）
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
	//是否抗菌药物降阶梯治疗
	private String kjywjjtzl;
	//抗菌药物治疗总疗程
	private String kjzlc;
	//血管活性药物停用时间（到分）
	private String xghxytysj;
	//CRRT停用时间（到分）
	private String crrttysj;
	//呼吸机停用时间（到分）
	private String hxjtysj;
	//出ICU时间（到分）
	private String cicusj;
	//出icu状态（关联基础数据表）
	private String cicuzt;
	//出院时间
	private String cysj;
	//出院状态（关联基础数据表）
	private String cyzt;
	//新建记录时间
	private String xjsj;
	//操作人，新增
	private String tjr;
	//修改人，操作人，
	private String xgr;
	//亚组
	private String yz;
	//排序
	private String nryjbhpx;
	
	public String getNryjbhpx() {
		return nryjbhpx;
	}
	public void setNryjbhpx(String nryjbhpx) {
		this.nryjbhpx = nryjbhpx;
	}
	//亚组
	public String getYz() {
		return yz;
	}
	//亚组
	public void setYz(String yz) {
		this.yz = yz;
	}
	//患者信息
	public String getHzid() {
		return hzid;
	}
	public void setHzid(String hzid){
		this.hzid = hzid;
	}
	//患者姓名
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm){
		this.hzxm = hzxm;
	}
	//住院号
	public String getZyh() {
		return zyh;
	}
	public void setZyh(String zyh){
		this.zyh = zyh;
	}
	//纳入研究编号
	public String getNryjbh() {
		return nryjbh;
	}
	public void setNryjbh(String nryjbh){
		this.nryjbh = nryjbh;
	}
	//纳入时间
	public String getNrsj() {
		return nrsj;
	}
	public void setNrsj(String nrsj){
		this.nrsj = nrsj;
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
	//就诊科室（关联系统参数表）
	public String getJzks() {
		return jzks;
	}
	public void setJzks(String jzks){
		this.jzks = jzks;
	}
	//病人类别（关联系统参数表）
	public String getBrlb() {
		return brlb;
	}
	public void setBrlb(String brlb){
		this.brlb = brlb;
	}
	//既往合并症（多选用逗号隔开，关联系统参数表）
	public String getJwhbz() {
		return jwhbz;
	}
	public void setJwhbz(String jwhbz){
		this.jwhbz = jwhbz;
	}
	//感染部位（多选用逗号隔开，关联系统参数表）
	public String getGrbw() {
		return grbw;
	}
	public void setGrbw(String grbw){
		this.grbw = grbw;
	}
	//发病前多少天存在抗菌药物暴露史（关联系统参数表）
	public String getKjywbls() {
		return kjywbls;
	}
	public void setKjywbls(String kjywbls){
		this.kjywbls = kjywbls;
	}
	//其他感染部位
	public String getQtgrbw() {
		return qtgrbw;
	}
	public void setQtgrbw(String qtgrbw){
		this.qtgrbw = qtgrbw;
	}
	//入院时间
	public String getRysj() {
		return rysj;
	}
	public void setRysj(String rysj){
		this.rysj = rysj;
	}
	//所属医院
	public String getSsyy() {
		return ssyy;
	}
	public void setSsyy(String ssyy){
		this.ssyy = ssyy;
	}
	//记录者名称
	public String getJlzmc() {
		return jlzmc;
	}
	public void setJlzmc(String jlzmc){
		this.jlzmc = jlzmc;
	}
	
	//是否抗菌药物降阶梯治疗
	public String getKjywjjtzl() {
		return kjywjjtzl;
	}
	public void setKjywjjtzl(String kjywjjtzl){
		this.kjywjjtzl = kjywjjtzl;
	}
	//抗菌药物治疗总疗程
	public String getKjzlc() {
		return kjzlc;
	}
	public void setKjzlc(String kjzlc){
		this.kjzlc = kjzlc;
	}
	//血管活性药物停用时间（到分）
	public String getXghxytysj() {
		return xghxytysj;
	}
	public void setXghxytysj(String xghxytysj){
		this.xghxytysj = xghxytysj;
	}
	//CRRT停用时间（到分）
	public String getCrrttysj() {
		return crrttysj;
	}
	public void setCrrttysj(String crrttysj){
		this.crrttysj = crrttysj;
	}
	//呼吸机停用时间（到分）
	public String getHxjtysj() {
		return hxjtysj;
	}
	public void setHxjtysj(String hxjtysj){
		this.hxjtysj = hxjtysj;
	}
	//出ICU时间（到分）
	public String getCicusj() {
		return cicusj;
	}
	public void setCicusj(String cicusj){
		this.cicusj = cicusj;
	}
	//出icu状态（关联基础数据表）
	public String getCicuzt() {
		return cicuzt;
	}
	public void setCicuzt(String cicuzt){
		this.cicuzt = cicuzt;
	}
	//出院时间
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj){
		this.cysj = cysj;
	}
	//出院状态（关联基础数据表）
	public String getCyzt() {
		return cyzt;
	}
	public void setCyzt(String cyzt){
		this.cyzt = cyzt;
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
	//修改人，操作人，
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
