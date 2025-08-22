package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WkmxPcrResultModel")
public class WkmxPcrResultModel implements Comparable<WkmxPcrResultModel>{
    private String CtVaule;
    //浓度
    private String Concentration;
    //单位
    private String ConcentrationUnit;
    private String StdConcentration;
    private String ReferenceDye;
    private String SampleUID;
    private String ReplicatedGroup;
    //结果
    private String QcResult;
    //位置
    private String WellHIndex;
    //位置
    private String WellVIndex;
    private String Well;
    private String SampleNumber;
    //样本编号
    private String SampleName;
    private String SampleType;
    private String DyeName;
    private String GeneName;
	public String getCtVaule() {
		return CtVaule;
	}
	public void setCtVaule(String ctVaule) {
		CtVaule = ctVaule;
	}
	public String getConcentration() {
		return Concentration;
	}
	public void setConcentration(String concentration) {
		Concentration = concentration;
	}
	public String getConcentrationUnit() {
		return ConcentrationUnit;
	}
	public void setConcentrationUnit(String concentrationUnit) {
		ConcentrationUnit = concentrationUnit;
	}
	public String getStdConcentration() {
		return StdConcentration;
	}
	public void setStdConcentration(String stdConcentration) {
		StdConcentration = stdConcentration;
	}
	public String getReferenceDye() {
		return ReferenceDye;
	}
	public void setReferenceDye(String referenceDye) {
		ReferenceDye = referenceDye;
	}
	public String getSampleUID() {
		return SampleUID;
	}
	public void setSampleUID(String sampleUID) {
		SampleUID = sampleUID;
	}
	public String getReplicatedGroup() {
		return ReplicatedGroup;
	}
	public void setReplicatedGroup(String replicatedGroup) {
		ReplicatedGroup = replicatedGroup;
	}
	public String getQcResult() {
		return QcResult;
	}
	public void setQcResult(String qcResult) {
		QcResult = qcResult;
	}
	public String getWellHIndex() {
		return WellHIndex;
	}
	public void setWellHIndex(String wellHIndex) {
		WellHIndex = wellHIndex;
	}
	public String getWellVIndex() {
		return WellVIndex;
	}
	public void setWellVIndex(String wellVIndex) {
		WellVIndex = wellVIndex;
	}
	public String getWell() {
		return Well;
	}
	public void setWell(String well) {
		Well = well;
	}
	public String getSampleNumber() {
		return SampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		SampleNumber = sampleNumber;
	}
	public String getSampleName() {
		return SampleName;
	}
	public void setSampleName(String sampleName) {
		SampleName = sampleName;
	}
	public String getSampleType() {
		return SampleType;
	}
	public void setSampleType(String sampleType) {
		SampleType = sampleType;
	}
	public String getDyeName() {
		return DyeName;
	}
	public void setDyeName(String dyeName) {
		DyeName = dyeName;
	}
	public String getGeneName() {
		return GeneName;
	}
	public void setGeneName(String geneName) {
		GeneName = geneName;
	}

	@Override
	public int compareTo(WkmxPcrResultModel o) {

		int flag = this.getWellVIndex().compareTo(o.getWellVIndex());
		if(flag == 0) {
			flag = (Integer.valueOf(this.getWell().charAt(0))+Integer.parseInt(this.getWell().substring(1)))
					- (Integer.valueOf(o.getWell().charAt(0))+Integer.parseInt(o.getWell().substring(1)));
		}
		return flag;
//		return this.getWellVIndex().compareTo(o.getWellVIndex());
	}
}
