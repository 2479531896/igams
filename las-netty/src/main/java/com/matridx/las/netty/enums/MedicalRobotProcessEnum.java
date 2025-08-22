package com.matridx.las.netty.enums;

/**
 * 机器人操作仪器流程枚举类
 * @time 20211103
 */
public enum  MedicalRobotProcessEnum{
	
	MEDICAL_ROBOT_MOVETOLIBRARY ("01","移动样本到建库仪"),//将样本等移动到建库仪并开始
	MEDICAL_ROBOT_MOVEOUTLIBRARY("02","移出建库仪样本"),//建库仪完成工作后将相应的位置
	MEDICAL_ROBOT_MOVETODISPENSER("03","移动样本到配置仪"),//样本放置到配置仪并开始配液
	MEDICAL_ROBOT_MOVEMACHINETOCLOSING("04","移动样本到压盖机"),//移动样本到压盖机并开始
	MEDICAL_ROBOT_MOVECLOSINGTOPCR("2001","移动样本到定量仪"),//移动样本到定量仪
	MEDICAL_ROBOT_MOVEEMPTYDISPENSER("06","移除空管到配置仪"),//移除空管到配置仪并开始
	MEDICAL_ROBOT_MOVEMACHINETOCLOSINGSECEND("07","抓取八连管到PCR定量仪"),//抓取八连管到PCR定量仪
	MEDICAL_ROBOT_MOVEMACHINETOCLOSINGSECEND_END("20","抓取八连管到PCR定量仪结束操作"),//抓取八连管到PCR定量仪结束操作
	MEDICAL_ROBOT_MOVECLOSINGTOPCRSECEND("08","从压盖机移动样本到定量仪第二次"),//从压盖机移动样本到定量仪第二次并开始定量
	MEDICAL_ROBOT_MOVETODISPENSERTHIRD("09","将测序仪卡放入配置仪"),//将测序仪卡放入配置仪,开始配液
	MEDICAL_ROBOT_MOVETOSEQUENCER("10","样本放入测序仪"),//样本放入测序仪
	MEDICAL_ROBOT_PLACETRAY("11","卡盒托盘放置机器人平台"),//机器人放置卡盒托盘至机器人平台
	MEDICAL_ROBOT_PLACE_EPTUBECARRIER("12","放置枪头载具"),//放置枪头载具
	MEDICAL_ROBOT_RETURNTRAY("13","归还托盘"), //机器人归还托盘 
	MEDICAL_ROBOT_PUTCLAMPINGJAWS("14","机器人归还夹爪"), //机器人归还夹爪
	MEDICAL_ROBOT_PLACEVEHICLE("15","放置有EP管载具"),//放置有EP管载具
	MEDICAL_ROBOT_REPLACETRAY("17","更换EP管的载具"),//更换EP管的载具
	DOOR_OPENING_CLAMPINGCLAW("19","拿开门夹爪"), //机器人拿开门夹爪命令
	DOOR_CLOSOING_CLAMPINGCLAW("20","还关门夹爪"), //机器人还关门夹爪
	MEDICAL_ROBOT_FROM_AUTO_AND_CMH("2001","从配置仪取八连管到压盖机"),//从配置仪取八连管到压盖机
	MEDICAL_ROBOT_CMH("2002","压盖机还有未压管流程"),//压盖机还有未压管流程
	MEDICAL_ROBOT_FROM_CMH_AND_PCR("2003","从压盖机取八连管放入PCR"),//压盖机还有为压管流程
	MEDICAL_ROBOT_PCR_START_ONE("2004","第一次PCR开始"),//压盖机还有为压管流程
	MEDICAL_ROBOT_PCR_START_TWO("2005","第二次PCR开始"),//压盖机还有为压管流程
	MEDICAL_ROBOT_PLACE_CEHICLE("3001","将载具放置AGV平台"), //将载具放置AGV平台
	MEDICAL_ROBOT_PUTMATERIAL_TOCUBICS("3002","物料放置建库仪"), //将物料放置建库仪
	MEDICAL_ROBOT_RETURN_TRAY("3003","归还托盘"), //归还托盘
	MEDICAL_ROBOT_RETURN_JKY("4001","操作建库仪"), //操作建库仪
	MEDICAL_AUTO_FILL_UP("1400","配置仪补满"), //配置仪补满

	;
	
	private String code;
	private String value;
	
	private MedicalRobotProcessEnum(String code,String value) {
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
		for (MedicalRobotProcessEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}