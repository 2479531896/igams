package com.matridx.las.frame.netty.enums;

/**
 * 仪器状态枚举类
 * @time 20211019
 */
public enum RobotStatesEnum {
	
	ROBOT_OFFLINE("99","未上线"),//未上线
	ROBOT_ONLINE("01","已上线"),//已上线
	ROBOT_BUSY("02","工作中"),//空闲
	ROBOT_EORR("03","出错"),//正在工作
	ROBOT_PAUSE("04","暂停"),//充电路上
	ROBOT_STOP("05","停止"),//停止
	ROBOT_CHARGE("06","正在充电"),//正在充电
	;
	
	private String code;
	private String value;
	
	private RobotStatesEnum(String code,String value) {
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
		for (RobotStatesEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}