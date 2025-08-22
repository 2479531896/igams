package com.matridx.igams.wechat.dao.entities;

import java.util.List;

public class WeChatInspectionResistanceModel {

	//耐药家族-药品
	private String drug_class;
	//基因数目-读数
	private String gene_count;	
	//耐药基因-基因
	private String related_gene;	
	//涉及的耐药机制-机理；主要机制
	private String main_mechanism;	
	//涉及的参考物种-相关物种
	private String ref_species;
	//基因分型，需分单元格
	private String genes;
	//序列数，需分单元格
	private String reads;
	//起源种
	private String origin_species;
	//耐药基因具体汇报物种的taxid
	private List<String> report_taxids;
	//耐药基因具体汇报物种的名称
	private List<String> report_taxnames;
	//疑似耐药
	private String drug_name;
	public String getDrug_class() {
		return drug_class;
	}
	public void setDrug_class(String drug_class) {
		this.drug_class = drug_class;
	}
	public String getGene_count() {
		return gene_count;
	}
	public void setGene_count(String gene_count) {
		this.gene_count = gene_count;
	}
	public String getRelated_gene() {
		return related_gene;
	}
	public void setRelated_gene(String related_gene) {
		this.related_gene = related_gene;
	}
	public String getMain_mechanism() {
		return main_mechanism;
	}
	public void setMain_mechanism(String main_mechanism) {
		this.main_mechanism = main_mechanism;
	}
	public String getRef_species() {
		return ref_species;
	}
	public void setRef_species(String ref_species) {
		this.ref_species = ref_species;
	}
	public String getGenes() {
		return genes;
	}
	public void setGenes(String genes) {
		this.genes = genes;
	}
	public String getReads() {
		return reads;
	}
	public void setReads(String reads) {
		this.reads = reads;
	}
	public String getOrigin_species() {
		return origin_species;
	}
	public void setOrigin_species(String origin_species) {
		this.origin_species = origin_species;
	}
	public List<String> getReport_taxids() {
		return report_taxids;
	}
	public void setReport_taxids(List<String> report_taxids) {
		this.report_taxids = report_taxids;
	}
	public List<String> getReport_taxnames() {
		return report_taxnames;
	}
	public void setReport_taxnames(List<String> report_taxnames) {
		this.report_taxnames = report_taxnames;
	}

	public String getDrug_name() {
		return drug_name;
	}

	public void setDrug_name(String drug_name) {
		this.drug_name = drug_name;
	}
}
