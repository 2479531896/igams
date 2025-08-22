package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FzjcjgDto")
public class FzjcjgDto extends FzjcjgModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//结果list
	private List<FzjcjgDto> jglist;
	//检测结果名称
	private String jcjgmc;
	//结果Json
	private String jcjg_json;
	//检测项目名称
	//private String jcxmmc;

	//结果名称
	private String jgmc;
	//分子检测项目名称
	private String fzjcxmmc;
	//分子检测子项目名称
	private String fzjczxmmc;
	//检测子项目ID
	private String jczxmid;
	//检测结果名称s
	private List<FzjcxxDto> fzjcxxmcs;
	//实验号
	private String syh;
	//检测项目名称
	private String jcxmmc;
	//检测子项目名称
	private String jczxmmc;
	//上传状态
	private String sczt;
	//结果代码
	private String jgdm;
	//通道
	private String td;
	//附件ID复数
	private List<String> fjids;
	//质控结果数据
	private String zkjg_json;
	//标准CT值[多]
	private List<String> bzctzs;

	public List<String> getBzctzs() {
		return bzctzs;
	}

	public void setBzctzs(List<String> bzctzs) {
		this.bzctzs = bzctzs;
	}

	public String getZkjg_json() {
		return zkjg_json;
	}

	public void setZkjg_json(String zkjg_json) {
		this.zkjg_json = zkjg_json;
	}

	public String getSyh() {
		return syh;
	}

	public void setSyh(String syh) {
		this.syh = syh;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getTd() {
		return td;
	}

	public void setTd(String td) {
		this.td = td;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getSczt() {
		return sczt;
	}

	public void setSczt(String sczt) {
		this.sczt = sczt;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public List<FzjcxxDto> getFzjcxxmcs() {
		return fzjcxxmcs;
	}

	public void setFzjcxxmcs(List<FzjcxxDto> fzjcxxmcs) {
		this.fzjcxxmcs = fzjcxxmcs;
	}

	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	public String getFzjcxmmc() {
		return fzjcxmmc;
	}

	public void setFzjcxmmc(String fzjcxmmc) {
		this.fzjcxmmc = fzjcxmmc;
	}

	public String getFzjczxmmc() {
		return fzjczxmmc;
	}

	public void setFzjczxmmc(String fzjczxmmc) {
		this.fzjczxmmc = fzjczxmmc;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getJcjg_json() {
		return jcjg_json;
	}

	public void setJcjg_json(String jcjg_json) {
		this.jcjg_json = jcjg_json;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public List<FzjcjgDto> getJglist() {
		return jglist;
	}

	public void setJglist(List<FzjcjgDto> jglist) {
		this.jglist = jglist;
	}

}
