package com.matridx.igams.common.enums;

public enum GoodsStatusEnum{
	
	GODDS_CHECK("01","到货验收"),//01到货验收
	GODDS_QUALITY("02","质量检验"),//02质量检验
	GODDS_STORE("03","入库管理"),//03入库管理
	REPAID_STORE("05","归还管理"),//05归还管理
	GODDS_NORMAL("80","正常区"),//80正常区
	GODDS_SCRAP("99","废品区"),//99废品区
	;
	private final String code;
	private final String value;
	
	GoodsStatusEnum(String code, String value) {
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
		for (GoodsStatusEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}
