package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias(value = "ZsxxDto")
public class ZsxxDto extends ZsxxModel {

	private static final long serialVersionUID = 1L;
	private String gram_stain;
	private String comment;
	private String name;
	private String last_update;
	private String origin_species;
	private String id;
	private List<String> mcs;

	public List<String> getMcs() {
		return mcs;
	}

	public void setMcs(List<String> mcs) {
		this.mcs = mcs;
	}

	public String getGram_stain() {
		return gram_stain;
	}

	public void setGram_stain(String gram_stain) {
		this.gram_stain = gram_stain;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLast_update() {
		return last_update;
	}

	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	public String getOrigin_species() {
		return origin_species;
	}

	public void setOrigin_species(String origin_species) {
		this.origin_species = origin_species;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
