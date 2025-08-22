package com.matridx.igams.wechat.dao.entities;

public class WechatVirulenceFactorStatModel {

	//基因
	private String gene;
	//毒力因子名称
	private String vf_name;
	//描述
	private String comment;
	//种名称
	private String species_name;
	//毒力因子类型
	private String vf_category;
	//原始毒力因子
	private String virulence_factor;
	//检出的序列数
	private String reads_count;
	//vfdb编号
	private String vf_id;
	//可能来源物种
	private String related_species;
	//关联物种ID
	private String species_taxid;

	public String getVirulence_factor() {
		return virulence_factor;
	}

	public void setVirulence_factor(String virulence_factor) {
		this.virulence_factor = virulence_factor;
	}

	public String getReads_count() {
		return reads_count;
	}

	public void setReads_count(String reads_count) {
		this.reads_count = reads_count;
	}

	public String getVf_id() {
		return vf_id;
	}

	public void setVf_id(String vf_id) {
		this.vf_id = vf_id;
	}

	public String getRelated_species() {
		return related_species;
	}

	public void setRelated_species(String related_species) {
		this.related_species = related_species;
	}

	public String getSpecies_taxid() {
		return species_taxid;
	}

	public void setSpecies_taxid(String species_taxid) {
		this.species_taxid = species_taxid;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getVf_name() {
		return vf_name;
	}

	public void setVf_name(String vf_name) {
		this.vf_name = vf_name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSpecies_name() {
		return species_name;
	}

	public void setSpecies_name(String species_name) {
		this.species_name = species_name;
	}

	public String getVf_category() {
		return vf_category;
	}

	public void setVf_category(String vf_category) {
		this.vf_category = vf_category;
	}
}
