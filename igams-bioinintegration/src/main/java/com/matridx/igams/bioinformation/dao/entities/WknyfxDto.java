package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WknyfxDto")
public class WknyfxDto extends  WknyfxModel{
    private String nyjy;
    private String bbh;
    private String nr;
    private String sfbg;

    private String nylb;
    private String ckwztaxid;
    private String nyjzu;
    private String nyjz;
    private String ckwz;
    private String topjydjcxx;
    private String topjy;
    private String drug_class;
    private String origin_species;
    private String related_gene;
    private String main_mechanism;
    private String ref_species;
    private String genes;
    private String reads;
    private String gene;
    private String genefamily;
    private String mechanism;
    private String tagrget;
    private String species;

    public String getNylb() {
        return nylb;
    }

    public void setNylb(String nylb) {
        this.nylb = nylb;
    }

    public String getCkwztaxid() {
        return ckwztaxid;
    }

    public void setCkwztaxid(String ckwztaxid) {
        this.ckwztaxid = ckwztaxid;
    }

    public String getNyjzu() {
        return nyjzu;
    }

    public void setNyjzu(String nyjzu) {
        this.nyjzu = nyjzu;
    }

    public String getNyjz() {
        return nyjz;
    }

    public void setNyjz(String nyjz) {
        this.nyjz = nyjz;
    }

    public String getCkwz() {
        return ckwz;
    }

    public void setCkwz(String ckwz) {
        this.ckwz = ckwz;
    }

    public String getTopjydjcxx() {
        return topjydjcxx;
    }

    public void setTopjydjcxx(String topjydjcxx) {
        this.topjydjcxx = topjydjcxx;
    }

    public String getTopjy() {
        return topjy;
    }

    public void setTopjy(String topjy) {
        this.topjy = topjy;
    }

    public String getDrug_class() {
        return drug_class;
    }

    public void setDrug_class(String drug_class) {
        this.drug_class = drug_class;
    }

    public String getOrigin_species() {
        return origin_species;
    }

    public void setOrigin_species(String origin_species) {
        this.origin_species = origin_species;
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

    public String getNyjy() {
        return nyjy;
    }

    public void setNyjy(String nyjy) {
        this.nyjy = nyjy;
    }

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getSfbg() {
        return sfbg;
    }

    public void setSfbg(String sfbg) {
        this.sfbg = sfbg;
    }

    private static final long serialVersionUID = 1L;

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getGenefamily() {
        return genefamily;
    }

    public void setGenefamily(String genefamily) {
        this.genefamily = genefamily;
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
