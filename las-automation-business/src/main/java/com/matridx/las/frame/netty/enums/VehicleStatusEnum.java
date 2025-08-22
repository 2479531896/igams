package com.matridx.las.frame.netty.enums;
/**
 * 载具状态枚举类
 * @time 20211203
 */
public enum VehicleStatusEnum {
		//卡盒状态
		VEHICLE_CARDBOX_PENDING ( "00","待处理"),//卡盒待处理
		VEHICLE_CARDBOX_USE ( "10","使用中"),//卡盒使用中，放入建库仪		
	    VEHICLE_CARDBOX_SAVE ( "20","已保存"),//卡盒使用中，放入建库仪

		//托盘状态
		VEHICLE_TRAY_PENDING ( "1","待处理"),//托盘待处理
		VEHICLE_TRAY_USE ( "2","使用中"),//托盘使用中，放入机器人平台
	    VEHICLE_TRAY_FREE ( "0","空闲中"),//托盘可以加入样本

	;
		
		private String code;
		private String value;
		
		private VehicleStatusEnum(String code,String value) {
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
			for (VehicleStatusEnum enumi : values()) {
				if(enumi.getCode().equals(code)){
					return enumi.getValue();
				}
			}
			return null;
		}
		
	}