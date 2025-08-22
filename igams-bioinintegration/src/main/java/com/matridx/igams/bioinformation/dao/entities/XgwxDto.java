package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value = "XgwxDto")
public class XgwxDto extends XgwxModel {
	private String title;
	private String journal;
	private String author;
	private String species_taxid;
	private String sample_type;
	private String id;
	private String wkbh;
	private String bbh;

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getSample_type() {
		return sample_type;
	}

	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSpecies_taxid() {
		return species_taxid;
	}

	public void setSpecies_taxid(String species_taxid) {
		this.species_taxid = species_taxid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

			;

	private static final long serialVersionUID = 1L;


}
