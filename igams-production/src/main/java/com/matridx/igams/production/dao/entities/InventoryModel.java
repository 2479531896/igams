package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="InventoryModel")
public class InventoryModel {
	//物料编号
	private String cInvCode;
	//物料名称
	private String cInvName;
	//物料类别	
	private String cInvCCode;
	//生产商
	private String cInvDefine2;
	//货号
	private String cInvDefine3;
	//规格型号
	private String cInvStd;
	//保质期
	private String iMassDate;
	//保存条件
	private String cInvDefine6;
	//保质期单位
	private String cMassUnit;
	//单位
	private String cComUnitCode;
	//类别
	private String cInvABC;
	//修改人员
	private String cModifyPerson;
	//安全库存
	private String isafeNum;
	//注册号
	private String cInvDefine1;

	public String getcInvDefine1() {
		return cInvDefine1;
	}

	public void setcInvDefine1(String cInvDefine1) {
		this.cInvDefine1 = cInvDefine1;
	}

	public String getcModifyPerson() {
		return cModifyPerson;
	}
	public void setcModifyPerson(String cModifyPerson) {
		this.cModifyPerson = cModifyPerson;
	}
	public String getcInvABC() {
		return cInvABC;
	}
	public void setcInvABC(String cInvABC) {
		this.cInvABC = cInvABC;
	}
	public String getcInvDefine2(){
		return cInvDefine2;
	}
	public void setcInvDefine2(String cInvDefine2){
		if(cInvDefine2.length()>20) {
			this.cInvDefine2=cInvDefine2.substring(0,20);
		}else {
			this.cInvDefine2 = cInvDefine2;
		}
	}
	public String getcInvDefine3(){
		return cInvDefine3;
	}
	public void setcInvDefine3(String cInvDefine3){
		if(cInvDefine3.length()>20) {
			this.cInvDefine3=cInvDefine3.substring(0,20);
		}else {
			this.cInvDefine3 = cInvDefine3;
		}
	}
	public String getcInvStd(){
		return cInvStd;
	}
	public void setcInvStd(String cInvStd){
		this.cInvStd = cInvStd;
	}
	public String getiMassDate(){
		return iMassDate;
	}
	public void setiMassDate(String iMassDate){
		this.iMassDate = iMassDate;
	}
	public String getcInvDefine6(){
		return cInvDefine6;
	}
	public void setcInvDefine6(String cInvDefine6){
		this.cInvDefine6 = cInvDefine6;
	}
	public String getcInvCCode(){
		return cInvCCode;
	}
	public void setcInvCCode(String cInvCCode){
		this.cInvCCode = cInvCCode;
	}
	public String getcInvCode() {
		return cInvCode;
	}
	public void setcInvCode(String cInvCode) {
		this.cInvCode = cInvCode;
	}
	public String getcInvName() {
		return cInvName;
	}
	public void setcInvName(String cInvName) {
		this.cInvName = cInvName;
	}
	public String getcMassUnit(){
		return cMassUnit;
	}
	public void setcMassUnit(String cMassUnit){
		this.cMassUnit = cMassUnit;
	}
	public String getcComUnitCode()
	{
		return cComUnitCode;
	}
	public void setcComUnitCode(String cComUnitCode)
	{
		this.cComUnitCode = cComUnitCode;
	}
	public String getIsafeNum() {
		return isafeNum;
	}
	public void setIsafeNum(String isafeNum) {
		this.isafeNum = isafeNum;
	}
}
