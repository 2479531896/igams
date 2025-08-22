package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="DlysymxModel")
public class DlysymxModel extends BaseModel{
	//荧光定量仪结果id
	private String dlysymxid;
	//ct值
	private String ctvalue;
	//浓度
	private String concentration;
	//浓度单位
	private String concentrationunit;
	//标准浓度
	private String stdconcentration;
	//荧光
	private String referencedye;
	//唯一编号
	private String sampleuid;
	//重复组
	private String replicatedgroup;
	//结果
	private String qcresult;
	//横坐标
	private String wellhindex;
	//纵坐标
	private String wellvindex;
	//孔号
	private String well;
	//样本编号
	private String samplenumber;
	//样本名称
	private String samplename;
	//样本类型
	private String sampletype;
	//后缀
	private String dyename;
	//目标基因
	private String genename;
	//文库id
	private String wkid;
	//收到结果时间
	private String jssj;
	//检测单位
	private String jcdw;
	//实验室类型
	private String jcdwtype;
	//prc第几次结果
	private String djc;
	//荧光定量仪结果id
	public String getDlysymxid() {
		return dlysymxid;
	}
	public void setDlysymxid(String dlysymxid){
		this.dlysymxid = dlysymxid;
	}
	//ct值
	public String getCtvalue() {
		return ctvalue;
	}
	public void setCtvalue(String ctvalue){
		this.ctvalue = ctvalue;
	}
	//浓度
	public String getConcentration() {
		return concentration;
	}
	public void setConcentration(String concentration){
		this.concentration = concentration;
	}
	//浓度单位
	public String getConcentrationunit() {
		return concentrationunit;
	}
	public void setConcentrationunit(String concentrationunit){
		this.concentrationunit = concentrationunit;
	}
	//标准浓度
	public String getStdconcentration() {
		return stdconcentration;
	}
	public void setStdconcentration(String stdconcentration){
		this.stdconcentration = stdconcentration;
	}
	//荧光
	public String getReferencedye() {
		return referencedye;
	}
	public void setReferencedye(String referencedye){
		this.referencedye = referencedye;
	}
	//唯一编号
	public String getSampleuid() {
		return sampleuid;
	}
	public void setSampleuid(String sampleuid){
		this.sampleuid = sampleuid;
	}
	//重复组
	public String getReplicatedgroup() {
		return replicatedgroup;
	}
	public void setReplicatedgroup(String replicatedgroup){
		this.replicatedgroup = replicatedgroup;
	}
	//结果
	public String getQcresult() {
		return qcresult;
	}
	public void setQcresult(String qcresult){
		this.qcresult = qcresult;
	}
	//横坐标
	public String getWellhindex() {
		return wellhindex;
	}
	public void setWellhindex(String wellhindex){
		this.wellhindex = wellhindex;
	}
	//纵坐标
	public String getWellvindex() {
		return wellvindex;
	}
	public void setWellvindex(String wellvindex){
		this.wellvindex = wellvindex;
	}
	//孔号
	public String getWell() {
		return well;
	}
	public void setWell(String well){
		this.well = well;
	}
	//样本编号
	public String getSamplenumber() {
		return samplenumber;
	}
	public void setSamplenumber(String samplenumber){
		this.samplenumber = samplenumber;
	}
	//样本名称
	public String getSamplename() {
		return samplename;
	}
	public void setSamplename(String samplename){
		this.samplename = samplename;
	}
	//样本类型
	public String getSampletype() {
		return sampletype;
	}
	public void setSampletype(String sampletype){
		this.sampletype = sampletype;
	}
	//后缀
	public String getDyename() {
		return dyename;
	}
	public void setDyename(String dyename){
		this.dyename = dyename;
	}
	//目标基因
	public String getGenename() {
		return genename;
	}
	public void setGenename(String genename){
		this.genename = genename;
	}

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

	//收到结果时间
	public String getJssj() {
		return jssj;
	}
	public void setJssj(String jssj){
		this.jssj = jssj;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getJcdwtype() {
		return jcdwtype;
	}

	public void setJcdwtype(String jcdwtype) {
		this.jcdwtype = jcdwtype;
	}

	public String getDjc() {
		return djc;
	}

	public void setDjc(String djc) {
		this.djc = djc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
