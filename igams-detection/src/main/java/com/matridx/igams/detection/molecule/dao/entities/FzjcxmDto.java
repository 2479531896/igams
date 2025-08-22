package com.matridx.igams.detection.molecule.dao.entities;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FzjcxmDto")
public class FzjcxmDto extends FzjcxmModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//项目名称
	private String xmmc;
	//检测结果
	private String jcjg;
	//项目简称
	private String xmjc;
	//检测项目名称
	private String jcxmmc;
	//检测子项目名称
	private String jczxmmc;
	//检测子项目List
	private List<FzjcxmDto> fzjczxmList;
	//页面判断标记
	private String pdbj;
	//检测结果名称
	private String jcjgmc;
	//结果id
	private String jgid;
	//姓名
	private String xm;
	//检测结果list
	private List<FzjcjgDto> fzjcjgDtos;
	//标本子编号
	private String bbzbh;

	//rownum编号
	private String rownum;

	//普检结果名称
	private String pjjgmc;
	//普检结果
	private String pjjg;
	private List<String> jcxms;
	//父参数id
	private String fcsid;
	//附件word
	List<FjcfbDto> wordList;
	//附件pdf
	List<FjcfbDto> pdfList;
	//审核人员
	private String shry;
	//检测对象参数代码
	private String jcdxcsdm;
	//检测类型代码
	private String jclxdm;
	//分子检测子项目s
	private String[] fzjczxmids;
	private String syry;
	//实验人员名称
	private String syrymc;
	//标记，用于实验列表的确认，未确认，未发送报告按钮的区分
	private String flag;
	//送检单位名称
	private String sjdwmc;
	//样本类型名称
	private String yblxmc;
	//性别
	private String xb;
	//采集日期
	private String cjsj;
	//科室名称
	private String ksmc;
	//标本状态
	private String bbzt;
	//年龄
	private String nl;
	//主治医师
	private String sjys;
	//床号
	private String ch;
	//住院号
	private String zyh;
	//备注
	private String bz;
	//分子检测信息的报告日期
	private String zbgrq;
	//ct值
	private String ctz;
	//报告完成数
	private String bgwcs;
	//单位名称
	private String dwmc;
	//标本状态名称
	private String bbztmc;
	//检测单位名称
	private String jcdwmc;
	//接收时间
	private String jssj;
	//实验时间
	private String sysj;
	//项目数
	private String xms;

	public String getXms() {
		return xms;
	}

	public void setXms(String xms) {
		this.xms = xms;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getSysj() {
		return sysj;
	}

	public void setSysj(String sysj) {
		this.sysj = sysj;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getBbztmc() {
		return bbztmc;
	}

	public void setBbztmc(String bbztmc) {
		this.bbztmc = bbztmc;
	}

	public String getBgwcs() {
		return bgwcs;
	}

	public void setBgwcs(String bgwcs) {
		this.bgwcs = bgwcs;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}

	public String getZbgrq() {
		return zbgrq;
	}

	public void setZbgrq(String zbgrq) {
		this.zbgrq = zbgrq;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getBbzt() {
		return bbzt;
	}

	public void setBbzt(String bbzt) {
		this.bbzt = bbzt;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getSjys() {
		return sjys;
	}

	public void setSjys(String sjys) {
		this.sjys = sjys;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSyry() {
		return syry;
	}

	public void setSyry(String syry) {
		this.syry = syry;
	}

	public String getSyrymc() {
		return syrymc;
	}

	public void setSyrymc(String syrymc) {
		this.syrymc = syrymc;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String[] getFzjczxmids() {
		return fzjczxmids;
	}

	public void setFzjczxmids(String[] fzjczxmids) {
		this.fzjczxmids = fzjczxmids;
	}

	public String getJclxdm() {
		return jclxdm;
	}

	public void setJclxdm(String jclxdm) {
		this.jclxdm = jclxdm;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getJcdxcsdm() {
		return jcdxcsdm;
	}

	public void setJcdxcsdm(String jcdxcsdm) {
		this.jcdxcsdm = jcdxcsdm;
	}

	public List<FjcfbDto> getWordList() {
		return wordList;
	}

	public void setWordList(List<FjcfbDto> wordList) {
		this.wordList = wordList;
	}

	public List<FjcfbDto> getPdfList() {
		return pdfList;
	}

	public void setPdfList(List<FjcfbDto> pdfList) {
		this.pdfList = pdfList;
	}

	public String getFcsid() {
		return fcsid;
	}

	public void setFcsid(String fcsid) {
		this.fcsid = fcsid;
	}

	public List<String> getJcxms() {
		return jcxms;
	}

	public void setJcxms(List<String> jcxms) {
		this.jcxms = jcxms;
	}
	public String getPjjg() {
		return pjjg;
	}

	public void setPjjg(String pjjg) {
		this.pjjg = pjjg;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	//样本编号
	private String ybbh;

	public String getPjjgmc() {
		return pjjgmc;
	}

	public void setPjjgmc(String pjjgmc) {
		this.pjjgmc = pjjgmc;
	}

	public String getRownum() {
		return rownum;
	}

	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

	public String getBbzbh() {
		return bbzbh;
	}

	public void setBbzbh(String bbzbh) {
		this.bbzbh = bbzbh;
	}

	public List<FzjcjgDto> getFzjcjgDtos() {
		return fzjcjgDtos;
	}

	public void setFzjcjgDtos(List<FzjcjgDto> fzjcjgDtos) {
		this.fzjcjgDtos = fzjcjgDtos;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public String getPdbj() {
		return pdbj;
	}

	public void setPdbj(String pdbj) {
		this.pdbj = pdbj;
	}

	public List<FzjcxmDto> getFzjczxmList() {
		return fzjczxmList;
	}

	public void setFzjczxmList(List<FzjcxmDto> fzjczxmList) {
		this.fzjczxmList = fzjczxmList;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getJcjg() {
		return jcjg;
	}
	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}
	public String getXmjc() {
		return xmjc;
	}

	public void setXmjc(String xmjc) {
		this.xmjc = xmjc;
	}
	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}
	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

}
