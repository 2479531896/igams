package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QmngshzxxModel")
public class QmngshzxxModel extends BaseModel{
	//Q-mNGS患者信息
	private String qmngshzid;
	//患者姓名
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
	//Q-mNGS患者信息
	public String getQmngshzid() {
		return qmngshzid;
	}
	public void setQmngshzid(String qmngshzid){
		this.qmngshzid = qmngshzid;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
