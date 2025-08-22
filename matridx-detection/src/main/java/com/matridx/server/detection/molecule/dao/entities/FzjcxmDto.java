package com.matridx.server.detection.molecule.dao.entities;

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
	//ct值
	private String ctz;

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}

	//附件word
	List<FjcfbDto> wordList;
	//附件pdf
	List<FjcfbDto> pdfList;

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
