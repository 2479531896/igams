package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="HbphzxxModel")
public class HbphzxxModel extends BaseModel{
	//HBP患者ID
	private String hbphzxxid;
	//姓名
	private String xm;
	//性别
	private String xb;
	//年龄
	private String nl;
	//身高
	private String sg;
	//体重
	private String tz;
	//入ICU主要诊断1
	private String ricuzyzd1;
	//入ICU主要诊断2
	private String ricuzyzd2;
	//入ICU主要诊断3
	private String ricuzyzd3;
	//入ICU主要诊断3
	private String ricuzyzd4;
	//既往病史
	private String jwbs;
	//其他既往病史
	private String qtjwbs;
	//APACHE II评分
	private String apache;
	//SOFA评分
	private String sofa;
	//GCS评分
	private String gcs;
	//入ICU日期
	private String ricurq;
	//住院号
	private String zyh;
	//备注
	private String bz;
	//因感染需要入住ICU
	private String sfygrxyrzicu;
	//是否成年
	private String sfcn;
	//是否只取第一次数据
	private String sfzqdycsj;
	//预计24小时内死亡患者
	private String sfyj24sw;
	//拒绝该项检查的患者
	private String sfjjjc;
	//是否机械通气
	private String sfjxtq;
	//机械通气开始日期
	private String jxtqksrq;
	//机械通气结束日期
	private String jxtqjsrq;
	//感染部位
	private String grbw;
	//其他感染部位
	private String qtgrbw;
	//是否脓毒性休克
	private String sfndxxk;
	//脓毒性休克日期
	private String ndxxkrq;
	//致病菌学名1
	private String zbjxm1;
	//感染致病菌日期1
	private String grzbjrq1;
	//确诊标本1
	private String qzbb1;
	//其他确诊标本1
	private String qtqzbb1;
	//致病菌学名2
	private String zbjxm2;
	//感染致病菌日期2
	private String grzbjrq2;
	//确诊标本2
	private String qzbb2;
	//其他确诊标本2
	private String qtqzbb2;
	//初始抗生素方案
	private String cskssfa;
	//初始抗生素日期
	private String cskssrq;
	//是否升级抗生素1
	private String sfsjkss1;
	//更改后抗生素方案1
	private String gghkssfa1;
	//更改抗生素日期1
	private String ggkssrq1;
	//是否升级抗生素2
	private String sfsjkss2;
	//更改后抗生素方案2
	private String gghkssfa2;
	//更改抗生素日期2
	private String ggkssrq2;
	//出ICU日期
	private String cicurq;
	//出ICU去向
	private String cicuqx;
	//其他出ICU去向
	private String qtcicuqx;
	//是否院内死亡
	private String sfynsw;
	//院内死亡日期
	private String ynswrq;
	//是否28天内死亡
	private String sfsw;
	//28天内死亡日期
	private String swrq;
	//入组日期
	private String rzrq;

	public String getRzrq() {
		return rzrq;
	}

	public void setRzrq(String rzrq) {
		this.rzrq = rzrq;
	}

	public String getCicurq() {
		return cicurq;
	}

	public void setCicurq(String cicurq) {
		this.cicurq = cicurq;
	}

	public String getCicuqx() {
		return cicuqx;
	}

	public void setCicuqx(String cicuqx) {
		this.cicuqx = cicuqx;
	}

	public String getQtcicuqx() {
		return qtcicuqx;
	}

	public void setQtcicuqx(String qtcicuqx) {
		this.qtcicuqx = qtcicuqx;
	}

	public String getSfynsw() {
		return sfynsw;
	}

	public void setSfynsw(String sfynsw) {
		this.sfynsw = sfynsw;
	}

	public String getYnswrq() {
		return ynswrq;
	}

	public void setYnswrq(String ynswrq) {
		this.ynswrq = ynswrq;
	}

	public String getSfsw() {
		return sfsw;
	}

	public void setSfsw(String sfsw) {
		this.sfsw = sfsw;
	}

	public String getSwrq() {
		return swrq;
	}

	public void setSwrq(String swrq) {
		this.swrq = swrq;
	}

