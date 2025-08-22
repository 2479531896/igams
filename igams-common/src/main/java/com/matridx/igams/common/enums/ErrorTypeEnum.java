package com.matridx.igams.common.enums;
/**
 * 审核类型枚举类
 * @author xinyf
 * @date 2015-5-20
 */
public enum ErrorTypeEnum {
	INSPECTION_RESULT_TYPE("INSPECTION_RESULT_TYPE"),//送检结果类型
	INSPECTION_DETAILED_TYPE("INSPECTION_DETAILED_TYPE"),//送检详细审核类型
	USERINSP_DETAILED_TYPE("USERINSP_DETAILED_TYPE"),//用户送检详细审核类型
	INSPECTION_STAND_ERROR("INSPECTION_STAND_ERROR"),//数据量低于8M的提示
	INSPECTION_MIN_ERROR("INSPECTION_MIN_ERROR"),//数据量低于5M的提示
	;
	
	private final String code;
	
	ErrorTypeEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
