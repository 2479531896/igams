package com.matridx.igams.common.enums;
/**
 * 消息类型枚举类
 * @author xinyf
 * @date 2015-5-28
 */
public enum MQTypeEnum_pub {
	CONTRACE_BASIC_W2P ("mq.tran.basic.w2p","文件上传到服务器"),
	MOD_HOSPITAL("matridx.hospital.mod","编辑医院信息"),
	DEL_HOSPITAL("matridx.hospital.del","删除医院信息");
	private final String code;
	private final String value;
	
	MQTypeEnum_pub(String code, String value) {
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
		for (MQTypeEnum_pub enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
