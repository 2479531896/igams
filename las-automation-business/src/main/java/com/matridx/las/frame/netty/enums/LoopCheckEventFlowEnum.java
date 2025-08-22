package com.matridx.las.frame.netty.enums;

/**
 * 事件循环判断类型
 * @time 2021215
 */
public enum LoopCheckEventFlowEnum {
	//不同的仪器状态不一样
	LOOP_CHECK_SJLC_QTP("01","取托盘"),//取托盘
	LOOP_CHECK_SJLC_FZYB("02","放置样本到建库仪"),//放置样本到建库仪
	LOOP_CHECK_SJLC_QCYB("03","从建库仪取出样本"),//从建库仪取出样本
	LOOP_CHECK_SJLC_GHTP("04","归还空得托盘"), //归还空托盘
	LOOP_CHECK_TAKE_EIGHT("31","从PCR中取八连管"),//从建库仪取出样本
	LOOP_CHECK_DROP_EIGHT("32","扔八连管"),//从建库仪取出样本
	LOOP_CHECK_TAKE_EIGHT_AUTO("40","从配液仪取八连管"),//从配液仪取八连管
	LOOP_CHECK_TAKE_EIGHT_TO_CMH("41","将八连管放到压盖机"),//将八连管放到压盖机
	LOOP_CHECK_TAKE_LID_TO_EIGHT("42","将八连管盖放到八连管上"),//将八连管盖放到八连管上
	LOOP_CHECK_TAKE_EIGHT_TO_AVG("43","将八连管放到AGV上"),//将八连管放到AGV上
	LOOP_CHECK_TAKE_EIGHT_TO_PCR("44","将八连管放入PCR"),//将八连管放入PCR
	LOOP_CHECK_TAKE_EIGHT_FROM_WL("45","从建库仪后的物料区取八连管"),//将八连管放入PCR
	LOOP_CHECK_TAKE_EIGHT_TO_AUTO("46","夹取八连管到配置仪"),//将八连管放入PCR
	LOOP_CHECK_DROPEP("300","循环从机器人身上向前物料区放ep托盘"),//放置样本到建库仪
	LOOP_CHECK_TAKEEP("301","循环前物料区向机器人身上放ep托盘"),//放置样本到建库仪

	LOOP_CHECK_SJLC_RETURNEPZJ("05","归还EP管载具是否循环"),//归还EP管载具是否循环

	LOOP_CHECK_SFKSAUTO("0101","是否开始auto"),//是否开始auto
	LOOP_CHECK_SFKSCMH("0101","是否开始cmh"),
	;

	private String code;
	private String value;

	private LoopCheckEventFlowEnum(String code, String value) {
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
		for (LoopCheckEventFlowEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}