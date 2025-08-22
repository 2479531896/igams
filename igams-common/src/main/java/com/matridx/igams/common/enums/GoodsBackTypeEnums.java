package com.matridx.igams.common.enums;

public enum GoodsBackTypeEnums {

	ARRIVALGOODS_BACK("ARRIVALGOODS_BACK"),//到货退回
	QUALITYCHECK_BACK("QUALITYCHECK_BACK"),//质检退回
	;
	private final String code;
	GoodsBackTypeEnums(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
