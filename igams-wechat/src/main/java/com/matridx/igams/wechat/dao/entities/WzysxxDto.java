package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WzysxxDto")
public class WzysxxDto extends WzysxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String MappingReads;

	private String reads;

	private String refANI;

	private String target;

	private String CleanQ30;

	private String RawReads;

	private String CleanReads;

	private String nbbm;

	private String wkbh;

	private String llhsj;

	private String ntm;

	private String tb;

	public String getTb() {
		return tb;
	}

	public void setTb(String tb) {
		this.tb = tb;
	}

	public String getNtm() {
		return ntm;
	}

	public void setNtm(String ntm) {
		this.ntm = ntm;
	}

	public String getLlhsj() {
		return llhsj;
	}

	public void setLlhsj(String llhsj) {
		this.llhsj = llhsj;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getMappingReads() {
		return MappingReads;
	}

	public void setMappingReads(String mappingReads) {
		MappingReads = mappingReads;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

	public String getRefANI() {
		return refANI;
	}

	public void setRefANI(String refANI) {
		this.refANI = refANI;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCleanQ30() {
		return CleanQ30;
	}

	public void setCleanQ30(String cleanQ30) {
		CleanQ30 = cleanQ30;
	}

	public String getRawReads() {
		return RawReads;
	}

	public void setRawReads(String rawReads) {
		RawReads = rawReads;
	}

	public String getCleanReads() {
		return CleanReads;
	}

	public void setCleanReads(String cleanReads) {
		CleanReads = cleanReads;
	}
}
