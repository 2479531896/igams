package com.matridx.las.frame.netty.enums;

/***
 * 卡盒载具来料空间载具枚举
 * @author de'l'l
 *
 */
public enum VehicleEnum {
	//机器人从台面抓取卡盒载具/EP管载具/枪头载具
	AGVTMZQLLKJ_CASSETTECARRIERVEHICLE1("1","卡盒载具"),
	AGVTMZQLLKJ_CASSETTECARRIERVEHICLE2("2","卡盒载具"),
	AGVTMZQLLKJ_CASSETTECARRIERVEHICLE3("3","卡盒载具"),
	AGVTMZQLLKJ_CASSETTECARRIERVEHICLE4("4","卡盒载具"),
	AGVTMZQLLKJ_EPTUBE1("5","EP管载具"),
	AGVTMZQLLKJ_EPTUBE2("6","EP管载具"),
	AGVTMZQLLKJ_EPTUBE3("7","EP管载具"),
	AGVTMZQLLKJ_TIP1("8","枪头载具"),
	AGVTMZQLLKJ_TIP2("9","枪头载具"),
	
	//机器人将载具放置到AGV台面,机器人从AGV台面取载具
	AGVTMZQ_CASSETTECARRIERVEHICLE1("1","卡盒载具"),
	AGVTMZQ_CASSETTECARRIERVEHICLE2("2","卡盒载具"),
	AGVTMZQ_EPTUBE1("3","EP管载具"),
	AGVTMZQ_EPTUBE2("4","EP管载具"),
	AGVTMZQ_TIP1("5","枪头载具"),
	AGVTMZQ_EIGHTTUB("6","八联管载具"),
	AGVTMZQ_SEQCARD("7","测序仪卡盒"),
	
	//机器人从AGV台面载具取料,
	AGVTMQL_CASSETTECARRIERVEHICLE1("1","卡盒载具"),
	AGVTMQL_AGVCASSETTECARRIERVEHICLE2("2","卡盒载具"),
	AGVTMQL_EPTUBE1("3","EP管载具"),
	AGVTMQL_EPTUBE2("4","EP管载具"),
	AGVTMQL_TIP1("5","枪头载具"),
	
	//机器人放料到AGV载具
	AGVTMFL_CASSETTECARRIERVEHICLE1("1","卡盒载具"),
	AGVTMFL_CASSETTECARRIERVEHICLE2("2","卡盒载具"),
	AGVTMFL_EPTUBE1("4","EP管载具"),
	AGVTMFL_EPTUBE2("5","EP管载具"),
	AGVTMFL_TIP1("3","枪头载具"),
	
	//机器人取/放料到建库仪
	RIGHTEP("1","右EP管"),
	LEFTEP("2","左EP管"),
	RIGHTTIP("3","右枪头"),
	LEFTTIP("4","左枪头"),
	CARD("5","卡盒"),
	
	//机器人抓取建库仪后端台面载具,机器人放载具到建库仪后端台面
	AGVHDZQ_EPTUBE1("1","EP管载具"),
	AGVHDZQ_EPTUBE2("2","EP管载具"),
	AGVHDZQ_EIGHTTUB1("3","八联管载具"),
	AGVHDZQ_EIGHTTUB2("4","八联管载具"),
	
	//机器人从建库仪后端八联管载具取八联管
	AGVHDZQBLG_EIGHTTUB1("1","八联管载具"),
	AGVHDZQBLG_EIGHTTUB2("2","八联管载具"),
	
	//机器人从配置仪抓取载具,机器人放置载具到配置仪
	AGVHDZQZJ_EPTUBE1("1","EP管载具"),
	AGVHDZQZJ_EIGHTTUB1("2","八联管载具"),
	SEQCARD("3","测序仪卡盒"),
	
	//机器人运动到AGV-工作台过渡点
	AGVCASSETTECARRIERWORKSPACE("1","卡盒载具来料工作位置"),
	CUBICSMATERIALWORKSPACE("2","建库仪放料及开关门工作位置（1-8号建库仪)"),
	CUBICSOPENDOORWORKSPACE("3","建库仪放料及开关门工作位置（9-16建库仪)"),
	CUBICSAFTERWORKSPACE("4","建库仪后端EP管载具/八联管载具工作位置"),
	AUTOWORKSPACE("5","配置仪工作位置"),
	PCRWORKSPACE("6","荧光定量仪工作位置"),
	CMHWORKSPACE("7","压盖机工作位置"),
	SEQWORKSPACE("8","测序仪卡盒工作位置"),
	
	//设置工作台工作空间
	AGVCASSETTECARRIERSPACE("1","卡盒来料载具空间"),
	CUBICSMATERIALSPACE("2","建库仪放料空间"),
	CUBICSOPENDOORSPACE("3","建库仪开门空间"),
	CUBICSAFTERSPACE("4","建库仪后端EP管载具/八联管载具空间"),
	AUTOSPACE("5","配置仪空间"),
	PCRSPACE("6","荧光定量仪空间"),
	CMHSPACE("7","压盖机空间"),
	SEQSPACE("8","测序仪卡盒空间"),
	
	//拍照位置
	AGVCASSETTECARRIERPICTURESPACE("1","卡盒载具来料拍照位置"),
	
	CUBICSAFTERPICTURESPACE("26","建库仪后端EP管载具/八联管载具拍照位置"),
	AUTOPICTURESPACE("27","配置仪拍照位置"),
	PCRPICTURESPACE("28","荧光定量仪拍照位置"),
	CMHPICTURESPACE("29","压盖机拍照位置"),
	SEQPICTURESPACE("30","测序仪卡盒拍照位置"),
	
	//夹爪类型
	SEQUENCECARDCLAWCLAW("1","测序盒夹爪"),
	EPTUBECLAW("2","EP管夹爪"),
	TIPCLAW("3","枪头夹爪"),
	EPTUBEANDEIGHTTUBECLAW("4","EP管/八联管夹爪"),
	CARDMATERIALCLAW("5","卡盒物料夹爪"),
	EIGHTTUBEMATERIALCLAW("6","八联管物料夹爪"),
	EIGHTLIDCLAW("7","八联管盖夹爪"),
	CARDANDTIPVEHICLECLAW("8","卡盒载具/枪头载具夹爪"),
	OPENANDCLOSECLAW("9","开关门夹爪"),
	
	;

	private String code;
	private String value;
	
	private VehicleEnum(String code,String value) {
		this.value = value;
		this.code = code;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getCode(){
		return code;
	}
	
	public static String getValueByCode(String code){
		for (VehicleEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}	
