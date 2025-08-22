package com.matridx.igams.common.enums;

/**
 * 数据权限类型枚举
 * @author goofus
 *
 */
public enum DataPermissionTypeEnum {
	
	/** 权限类型，附加自定义SQL */
	PERMISSION_TYPE_SQL("PERMISSION_TYPE_SQL","附加自定义SQL"),
	/** 权限类型，多单位权限 */
	PERMISSION_TYPE_DDWQX_JS("PERMISSION_TYPE_DDWQX_JS","角色多单位权限"),//角色对单位进行限制
	/** 权限类型，多单位权限扩展 */
	PERMISSION_TYPE_DDWQX_EXTEND("PERMISSION_TYPE_DDWQX_EXTEND","多单位权限扩展"),//属于多个单位的情况，前后两个权限不一样的时候
	/** 权限类型，未审批 */
	PERMISSION_TYPE_WSP("PERMISSION_TYPE_WSP","未审批"),
	/** 权限类型，已审批 */
	PERMISSION_TYPE_YSP("PERMISSION_TYPE_YSP","已审批"),
	/** 权限类型，已审批(关联履历的情况) */
	PERMISSION_TYPE_YSP_LL("PERMISSION_TYPE_YSP_LL","已审批"),
	/** 审批单位权限*/
	PERMISSION_TYPE_DDWQX_SP("PERMISSION_TYPE_DDWQX_SP","审批多单位权限");//审批角色对单位进行限制
	
	private final String code;
	private final String value;
	
	DataPermissionTypeEnum(String code, String value) {
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
		for (DataPermissionTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
