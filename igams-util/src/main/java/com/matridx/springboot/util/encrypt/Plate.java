package com.matridx.springboot.util.encrypt;

public class Plate {

	//标本名称
	private String sampleName;//char
	//引物名称（未使用）
	private String primerName;//char
	//备注（未使用）
	private String remark; //char
	//唯一性编号
	private String uid;//char
	//标本编号
	private String sampleUid;//char
	//每个通道设置的项目名称
	private String[] targetItem = new String[6];//char
	//是否使用通道
	private String[] channelIsEnable = new String[6];//bool
	//是否设置了浓度值
	private String[] concentrationIsSet = new String[6];//bool
	//是否显示曲线（未使用）
	private String showCurve;//bool
	//标本是否使用
	private String enable;//bool
	//标本类型
	private String sampleType; //qint16--short
	//每个通道的浓度值
	private String[] concentration = new String[6];//double
	//分组编号（未使用）
	private String groupNo; //qint16--short
    //模板基因名称
	private String mbjymc;
	//设定的标本的type，用来帮助设定结构体的sampleType类型
	private String type;	
	//存放的序号
	private String xh;
	
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMbjymc() {
		return mbjymc;
	}
	public void setMbjymc(String mbjymc) {
		this.mbjymc = mbjymc;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getPrimerName() {
		return primerName;
	}
	public void setPrimerName(String primerName) {
		this.primerName = primerName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSampleUid() {
		return sampleUid;
	}
	public void setSampleUid(String sampleUid) {
		this.sampleUid = sampleUid;
	}	  	
	public String[] getTargetItem() {
		return targetItem;
	}
	public void setTargetItem(String[] targetItem) {
		this.targetItem = targetItem;
	}
	public String[] getChannelIsEnable() {
		return channelIsEnable;
	}
	public void setChannelIsEnable(String[] channelIsEnable) {
		this.channelIsEnable = channelIsEnable;
	}
	public String[] getConcentrationIsSet() {
		return concentrationIsSet;
	}
	public void setConcentrationIsSet(String[] concentrationIsSet) {
		this.concentrationIsSet = concentrationIsSet;
	}
	public String getShowCurve() {
		return showCurve;
	}
	public void setShowCurve(String showCurve) {
		this.showCurve = showCurve;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public void setConcentration(String[] concentration) {
		this.concentration = concentration;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String[] getConcentration() {
		return concentration;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

}







////标本名称
//private String sampleName;
////引物名称（未使用）
//private String primerName;
////备注（未使用）
//private String remark; 
////唯一性编号
//private String uid;
////标本编号
//private String sampleUid;
////每个通道设置的项目名称
//private String targetItem[];
////是否使用通道
//private boolean channelIsEnable[];
////是否设置了浓度值
//private boolean concentrationIsSet[];
////是否显示曲线（未使用）
//private boolean showCurve;
////标本是否使用
//private boolean enable;
////标本类型
//private String sampleType; 
////每个通道的浓度值
//private double concentration[];
////分组编号（未使用）
//private String groupNo;
//
//public String getSampleName() {
//	return sampleName;
//}
//public void setSampleName(String sampleName) {
//	this.sampleName = sampleName;
//}
//public String getPrimerName() {
//	return primerName;
//}
//public void setPrimerName(String primerName) {
//	this.primerName = primerName;
//}
//public String getRemark() {
//	return remark;
//}
//public void setRemark(String remark) {
//	this.remark = remark;
//}
//public String getUid() {
//	return uid;
//}
//public void setUid(String uid) {
//	this.uid = uid;
//}
//public String getSampleUid() {
//	return sampleUid;
//}
//public void setSampleUid(String sampleUid) {
//	this.sampleUid = sampleUid;
//}	  	
//public String[] getTargetItem() {
//	return targetItem;
//}
//public void setTargetItem(String[] targetItem) {
//	this.targetItem = targetItem;
//}
//public boolean[] getChannelIsEnable() {
//	return channelIsEnable;
//}
//public void setChannelIsEnable(boolean[] channelIsEnable) {
//	this.channelIsEnable = channelIsEnable;
//}
//public boolean[] getConcentrationIsSet() {
//	return concentrationIsSet;
//}
//public void setConcentrationIsSet(boolean[] concentrationIsSet) {
//	this.concentrationIsSet = concentrationIsSet;
//}
//public boolean isShowCurve() {
//	return showCurve;
//}
//public void setShowCurve(boolean showCurve) {
//	this.showCurve = showCurve;
//}
//public boolean isEnable() {
//	return enable;
//}
//public void setEnable(boolean enable) {
//	this.enable = enable;
//}
//public String getSampleType() {
//	return sampleType;
//}
//public void setSampleType(String sampleType) {
//	this.sampleType = sampleType;
//}
//public double[] getConcentration() {
//	return concentration;
//}
//public void setConcentration(double[] concentration) {
//	this.concentration = concentration;
//}
//public String getGroupNo() {
//	return groupNo;
//}
//public void setGroupNo(String groupNo) {
//	this.groupNo = groupNo;
//}
