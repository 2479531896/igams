package com.matridx.igams.common.enums;

/**
 * 实体类别枚举类
 * 
 * @author fuwb
 * @time 20150401
 */
public enum EntityTypeEnum {
	ENTITY_SJZZSQDTO("ENTITY_SJZZSQDTO"),//送检纸质申请实体
	ENTITY_SJPDGLDTO("ENTITY_SJPDGLDTO"),//送检派单物流
	ENTITY_SJWLXXDTO("ENTITY_SJWLXXDTO"),//送检物流信息
	;
	//TRANSVERSE_PROJECT_TYPE("TRANSVERSE_PROJECT_TYPE"); // 横向项目类型
	private final String code;

	EntityTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}