package com.matridx.igams.common.enums;

/**
 * 钉钉审批流程枚举类
 * @author yao
 *
 */
public enum DdAuditTypeEnum {
	
	SP_QG("SP_QG"),//请购审批
	SW_HT("SW_HT"),//商务合同审批
	SW_KP("SW_KP"),//商务开票审批
	SP_HT("SP_HT"),//请购审批
	SP_YXHT("SP_YXHT"),//营销合同审批
	SP_SBWX("SP_SBWX"),//设备维修审批
	SP_QG_XZ("SP_QG_XZ"),//行政请购
	SP_QG_XQQR_XZ("SP_QG_XQQR_XZ"),//行政用品需求审批
	SP_QG_JGQR_XZ("SP_QG_JGQR_XZ"),//行政请购价格确认
	SP_FK_XZ_YJ("SP_FK_XZ_YJ"),//行政付款(月结)
	SP_FK_XZ_GDG("SP_FK_XZ_GDG"),//行政付款(月结)
	SP_GFYZSQ("SP_GFYZSQ"),//供方申请
	SP_GFYZJG("SP_GFYZJG"),//供方结果
	SP_GFPJ("SP_GFPJ"),//供方评价
	SP_GFJX("SP_GFJX");//供方绩效
	private final String code;

	DdAuditTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
