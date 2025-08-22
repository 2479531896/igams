package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * 建库仪参数上报类
 */
@Alias(value = "CubisUpParamModel")
public class CubisUpParamModel  {
	//通道号
	private String CubsChannel;
	//制备法
	private String PreparationMethod;
	//子设备
	private String SubDeviceAdd;
	//样本编号
	private String SampleName;
	//试剂号
	private String Reagent;
	//提纯试剂编号
	private String ReagentBox;
	//建库实际编号
	private String BuildReagent;
	//接头号
	private String Biomarker;
	//是否打印
	private String IsPrint;
	//是否删除设备
	private String IsReadDel;
	//设备状态
	private String WorkStage;
	//任务状态   todo判断
	private String ProcessStage;
	//开始时间
	private String StartTime;
	//完成时间
	private String CompleteTime;
	//错误信息
	private String ErrorExplain;
	//设备是否上电
	private String IsOnPower;

	public String getCubsChannel() {
		return CubsChannel;
	}

	public void setCubsChannel(String cubsChannel) {
		CubsChannel = cubsChannel;
	}

	public String getPreparationMethod() {
		return PreparationMethod;
	}

	public void setPreparationMethod(String preparationMethod) {
		PreparationMethod = preparationMethod;
	}

	public String getSubDeviceAdd() {
		return SubDeviceAdd;
	}

	public void setSubDeviceAdd(String subDeviceAdd) {
		SubDeviceAdd = subDeviceAdd;
	}

	public String getSampleName() {
		return SampleName;
	}

	public void setSampleName(String sampleName) {
		SampleName = sampleName;
	}

	public String getReagent() {
		return Reagent;
	}

	public void setReagent(String reagent) {
		Reagent = reagent;
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

	public String getIsPrint() {
		return IsPrint;
	}

	public void setIsPrint(String isPrint) {
		IsPrint = isPrint;
	}

	public String getIsReadDel() {
		return IsReadDel;
	}

	public void setIsReadDel(String isReadDel) {
		IsReadDel = isReadDel;
	}

	public String getWorkStage() {
		return WorkStage;
	}

	public void setWorkStage(String workStage) {
		WorkStage = workStage;
	}

	public String getProcessStage() {
		return ProcessStage;
	}

	public void setProcessStage(String processStage) {
		ProcessStage = processStage;
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

	public String getErrorExplain() {
		return ErrorExplain;
	}

	public void setErrorExplain(String errorExplain) {
		ErrorExplain = errorExplain;
	}

	public String getIsOnPower() {
		return IsOnPower;
	}

	public void setIsOnPower(String isOnPower) {
		IsOnPower = isOnPower;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
