package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WkdlfxDto")
public class WkdlfxDto extends  WkdlfxModel{
    private String wkbh;
    private String bbh;
    private String nr;
    private String sfbg;
    private String virulencefactor;
    private String category;
    private String gene;
    private String reads;
    private String species;

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

}
