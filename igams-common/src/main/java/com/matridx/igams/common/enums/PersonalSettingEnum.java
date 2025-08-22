package com.matridx.igams.common.enums;

/**
 * 个人设置枚举类
 * @return
 */
public enum PersonalSettingEnum{
		SETTING_NEXT_CONFIRMER("SETTING_NEXT_CONFIRMER"),//任务确认人员
		TEST_PLACE("TEST_PLACE"),//检测单位快捷设置
		MACHINE_TYPE("MACHINE_TYPE"),//
		SEQUENCER_CODE("SEQUENCER_CODE"),//测序仪器类型快捷设置仪快捷设置
		REAGENT_SELECT("REAGENT_SELECT"),//试剂选择快捷设置
		PRINT_ADDRESS("PRINT_ADDRESS"),//打印机设置
		WHETHER_TO_PRINT("WHETHER_TO_PRINT"),//收样确认是否打印
		REAGENT_ONE_SETTINGS("REAGENT_ONE_SETTINGS"),//试剂号1设置(宏基因组DNA建库试剂盒)
		REAGENT_TWO_SETTINGS("REAGENT_TWO_SETTINGS"),//试剂号2设置(逆转录试剂盒)
		REAGENT_THREE_SETTINGS("REAGENT_THREE_SETTINGS"),//试剂号3设置(文库定量试剂盒)
		REAGENT_JSON_SETTINGS("REAGENT_JSON_SETTINGS"),//试剂JSON设置(整合)
		REAGENT_SETTINGS("REAGENT_SETTINGS"),//试剂设置(核酸提取试剂盒)
		PREIMER_NUMBER("PREIMER_NUMBER"),//引物编号
		REAGENT_KZ_SETTINGS("REAGENT_KZ_SETTINGS"),//(用于扩增布板时则指扩增试剂批号)
		REAGENT_TQ_SETTINGS("REAGENT_TQ_SETTINGS"),//(用于扩增布板时则指提取试剂批号)
	    KZBGL_SJDW("KZBGL_SJDW"),//扩增布板检测单位
		BIO_AUDIT("BIO_AUDIT"),//生信审核
		BIO_INSPECT("BIO_INSPECT"),//生信检验
		LIBRARY_SET_TYPE("LIBRARY_SET_TYPE"),//自动排版
		LIBRARY_SET_TYPE_IN("LIBRARY_SET_TYPE_IN"),//自动排版包含

		LIBRARY_SET_TYPE_NOTIN("LIBRARY_SET_TYPE_NOTIN"),//自动排版不包含

		LIBRARY_SET_TYPE_WK("LIBRARY_SET_TYPE_WK"),//文库自动排版
		LIBRARY_SET_TYPE_IN_WK("LIBRARY_SET_TYPE_IN_WK"),//文库自动排版包含

		LIBRARY_SET_TYPE_NOTIN_WK("LIBRARY_SET_TYPE_NOTIN_WK"),//文库自动排版不包含

		HOSPITAL_SELECT("HOSPITAL_SELECT"),//医院选择
		MATERIAL_SELECT("MATERIAL_SELECT"),//物料选择
		TB_AUDIT("TB_AUDIT"),//TB审核人员
		TB_INSPECT("TB_INSPECT"),//TB检验人员
		OPERATOR("OPERATOR"),//操作人
		COLLATOR("COLLATOR"),//核对人
		SNZK_SET_JCXM("SNZK_SET_JCXM"),//室内质控
		;
		
		private final String code;
		
		PersonalSettingEnum(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
}
