package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="HzxxDto")
public class HzxxDto extends HzxxModel{

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
	//检测结果
	private String jcjg;
	//检测单位
	private String jcdw;
	//检测时间
	private String sysj;
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
	private String fssj;
	//附件ID复数
	private List<String> fjids;
	//证件类型名称
	private String zjlxmc;
	//预约检测日期
	private String yyjcrq;
	//预约检测日期取日期
	private String yyjc;
	//采集时间
	private String cjsj;
	//分子检测id
	private String fzjcid;
	//微信ID
	private String wxid;
	//省份名称
	private String sfmc;
	//现居住地，用来拼接详细地址
	private String xjzd;
   //省份ID，用来获取对应的城市
   private String sfid;
   	//城市名称
	private String csmc;
	//采样点
	private String cyd;
	//送检单位
	private String sjdw;
	//其他单位,当送检单位选择其他的时候，填写其他单位到送检单位名称
	private String sjdwmc;
	//标本类型
	private String yblx;
	//样品名称
	private String ypmc;
	//生产厂家
	private String sccj;
	//生产批次
	private String scpc;
	//生产地
	private String scd;
	//检测对象类型
	private String jcdxlx;
	//生产日期
	private String scrq;
	//样本类型名称，对应物标检测的样品类型，其他标本类型
	private String yblxmc;
	//样本编号
	private String ybbh;
	//备注
	private String bz;
	//钉钉查询主条码标记
	private String ddbj;
	//分子检测子项目ID，存检测类型是单检还是混检
	private String fzjczxmid;

	//检测类型
	private String jclx;

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getYyjc() {
		return yyjc;
	}

	public void setYyjc(String yyjc) {
		this.yyjc = yyjc;
	}

	public String getFzjczxmid() {
		return fzjczxmid;
	}

	public void setFzjczxmid(String fzjczxmid) {
		this.fzjczxmid = fzjczxmid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getJcdxlx() {
		return jcdxlx;
	}

	public void setJcdxlx(String jcdxlx) {
		this.jcdxlx = jcdxlx;
	}

	public String getYpmc() {
		return ypmc;
	}

	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}

	public String getSccj() {
		return sccj;
	}

	public void setSccj(String sccj) {
		this.sccj = sccj;
	}

	public String getScpc() {
		return scpc;
	}

	public void setScpc(String scpc) {
		this.scpc = scpc;
	}

	public String getScd() {
		return scd;
	}

	public void setScd(String scd) {
		this.scd = scd;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getSfmc() {
		return sfmc;
	}

	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}
	public String getCyd() {
		return cyd;
	}

	public void setCyd(String cyd) {
		this.cyd = cyd;
	}
	public String getSfid() {
		return sfid;
	}

	public void setSfid(String sfid) {
		this.sfid = sfid;
	}

	public String getXjzd() {
		return xjzd;
	}

	public void setXjzd(String xjzd) {
		this.xjzd = xjzd;
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

	public String getZjlxmc() {
		return zjlxmc;
	}

	public void setZjlxmc(String zjlxmc) {
		this.zjlxmc = zjlxmc;
	}


//证件类型参数id
	private String zjcsid;

	public String getZjcsid() {
		return zjcsid;
	}

	public void setZjcsid(String zjcsid) {
		this.zjcsid = zjcsid;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getSysj() {
		return sysj;
	}

	public void setSysj(String sysj) {
		this.sysj = sysj;
	}
		public String getYyjcrq() {
		return yyjcrq;
	}

	public void setYyjcrq(String yyjcrq) {
		this.yyjcrq = yyjcrq;
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

	public String getFssj() {
		return fssj;
	}

	public void setFssj(String fssj) {
		this.fssj = fssj;
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

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
