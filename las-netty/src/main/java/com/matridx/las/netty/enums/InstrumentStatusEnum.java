package com.matridx.las.netty.enums;

/**
 * 仪器状态枚举类
 * @time 20211019
 */
public enum InstrumentStatusEnum {

	STATE_OFFLINE("99","未上线"),//未上线
	STATE_FREE("01","空闲"),//空闲
	STATE_WORK("02","工作中"),//工作中
	STATE_ERROR("03","错误"),//错误
	STATE_PAUSE("04","暂停"),//暂停
	STATE_STOP("05","停止"),//停止

	//pcr的状态20-39
	STATE_PCR_CONNECT("20","已连接"),//已连接
	STATE_PCR_NOCONNECT("21","连接失败"),//已连接
	STATE_PCR_START("22","已开始"),//已开始
	STATE_PCR_COMPLETE("30","已完成"),//已开始

	//定量仪器的状态40-59
	STATE_CPCR_OPEN("40","开抽屉"),//已开抽屉
	STATE_CPCR_REAGENT("41","已放置试剂"),//已放置试剂
	STATE_CPCR_CLOSE("42","已关上抽屉"),//已关上抽屉
	STATE_CPCR_START("43","开始命令"),//开始命令
	STATE_CPCR_ADDSAMPLE("44","已添加样本信息"),//已添加样本信息
	STATE_CPCR_FIRST_COMPLETE("45","第一步完成"),//第一步完成
	STATE_CPCR_STOP("50","已停止"),//已发送停止命令
	STATE_CPCR_SUSPEND("46","已暂停"),//已发暂停
	STATE_CPCR_POOLING("47","发送pooling结果"),//发送给计算好的结果
	STATE_CPCR_SECOND_COMPLETE("48","完成"),//完成

	//仪器状态
	AFTER_ADDING_DILUENT ("1","加完稀释液"),//加完稀释液
	AFTER_ADDING_SAMPLE ( "2","加完样本"),//加完样本
	AFTER_ADDING_AMPLIFICATION ("3","加完扩增液"),//加完扩增液
	AFTER_ADDING_FINAL_MIXTURE ( "4","加最终混合液"),//加最终混合液
	AFTER_ADDING_STANDARD ( "5","加标准液"),//加标准液
	DILUENT_TO_PCR ( "6","样本从稀释孔板移动到荧光孔板"),//样本从稀释孔板移动到荧光孔板
	ADDING_SAMPLE_EP ( "7","样本添加到最终EP管"),//样本添加到最终EP管
	MIXED_SAMPLE_FIRST ( "8","吸取混合样本扩增液到八连管一列第一孔"),//吸取混合样本扩增液到八连管一列第一孔
	FINAL_SEPARATE ( "9","最终液分液"),//最终液分液
	ADDING_STANDARD_SOLUTION ( "10","添加标准液"),//添加标准液
	AUTO_FINISH_ONE ( "Finish_1","完成"),//完成
	AUTO_FINISH_TWO ( "Finish_2","完成"),//完成
	AUTO_FINISH_THREE ( "Finish_3","完成"),//完成

	;
	
	private String code;
	private String value;
	
	private InstrumentStatusEnum(String code, String value) {
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
		for (InstrumentStatusEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	
}