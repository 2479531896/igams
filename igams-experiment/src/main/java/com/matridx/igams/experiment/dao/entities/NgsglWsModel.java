package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="NgsglWsModel")
public class NgsglWsModel {

	//设备ID
	private String DeviceAddress;
	//操作权限
	private String AccountId;
	//创建时间
	private String Created;
	//通道号
	private String OrderId;
	//试剂号
	private String Reagent;
	//制备法
	private String PreparationMethod;
	//内部编码
	private String SampleName;
	//提纯试剂批号
	private String ReagentBox;
	//建库试剂批号
	private String BuildReagent;
	//接头号
	private String Biomarker;
	//开始时间
	private String StartTime;
	//结束时间
	private String CompleteTime;
	//是否成功
	private String IsSucceed;
	//备注
	private String Note;
	//录入人员
	private String AccountName;
	//检测单位名称
	private String InspectionUnitName;
	//网关地址
	private String MACAddress;
	//进行状态
	private String SuccessStatus;
	//实验类型
	private String ExperimentTypeDesc;

	public String getExperimentTypeDesc() {
		return ExperimentTypeDesc;
	}

	public void setExperimentTypeDesc(String experimentTypeDesc) {
		ExperimentTypeDesc = experimentTypeDesc;
	}

	public String getSuccessStatus() {
		return SuccessStatus;
	}
	public void setSuccessStatus(String successStatus) {
		SuccessStatus = successStatus;
	}
	public String getMACAddress() {
		return MACAddress;
	}
	public void setMACAddress(String mACAddress) {
		MACAddress = mACAddress;
	}
	public String getInspectionUnitName() {
		return InspectionUnitName;
	}
	public void setInspectionUnitName(String inspectionUnitName) {
		InspectionUnitName = inspectionUnitName;
	}
	public String getDeviceAddress() {
		return DeviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		DeviceAddress = deviceAddress;
	}
	public String getAccountId() {
		return AccountId;
	}
	public void setAccountId(String accountId) {
		AccountId = accountId;
	}
	public String getCreated() {
		return Created;
	}
	public void setCreated(String created) {
		Created = created;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public String getReagent() {
		return Reagent;
	}
	public void setReagent(String reagent) {
		Reagent = reagent;
	}
	public String getPreparationMethod() {
		return PreparationMethod;
	}
	public void setPreparationMethod(String preparationMethod) {
		PreparationMethod = preparationMethod;
	}
	public String getSampleName() {
		return SampleName;
	}
	public void setSampleName(String sampleName) {
		SampleName = sampleName;
	}
	public String getReagentBox() {
		return ReagentBox;
	}
	public void setReagentBox(String reagentBox) {
		ReagentBox = reagentBox;
	}
	public String getBuildReagent() {
		return BuildReagent;
	}
	public void setBuildReagent(String buildReagent) {
		BuildReagent = buildReagent;
	}
	public String getBiomarker() {
		return Biomarker;
	}
	public void setBiomarker(String biomarker) {
		Biomarker = biomarker;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getCompleteTime() {
		return CompleteTime;
	}
	public void setCompleteTime(String completeTime) {
		CompleteTime = completeTime;
	}
	public String getIsSucceed() {
		return IsSucceed;
	}
	public void setIsSucceed(String isSucceed) {
		IsSucceed = isSucceed;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	
	
}
