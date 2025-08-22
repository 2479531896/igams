package com.matridx.igams.common.enums;


/**
 * 钉钉消息类型枚举类
 * 
 * @author fuwb
 * @time 20150512
 */
public enum DingMessageType {
	INSPECTION_TYPE("INSPECTION_TYPE","新增送检提醒"),//新增送检提醒
	SYSTS_TYPE("SYSTS_TYPE","实验室推送提醒"),//新增送检提醒
	STATIS_TYPE("STATIS_TYPE","领导汇报周报"),//领导汇报周报
	OVERDUE_TYPE("OVERDUE_TYPE","任务逾期提醒"),//任务逾期提醒
	DALIY_TYPE("DALIY_TYPE","销售统计日报"),//销售统计日报
	GOODS_QA_REAGENT("GOODS_QA_REAGENT","质量试剂人员"),//质量试剂人员
	GOODS_QA_INSTRUMENT("GOODS_QA_INSTRUMENT","质量仪器人员"),//质量仪器人员
	GOODS_IN_STORAGE("GOODS_IN_STORAGE","入库管理"),//入库管理
	INSPECTION_STAND_ERROR("INSPECTION_STAND_ERROR","数据量低于8M提醒"),//数据量低于8M提醒
	INSPECTION_MIN_ERROR("INSPECTION_MIN_ERROR","数据量低于5M提醒"),//数据量低于5M提醒
	INSPECTION_REPORT_TYPE("INSPECTION_REPORT_TYPE","送检报告完成提醒"),//送检报告完成提醒
	FASTQ_ERROR_TYPE("FASTQ_ERROR_TYPE","fastq数据类型错误提醒"),//fastq数据类型错误提醒
	FASTQ_RESULT_TYPE("FASTQ_RESULT_TYPE","fastq结果发送"),//fastq结果发送
	MATRIDX_TEXT_TYPE("MATRIDX_TEXT_TYPE","杰毅生物微信消息"),//杰毅生物微信消息
	UNCOLLECTED_TYPE("UNCOLLECTED_TYPE","未收款特检情况提醒"),//未收款特检情况提醒
	SPECIALAPPLY_TYPE("SPECIALAPPLY_TYPE","送检特殊申请提醒"),//送检特殊申请提醒
	RECHECK_TYPE("RECHECK_TYPE","内部复测通过提醒"),//内部复测申请通过提醒
	RECHECK_OUT_TYPE("RECHECK_OUT_TYPE","外部复测通过提醒"),//外部复测申请通过提醒
	RECHECK_DING_TYPE("RECHECK_DING_TYPE","钉钉复测通过提醒"),//钉钉复测申请通过提醒
	INSPECTION_FEEDBACK("INSPECTION_FEEDBACK","送检反馈提醒"),//送检反馈提醒
	STATISTIC_PARTNER("STATISTIC_PARTNER","统计信息发送提醒"),//统计信息发送提醒
	INSPECTION_EXCEPTION("INSPECTION_EXCEPTION","送检异常提醒"),//送检异常提醒
	VERIFICATION_TYPE("VERIFICATION_TYPE","验证通知"),//送检验证通知
	RFS_UP_TYPE("RFS_UP_TYPE","RFS文件上传通知"),//RFS文件上传通知
	SEND_MESSAGE_TYPE("SEND_MESSAGE_TYPE","钉钉上限通知"),//钉钉上限通知
	PARTNER_FEES_REMINDER("PARTNER_FEES_REMINDER","伙伴收费提示"),//伙伴收费提示
	VERIFICATION_OPERATE_TYPE("VERIFICATION_OPERATE_TYPE","验证运营通知"),//送检验证运营通知
	VERIFICATION_RESULT_TYPE("VERIFICATION_RESULT_TYPE","验证结果通知"),//验证结果通知
	VERIFICATION_AUDIT_FAIL("VERIFICATION_AUDIT_FAIL","验证审核不通过通知"),//验证审核失败通知
	CONTRACT_AUDIT_CC("CONTRACT_AUDIT_CC","合同提交审核抄送人员"),//合同提交审核抄送人员
	PURCHASE_AUDIT_CC("PURCHASE_AUDIT_CC","请购提交审核抄送人员"),//请购提交审核抄送人员
	PURCHASE_AUDIT_XZ_CC("PURCHASE_AUDIT_XZ_CC","行政请购提交审核抄送人员"),//请购提交审核抄送人员
	DINKTALKAUDIT_CALLBACK_ERROR("DINKTALKAUDIT_CALLBACK_ERROR","钉钉审核回调错误通知"),//钉钉审批回调错误信息通知
	PAY_SUCCESS_TYPE("PAY_SUCCESS_TYPE","支付成功通知"),//支付成功通知
	LOGISTICS_WAREHOUSE_NOTICE("LOGISTICS_WAREHOUSE_NOTICE","物流仓库通知"),//物流仓库通知
	MQ_MESSAGECOUNT_NOTICE("MQ_MESSAGECOUNT_NOTICE","MQ消息数量监控提醒"),//MQ消息数量监控提醒
	GOODS_IN_STORAGE_CHECK("GOODS_IN_STORAGE_CHECK","检验完成入库通知"),
	GOODS_IN_CHECK_THORGH("GOODS_IN_CHECK_THORGH","退货或归还检测通知"),//退货或归还检测通知
	GOODS_IN_CHECK_CONSUMABLE("GOODS_IN_CHECK_CONSUMABLE","耗材检测通知"),//耗材检测通知
	GOODS_IN_CHECK_REAGENT("GOODS_IN_CHECK_REAGENT","试剂检测通知"),//试剂检测通知
	GOODS_IN_CHECK_INSTRUMENT("GOODS_IN_CHECK_INSTRUMENT","仪器检测通知"),//仪器检测通知
	GOODS_IN_STORAGE_NOCHECK("GOODS_IN_STORAGE_NOCHECK","无需质检的入库通知"),//无需质检的入库通知
	INVOICE_MAINTENANCE("INVOICE_MAINTENANCE","合同发票核销通知"),//合同发票核销通知
	GOODS_IN_CHECK_DEVICE("GOODS_IN_CHECK_DEVICE","设备到货通知"),//设备到货通知
	SAFETY_STOCK("SAFETY_STOCK","安全库存提醒"),//安全库存提醒
	EXPRESS_MAINTENANCE("EXPRESS_MAINTENANCE","物流信息提醒"),//物流信息提醒
	SALEFEED_BACK("SALEFEED_BACK","售后反馈提醒"),//售后反馈提醒
	UNCOMPARE_KS_INFO("UNCOMPARE_KS_INFO","科室不匹配提醒"),//科室不匹配提醒
	UNCOMPARE_DETECT_INFO("UNCOMPARE_DETECT_INFO","检测项目不匹配提醒"),//检测项目不匹配提醒
	UPLOAD_FAILED_SAMPLE_INFO("UPLOAD_FAILED_SAMPLE_INFO","上传失败样本提醒"),//上传失败样本提醒
	DEVICE_LL("DEVICE_LL","设备领料查看"),
	DEVICE_JCJY("DEVICE_JCJY","设备借出借用查看"),
	DEVICE_JCGH("DEVICE_JCGH","设备归还查看"),
	DEVICE_FH("DEVICE_FH","设备发货查看"),
	DEVICE_TH("DEVICE_TH","设备退货查看"),
	BIOINFORMATION_ERROR("BIOINFORMATION_ERROR","生信错误通知"),
	CONTRACT_DQTX("CONTRACT_DQTX","合同到期提醒"),
	LOGISTICS_FHTZ("LOGISTICS_FHTZ","物流发货通知"),
	LOGISTICS_LLTZ("LOGISTICS_LLTZ","物流领料通知"),
	LOGISTICS_JCJYTZ("LOGISTICS_JCJYTZ","物流借出借用通知"),
	LOGISTICS_XSTZ("LOGISTICS_XSTZ","物流销售通知"),
	MATERIAL_ADDTZ("MATERIAL_ADDTZ","物料新增通知"),
	AUDIT_PASS_XSTZ("AUDIT_PASS_XSTZ","销售审核通过通知"),
	SJ_TODAYINFO("SJ_TODAYINFO","送检当日情况统计查看通知"),
	CG_TODAYINFO("CG_TODAYINFO","采购当日情况统计查看通知"),
	TEMPORARY_QUERY("TEMPORARY_QUERY","临时查询表通知"),
	INSUFFICIENT_INVENTORY("INSUFFICIENT_INVENTORY","库存不足通知"),
	TEST_NUMBER_WARNING("TEST_NUMBER_WARNING","测试数预警提醒"),//测试数预警提醒
	OVERDUE_CONTRACT_WARNING("OVERDUE_CONTRACT_WARNING","商务合同逾期预警提醒"),//合同逾期预警提醒
	NEW_DEPARTMENT_REMINDER("NEW_DEPARTMENT_REMINDER","新增机构提醒"),
	JAVA_ECHART("JAVA_ECHART","营销数据看板"),//未收款特检情况提醒
	VACATION_LEFT("VACATION_LEFT","假期剩余"),//假期剩余
	CONTENT_BATCH("CONTENT_BATCH","连接网站通知"),
	EMAIL_SEND_FAIL("EMAIL_SEND_FAIL","邮箱发送失败通知"),
	LOCKED_USER_PARTNER("LOCKED_USER_PARTNER","用户已锁定伙伴通知"),//用户已锁定伙伴通知
	SERVER_INIT_EXCEPTION("SERVER_INIT_EXCEPTION","服务器初始化异常"),
	EXPENSE_BUDGET_MESSAGE("EXPENSE_BUDGET_MESSAGE","预算费用消息"),
	QUERY_STOCK_MESSAGE("QUERY_STOCK_MESSAGE","U8和OA库存不一致提醒消息"),
	ABNORMAL_REPORT_INFO("ABNORMAL_REPORT_INFO","报告发送异常提醒"),
	DOWNLOADREPORT_ANOMALY_NOTIFICATION("DOWNLOADREPORT_ANOMALY_NOTIFICATION","下载报告异常通知"),
	;
	
	private final String code;
	private final String value;
	DingMessageType(String code,String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String getValueByCode(String code){
		for (DingMessageType enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
	public static DingMessageType getByCode(String code) {
		for (DingMessageType dingMessageType : DingMessageType.values()) {
			if (dingMessageType.code.equals(code)) {
				return dingMessageType;
			}
		}
		return null;
	}
}
