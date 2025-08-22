package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="NyjyzsglModel")
public class NyjyzsglModel extends BaseModel{
	//耐药基因ID
	private String nyjyid;
	//基因家族名称   gene_family
	private String jyjzmc;
	//注释内容  comment
	private String zsnr;
	//耐药机制
	private String drugclass;
	//耐药类型
	private String mainmechanism;

	public String getDrugclass() {
		return drugclass;
	}

	public void setDrugclass(String drugclass) {
		this.drugclass = drugclass;
	}

	public String getMainmechanism() {
		return mainmechanism;
	}

	public void setMainmechanism(String mainmechanism) {
		this.mainmechanism = mainmechanism;
	}

	//耐药基因ID
	public String getNyjyid() {
		return nyjyid;
	}
	public void setNyjyid(String nyjyid){
		this.nyjyid = nyjyid;
	}
	//基因家族名称   gene_family
	public String getJyjzmc() {
		return jyjzmc;
	}
	public void setJyjzmc(String jyjzmc){
		this.jyjzmc = jyjzmc;
	}
	//注释内容  comment
	public String getZsnr() {
		return zsnr;
	}
	public void setZsnr(String zsnr){
		this.zsnr = zsnr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
