package com.matridx.las.frame.connect.enums;
/**
 * 一些公用的枚举类
 * @time 20211203
 */
public enum GlobalrouteEnum {

	CMD_SEND_TYPE_CMD ( "1","命令"),
	CMD_SEND_TYPE_EVEN ( "2","事件"),
	SYSTEM_STATE_FREE("0","空闲"),
	SYSTEM_STATE_SUSPEND("1","暂停"),
	SYSTEM_STATE_STOP("2","停止"),
	SYSTEM_STATE_WROK("3","工作中"),

	;
		private String code;
		private String value;

		private GlobalrouteEnum(String code, String value) {
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
			for (GlobalrouteEnum enumi : values()) {
				if(enumi.getCode().equals(code)){
					return enumi.getValue();
				}
			}
			return null;
		}

	}