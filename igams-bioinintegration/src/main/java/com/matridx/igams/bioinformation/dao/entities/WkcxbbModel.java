package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "WkcxbbModel")
public class WkcxbbModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String jgid;
    private String wkbh;
    private String wkcxid;
    private String rawreads;
    private String totalreads;
    private String homo;
    private String bacteria;
    private String fungi;
    private String viruses;
    private String parasite;
    private String others;
    private String unmapped;
    private String spikein;
    private String spikeinrpm;
    private String bbh;
    private String spike;
    private String ryzs;
    private String rypw;
    private String sfzwqckj;

	private String spikejson;

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getTotalreads() {
		return totalreads;
	}

	public void setTotalreads(String totalreads) {
		this.totalreads = totalreads;
	}

	public String getViruses() {
		return viruses;
	}

	public void setViruses(String viruses) {
		this.viruses = viruses;
	}

	public String getUnmapped() {
		return unmapped;
	}

	public void setUnmapped(String unmapped) {
		this.unmapped = unmapped;
	}

	public String getSpikein() {
		return spikein;
	}

	public void setSpikein(String spikein) {
		this.spikein = spikein;
	}

	public String getSpikeinrpm() {
		return spikeinrpm;
	}

	public void setSpikeinrpm(String spikeinrpm) {
		this.spikeinrpm = spikeinrpm;
	}

	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getSfzwqckj() {
		return sfzwqckj;
	}

	public void setSfzwqckj(String sfzwqckj) {
		this.sfzwqckj = sfzwqckj;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getRawreads() {
		return rawreads;
	}

	public void setRawreads(String rawreads) {
		this.rawreads = rawreads;
	}

	public String getHomo() {
		return homo;
	}

	public void setHomo(String homo) {
		this.homo = homo;
	}

	public String getBacteria() {
		return bacteria;
	}

	public void setBacteria(String bacteria) {
		this.bacteria = bacteria;
	}

	public String getFungi() {
		return fungi;
	}

	public void setFungi(String fungi) {
		this.fungi = fungi;
	}

	public String getParasite() {
		return parasite;
	}

	public void setParasite(String parasite) {
		this.parasite = parasite;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getRyzs() {
		return ryzs;
	}

	public void setRyzs(String ryzs) {
		this.ryzs = ryzs;
	}

	public String getRypw() {
		return rypw;
	}

	public void setRypw(String rypw) {
		this.rypw = rypw;
	}

	public String getSpikejson() {
		return spikejson;
	}

	public void setSpikejson(String spikejson) {
		this.spikejson = spikejson;
	}
}
