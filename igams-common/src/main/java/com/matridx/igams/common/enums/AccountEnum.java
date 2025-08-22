package com.matridx.igams.common.enums;

public enum AccountEnum {
	MATRIDX_FINANCE("003","杰毅生物"),//杰毅生物
	BEIJING_FINANCE("004","医检北京分"),//医检北京分
	KEITH_FINANCE("006","杭州恺思"),//杭州恺思
	LAB_FINANCE("010","杰毅医检"),//杰毅医检
	CQMATRIDX_FINANCE("017","重庆杰毅"),//重庆杰毅
	CQJZ_FINANCE("018","重庆基拯"),//重庆基拯
	HNMATRIDX_FINANCE("019","湖南杰毅"),//湖南杰毅

	;
	private final String code;
	private final String value;

	AccountEnum(String code, String value) {
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
		for (AccountEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return "";
	}
}
