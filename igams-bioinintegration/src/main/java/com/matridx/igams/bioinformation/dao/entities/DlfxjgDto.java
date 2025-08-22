package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "DlfxjgDto")
public class DlfxjgDto extends DlfxjgModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String virulencefactor;
    private String category;
    private String gene;
    private String reads;
    private String species;
    private String dbreads;
	private String dljy;
	private String vfdb;
	private String dlyz;
	private String dlxglb;
	private String vfid;
	private String taxid;

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getVfid() {
		return vfid;
	}

	public void setVfid(String vfid) {
		this.vfid = vfid;
	}

	public String getDbreads() {
		return dbreads;
	}

	public void setDbreads(String dbreads) {
		this.dbreads = dbreads;
	}

	public String getDljy() {
		return dljy;
	}

	public void setDljy(String dljy) {
		this.dljy = dljy;
	}

	public String getVfdb() {
		return vfdb;
	}

	public void setVfdb(String vfdb) {
		this.vfdb = vfdb;
	}

	public String getDlyz() {
		return dlyz;
	}

	public void setDlyz(String dlyz) {
		this.dlyz = dlyz;
	}

	public String getDlxglb() {
		return dlxglb;
	}

	public void setDlxglb(String dlxglb) {
		this.dlxglb = dlxglb;
	}

	public String getVirulencefactor() {
		return virulencefactor;
	}

	public void setVirulencefactor(String virulencefactor) {
		this.virulencefactor = virulencefactor;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}
}
