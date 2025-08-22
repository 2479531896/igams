package com.matridx.igams.wechat.dao.entities;

public class WechatReferencesModel {

	//参考文献id
	private String id;
	//关联的物种id
	private String species_taxid;
	//作者
	private String author;
	//标题
	private String title;
	//期刊
	private String journal;
	//标本类型
	private String sample_type;
	//引用序号
	private String yyxh;
	//重新排序(引用序号)
	private String cxpx;

	public String getCxpx() {
		return cxpx;
	}

	public void setCxpx(String cxpx) {
		this.cxpx = cxpx;
	}

	public String getYyxh() {
		return yyxh;
	}

	public void setYyxh(String yyxh) {
		this.yyxh = yyxh;
	}

	public String getSample_type() {
		return sample_type;
	}
	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpecies_taxid() {
		return species_taxid;
	}
	public void setSpecies_taxid(String species_taxid) {
		this.species_taxid = species_taxid;
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
	
}
