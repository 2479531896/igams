package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "NyfxjgDto")
public class NyfxjgDto extends NyfxjgModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
	private String nyjzu;//耐药家族
	private String nylb;//耐药类别
    private String genefamily;
    private String gene;
    private String reads;
    private String mechanism;
    private String tagrget;
    private String species;
	private String zdnyjy;
	private String topjydjcxx;
	private String ckwztaxid;
	private String nyjz;
	private String topjy;
	private String gram_stain;
	private String comment;
	private String name;
	private String last_update;
	private String origin_species;
	private String id;
	private String drug_class;
	private String related_gene;
	private String main_mechanism;
	private String ref_species;
	private String genes;
	private String ckwz;
	private String taxid;
	private String glsrst;
	private String knwzly;

	public String getKnwzly() {
		return knwzly;
	}

	public void setKnwzly(String knwzly) {
		this.knwzly = knwzly;
	}

	public String getGlsrst() {
		return glsrst;
	}

	public void setGlsrst(String glsrst) {
		this.glsrst = glsrst;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getCkwz() {
		return ckwz;
	}

	public void setCkwz(String ckwz) {
		this.ckwz = ckwz;
	}

	public String getDrug_class() {
		return drug_class;
	}

	public void setDrug_class(String drug_class) {
		this.drug_class = drug_class;
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

	public String getZdnyjy() {
		return zdnyjy;
	}

	public void setZdnyjy(String zdnyjy) {
		this.zdnyjy = zdnyjy;
	}

	public String getTopjydjcxx() {
		return topjydjcxx;
	}

	public void setTopjydjcxx(String topjydjcxx) {
		this.topjydjcxx = topjydjcxx;
	}

	public String getCkwztaxid() {
		return ckwztaxid;
	}

	public void setCkwztaxid(String ckwztaxid) {
		this.ckwztaxid = ckwztaxid;
	}

	public String getNyjz() {
		return nyjz;
	}

	public void setNyjz(String nyjz) {
		this.nyjz = nyjz;
	}

	public String getTopjy() {
		return topjy;
	}

	public void setTopjy(String topjy) {
		this.topjy = topjy;
	}

	public String getNylb() {
		return nylb;
	}

	public void setNylb(String nylb) {
		this.nylb = nylb;
	}

	public String getNyjzu() {
		return nyjzu;
	}

	public void setNyjzu(String nyjzu) {
		this.nyjzu = nyjzu;
	}
	public String getGenefamily() {
		return genefamily;
	}

	public void setGenefamily(String genefamily) {
		this.genefamily = genefamily;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

	public String getMechanism() {
		return mechanism;
	}

	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}

	public String getTagrget() {
		return tagrget;
	}

	public void setTagrget(String tagrget) {
		this.tagrget = tagrget;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}
}
