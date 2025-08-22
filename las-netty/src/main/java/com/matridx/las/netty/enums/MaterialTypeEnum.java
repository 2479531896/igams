package com.matridx.las.netty.enums;

/***
 * 物料区类型
 * @author de'l'l
 *
 */
public enum MaterialTypeEnum {

	MATERIAL_FMA("wlq","前物料区"),

	MATERIAL_CUBICS("1","建库仪"),
	MATERIAL_AUTO("2","配置仪"),
	MATERIAL_PCR("3","定量仪"),
	MATERIAL_SEQ("4","测序仪"),
	MATERIAL_AGV("5","机器人"),
	MATERIAL_CMH("6","加盖机"),
	MATERIAL_OCP("7","后物料区"),
	MATERIAL_ZJ("zj","组件"),
	ZJ_TYPE_TP("TP","托盘"),
	ZJ_TYPE_EP("EP","EP管"),
	ZJ_TYPE_BL("BL","八连"),
	ZJ_TYPE_QT("QT","枪头"),

	;

	private String code;
	private String value;

	private MaterialTypeEnum(String code, String value) {
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
		for (MaterialTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}	
