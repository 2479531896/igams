package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Date;
import java.util.List;

@Alias(value="HzxxtDto")
public class HzxxtDto extends HzxxtModel{

	//中/英姓名
	private String partyName;
	//性别
	private String gender;
	//出生日期
	private String bornDay;
	//身份证号
	private String certNumber;
	//分子检测项目
	private String fzjcxm;
	//分子检测项目
	private String[] fzjcxms;
	//检测项目
	private String jcxm;
	//检测日期
	private String jcsj;
	//检查项目（多）
	private String[] jcxmmcs;
	//检测项目id(多)
	private String[] jcxmids;
	//检测项目名称
	private String jcxmmc;
	//检测项目id
	private String jcxmid;
	//验证码
	private String yzm;
	//发送时间
	private Date fssj;
	//附件ID复数
	private List<String> fjids;
	//证件类型名称
	private String zjlxmc;
	//预约检测日期
	private String yyjcrq;
	//采集时间
	private String cjsj;
	//分子检测id
	private String fzjcid;
	//微信id
	private String wxid;
	//是否默认
	private String sfmr;
	//采样点
	private String cyd;
	//标记
	private String flag;
	//实验时间
	private String sysj;
	//报告日期
	private String bgrq;
	//操作标记
	private String czbj;
	//支付金额
	private String amount;
	//商品名称
	private String subject;
	//付款标记
	private String fkbj;
	//开票标记
	private String kpbj;
	//发票路径
	private String imgurl;
	//发票pdf路径
	private String pdfurl;
	//支付订单
	private String orderno;
	//退款信息
	private String mes;
	//退款信息
	private String fkje;
	//实付金额
	private String sfje;
	//支付方式
	private String zffs;
	//平台
	private String pt;
	//检测类型
	private String jclx;

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getZffs() {
		return zffs;
	}

	public void setZffs(String zffs) {
		this.zffs = zffs;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getPdfurl() {
		return pdfurl;
	}

	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getKpbj() {
		return kpbj;
	}

	public void setKpbj(String kpbj) {
		this.kpbj = kpbj;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getFkbj() {
		return fkbj;
	}

	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}

	public String getCzbj() {
		return czbj;
	}

	public void setCzbj(String czbj) {
		this.czbj = czbj;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSysj() {
		return sysj;
	}

	public void setSysj(String sysj) {
		this.sysj = sysj;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCyd() {
		return cyd;
	}

	public void setCyd(String cyd) {
		this.cyd = cyd;
	}

	public String getSfmr() {
		return sfmr;
	}

	public void setSfmr(String sfmr) {
		this.sfmr = sfmr;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public Date getFssj() {
		return fssj;
	}

	public void setFssj(Date fssj) {
		this.fssj = fssj;
	}

	public String getYyjcrq() {
		return yyjcrq;
	}

	public void setYyjcrq(String yyjcrq) {
		this.yyjcrq = yyjcrq;
	}

	public String getZjlxmc() {
		return zjlxmc;
	}

	public void setZjlxmc(String zjlxmc) {
		this.zjlxmc = zjlxmc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String[] getJcxmids() {
		return jcxmids;
	}

	public void setJcxmids(String[] jcxmids) {
		this.jcxmids = jcxmids;
		for(int i=0;i<this.jcxmids.length;i++)
		{
			this.jcxmids[i] = this.jcxmids[i].replace("'", "");
		}
	}

	public String[] getJcxmmcs() {
		return jcxmmcs;
	}

	public void setJcxmmcs(String[] jcxms) {
		this.jcxmmcs = jcxms;
		for(int i=0;i<this.jcxmmcs.length;i++)
		{
			this.jcxmmcs[i] = this.jcxmmcs[i].replace("'", "");
		}
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String getJcsj() {
		return jcsj;
	}

	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}

	public String getFzjcxm() {
		return fzjcxm;
	}

	public void setFzjcxm(String fzjcxm) {
		this.fzjcxm = fzjcxm;
	}

	public String[] getFzjcxms() {
		return fzjcxms;
	}

	public void setFzjcxms(String[] fzjcxms) {
		this.fzjcxms = fzjcxms;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBornDay() {
		return bornDay;
	}

	public void setBornDay(String bornDay) {
		this.bornDay = bornDay;
	}

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
