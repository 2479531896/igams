package com.matridx.igams.common.enums;
/**
 * 审核流程扩展参数枚举类
 * @author xinyf
 * @date 2015年7月28日
 */
public enum ShlcKzcsEnum {
	/**
	 * 调用外部接口
	 * 管理费设置显示
	 */
	CALL_INTF("01"),  //接口
	COST_DISP("02"),  //显示
	CALL_DISP("03")   //接口和显示
	;
	
	private final String code;
	
	ShlcKzcsEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static ShlcKzcsEnum getEnumByCode(String code){
		for (ShlcKzcsEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi;
			}
		}
		return null;
	}
}
