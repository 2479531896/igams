package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias("BioDlyzzsDto")
public class BioDlyzzsDto extends BioDlyzzsModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1692209522751835915L;
	private String wzzwm;
    private String gene;
    private String vf_name;
    private String species_name;
    private String vf_category;
    private String dlfxid;

    public String getDlfxid() {
        return dlfxid;
    }

    public void setDlfxid(String dlfxid) {
        this.dlfxid = dlfxid;
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

    public String getWzzwm() {
        return wzzwm;
    }

    public void setWzzwm(String wzzwm) {
        this.wzzwm = wzzwm;
    }
}
