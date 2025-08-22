package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjjcjgDto")
public class SjjcjgDto extends SjjcjgModel{
	private static final long serialVersionUID = 1L;
	private String jcjgmc;//检测结果名称
	private String jlmc;//结论名称
	private String jclxmc;//检测类型名称
	private String kw;//孔位
	private String ckbl;//参考比例
	private String ckbl2;//参考比例2
	//钉钉标记
	private String ddbj;
	private String nbbm;
	private String info;
	private String bbh;
	//区分不同的审核页面
	private String xsbj;
	//状态
	private String zt;
	//结论代码
	private String jldm;
	//检测结果代码
	private String jcjgdm;
	private String id;
	private String author;
	private String title;
	private String journal;
	private String species_taxid;
	private String sample_type;
	private String mrwx;
	private String yblx;
	private List<String> strings;
	//复数ID
	private List<String> ids;
	//样式标记
	private String styleFlag;
	//检测结果参数扩展4
	private String jcjgcskz4;

	public String getJcjgcskz4() {
		return jcjgcskz4;
	}

	public void setJcjgcskz4(String jcjgcskz4) {
		this.jcjgcskz4 = jcjgcskz4;
	}

	public String getStyleFlag() {
		return styleFlag;
	}

	public void setStyleFlag(String styleFlag) {
		this.styleFlag = styleFlag;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getCkbl2() {
		return ckbl2;
	}

	public void setCkbl2(String ckbl2) {
		this.ckbl2 = ckbl2;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getCkbl() {
		return ckbl;
	}

	public void setCkbl(String ckbl) {
		this.ckbl = ckbl;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getSpecies_taxid() {
		return species_taxid;
	}

	public void setSpecies_taxid(String species_taxid) {
		this.species_taxid = species_taxid;
	}

	public String getSample_type() {
		return sample_type;
	}

	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}

	public String getMrwx() {
		return mrwx;
	}

	public void setMrwx(String mrwx) {
		this.mrwx = mrwx;
	}

	public String getJcjgdm() {
		return jcjgdm;
	}

	public void setJcjgdm(String jcjgdm) {
		this.jcjgdm = jcjgdm;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public String getJlmc() {
		return jlmc;
	}

	public void setJlmc(String jlmc) {
		this.jlmc = jlmc;
	}

	public String getJclxmc() {
		return jclxmc;
	}

	public void setJclxmc(String jclxmc) {
		this.jclxmc = jclxmc;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getJldm() {
		return jldm;
	}

	public void setJldm(String jldm) {
		this.jldm = jldm;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getXsbj() {
		return xsbj;
	}

	public void setXsbj(String xsbj) {
		this.xsbj = xsbj;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}
}