	//HBP患者ID
	public String getHbphzxxid() { return hbphzxxid; }
	public void setHbphzxxid(String hbphzxxid) { this.hbphzxxid = hbphzxxid; }
	//姓名
	public String getXm() {
		return xm;
	}
	public void setXm(String xm){
		this.xm = xm;
	}
	//性别
	public String getXb() {
		return xb;
	}
	public void setXb(String xb){
		this.xb = xb;
	}
	//年龄
	public String getNl() {
		return nl;
	}
	public void setNl(String nl){
		this.nl = nl;
	}
	//身高
	public String getSg() {
		return sg;
	}
	public void setSg(String sg){
		this.sg = sg;
	}
	//体重
	public String getTz() {
		return tz;
	}
	public void setTz(String tz){
		this.tz = tz;
	}
	//入ICU主要诊断1
	public String getRicuzyzd1() {
		return ricuzyzd1;
	}
	public void setRicuzyzd1(String ricuzyzd1){
		this.ricuzyzd1 = ricuzyzd1;
	}
	//入ICU主要诊断2
	public String getRicuzyzd2() {
		return ricuzyzd2;
	}
	public void setRicuzyzd2(String ricuzyzd2){
		this.ricuzyzd2 = ricuzyzd2;
	}
	//入ICU主要诊断3
	public String getRicuzyzd3() {
		return ricuzyzd3;
	}
	public void setRicuzyzd3(String ricuzyzd3){
		this.ricuzyzd3 = ricuzyzd3;
	}
	//入ICU主要诊断3
	public String getRicuzyzd4() {
		return ricuzyzd4;
	}
	public void setRicuzyzd4(String ricuzyzd4){
		this.ricuzyzd4 = ricuzyzd4;
	}
	//既往病史
	public String getJwbs() {
		return jwbs;
	}
	public void setJwbs(String jwbs){
		this.jwbs = jwbs;
	}
	//其他既往病史
	public String getQtjwbs() {
		return qtjwbs;
	}
	public void setQtjwbs(String qtjwbs){
		this.qtjwbs = qtjwbs;
	}
	//APACHE II评分
	public String getApache() {
		return apache;
	}
	public void setApache(String apache){
		this.apache = apache;
	}
	//SOFA评分
	public String getSofa() {
		return sofa;
	}
	public void setSofa(String sofa){
		this.sofa = sofa;
	}
	//GCS评分
	public String getGcs() {
		return gcs;
	}
	public void setGcs(String gcs){
		this.gcs = gcs;
	}
	//入ICU日期
	public String getRicurq() {
		return ricurq;
	}
	public void setRicurq(String ricurq){
		this.ricurq = ricurq;
	}
	//住院号
	public String getZyh() {
		return zyh;
	}
	public void setZyh(String zyh){
		this.zyh = zyh;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	//因感染需要入住ICU
	public String getSfygrxyrzicu() {
		return sfygrxyrzicu;
	}
	public void setSfygrxyrzicu(String sfygrxyrzicu){
		this.sfygrxyrzicu = sfygrxyrzicu;
	}
	//是否成年
	public String getSfcn() {
		return sfcn;
	}
	public void setSfcn(String sfcn){
		this.sfcn = sfcn;
	}
	//是否只取第一次数据
	public String getSfzqdycsj() {
		return sfzqdycsj;
	}
	public void setSfzqdycsj(String sfzqdycsj){
		this.sfzqdycsj = sfzqdycsj;
	}
	//预计24小时内死亡患者
	public String getSfyj24sw() {
		return sfyj24sw;
	}
	public void setSfyj24sw(String sfyj24sw){
		this.sfyj24sw = sfyj24sw;
	}
	//拒绝该项检查的患者
	public String getSfjjjc() {
		return sfjjjc;
	}
	public void setSfjjjc(String sfjjjc){
		this.sfjjjc = sfjjjc;
	}
	//是否机械通气
	public String getSfjxtq() {
		return sfjxtq;
	}
	public void setSfjxtq(String sfjxtq){
		this.sfjxtq = sfjxtq;
	}
	//机械通气开始日期
	public String getJxtqksrq() {
		return jxtqksrq;
	}
	public void setJxtqksrq(String jxtqksrq){
		this.jxtqksrq = jxtqksrq;
	}
	//机械通气结束日期
	public String getJxtqjsrq() {
		return jxtqjsrq;
	}
	public void setJxtqjsrq(String jxtqjsrq){
		this.jxtqjsrq = jxtqjsrq;
	}
	//感染部位
	public String getGrbw() {
		return grbw;
	}
	public void setGrbw(String grbw){
		this.grbw = grbw;
	}
	//其他感染部位
	public String getQtgrbw() {
		return qtgrbw;
	}
	public void setQtgrbw(String qtgrbw){
		this.qtgrbw = qtgrbw;
	}
	//是否脓毒性休克
	public String getSfndxxk() {
		return sfndxxk;
	}
	public void setSfndxxk(String sfndxxk){
		this.sfndxxk = sfndxxk;
	}
	//脓毒性休克日期
	public String getNdxxkrq() {
		return ndxxkrq;
	}
	public void setNdxxkrq(String ndxxkrq){
		this.ndxxkrq = ndxxkrq;
	}
	//致病菌学名1
	public String getZbjxm1() {
		return zbjxm1;
	}
	public void setZbjxm1(String zbjxm1){
		this.zbjxm1 = zbjxm1;
	}
	//感染致病菌日期1
	public String getGrzbjrq1() {
		return grzbjrq1;
	}
	public void setGrzbjrq1(String grzbjrq1){
		this.grzbjrq1 = grzbjrq1;
	}
	//确诊标本1
	public String getQzbb1() {
		return qzbb1;
	}
	public void setQzbb1(String qzbb1){
		this.qzbb1 = qzbb1;
	}
	//其他确诊标本1
	public String getQtqzbb1() {
		return qtqzbb1;
	}
	public void setQtqzbb1(String qtqzbb1){
		this.qtqzbb1 = qtqzbb1;
	}
	//致病菌学名2
	public String getZbjxm2() {
		return zbjxm2;
	}
	public void setZbjxm2(String zbjxm2){
		this.zbjxm2 = zbjxm2;
	}
	//感染致病菌日期2
	public String getGrzbjrq2() {
		return grzbjrq2;
	}
	public void setGrzbjrq2(String grzbjrq2){
		this.grzbjrq2 = grzbjrq2;
	}
	//确诊标本2
	public String getQzbb2() {
		return qzbb2;
	}
	public void setQzbb2(String qzbb2){
		this.qzbb2 = qzbb2;
	}
	//其他确诊标本2
	public String getQtqzbb2() {
		return qtqzbb2;
	}
	public void setQtqzbb2(String qtqzbb2){
		this.qtqzbb2 = qtqzbb2;
	}
	//初始抗生素方案
	public String getCskssfa() {
		return cskssfa;
	}
	public void setCskssfa(String cskssfa){
		this.cskssfa = cskssfa;
	}
	//初始抗生素日期
	public String getCskssrq() {
		return cskssrq;
	}
	public void setCskssrq(String cskssrq){
		this.cskssrq = cskssrq;
	}
	//是否升级抗生素1
	public String getSfsjkss1() {
		return sfsjkss1;
	}
	public void setSfsjkss1(String sfsjkss1){
		this.sfsjkss1 = sfsjkss1;
	}
	//更改后抗生素方案1
	public String getGghkssfa1() {
		return gghkssfa1;
	}
	public void setGghkssfa1(String gghkssfa1){
		this.gghkssfa1 = gghkssfa1;
	}
	//更改抗生素日期1
	public String getGgkssrq1() {
		return ggkssrq1;
	}
	public void setGgkssrq1(String ggkssrq1){
		this.ggkssrq1 = ggkssrq1;
	}
	//是否升级抗生素2
	public String getSfsjkss2() {
		return sfsjkss2;
	}
	public void setSfsjkss2(String sfsjkss2){
		this.sfsjkss2 = sfsjkss2;
	}
	//更改后抗生素方案2
	public String getGghkssfa2() {
		return gghkssfa2;
	}
	public void setGghkssfa2(String gghkssfa2){
		this.gghkssfa2 = gghkssfa2;
	}
	//更改抗生素日期2
	public String getGgkssrq2() {
		return ggkssrq2;
	}
	public void setGgkssrq2(String ggkssrq2){
		this.ggkssrq2 = ggkssrq2;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
