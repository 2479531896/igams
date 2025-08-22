package com.matridx.igams.common.enums;
/**
 * 消息类型枚举类
 * @author xinyf
 * @date 2015-5-28
 */
public enum MQTypeEnum {
	WECHAR_USER_MOD("wechar.user.mod","更新微信用户信息"),
	WECHAR_SUBSCIBE("wechar.subscibe","订阅"),
	WECHAR_UNSUBSCIBE("wechar.unsubscibe","取消订阅"),
	WECHAT_AUTHORIZE("wechat.authorize","微信用户授权"),
	ADD_CHECK_APPLY("matridx.inspect.apply","检验申请"),
	DEL_CHECK_APPLY("matridx.inspect.delapply","删除检验申请"),
	MOD_PARTNER("matridx.partner.mod","修改伙伴信息"),
	DEL_PARTNER("matridx.partner.del","删除伙伴信息"),
	ADD_PARTNER("matridx.partner.add","新增伙伴信息"),
	ENABLE_OR_DISABLE_PARTNER("matridx.partner.enableOrDisable","启用或者停用伙伴信息"),
	ADD_FXWCSJ("matridx.partner.addfxwcsj","添加分析完成时间同步到85端"),
	MOD_PARTNER_TOLL("matridx.partner.toll.mod","伙伴收费修改"),
	BatchMod_SFBZ("matridx.partner.sfbz.batchmod","批量修改伙伴收费修改"),
	WECHAT_TEXT("wechar.text","文本消息"),
	MENU_CLICK("wechar.menu.click","菜单点击"),
	REGISTER("matridx.register.result","注册信息同步"),
	RESULT_INSPECTION("matridx.inspection.result","送检结果"),
	COMMENT_INSPECTION("matridx.inspection.comment","送检结果说明"),
	RESISTANCE_INSPECTION("matridx.inspection.resistance","送检耐药性"),
	DETAILED_INSPECTION ("matridx.inspection.detailed","送检详细审核结果"),
	DETAILED_IGAMS_INSPECTION ("matridx.igams.inspection.detailed","生信送检详细审核结果"),
	CONTRACE_BASIC_W2P ("mq.tran.basic.w2p","转换pdf"),
	CONTRACE_BASIC_OK ("mq.tran.basic.ok","文件上传到服务器"),
	MOD_INSPECTION_SELFRESULT("matridx.selfresult.mod","修改送检自免结果"),
	ADD_INSPECTION_GUIDE("matridx.guide.add","新增送检临床指南"),
	MOD_INSPECTION_VERIFICATION("matridx.inspect.verification.mod","修改送检验证信息"),
	ADD_INSPECTION_VERIFICATION("matridx.inspect.verification.add","新增送检验证信息"),
	DEL_INSPECTION_VERIFICATION("matridx.inspect.verification.del","新增送检验证信息"),
	ADD_TRANSFER("wechat.transfer.add","调用接口信息"),
	OPERATE_INSPECTION("matridx.inspection.operate","送检信息操作同步"),
	OPERATE_INSPECT("matridx.inspect.operate","送检信息操作同步"),
	WECHAR_LOGISTICS_ORDER("wechat.logistics.order","物流接单录入保存"),
	OPERATE_BASICDATA("matridx.basicdata.operate","基础数据同步"),
	MATCHING_SEND_REPORT("matridx.matching.sendreport","对接接口发送报告"),
	Z_FILE_DISPOSE("wechat.file.dispose","Z项目文件处理"),
	SSE_SENDMSG("matridx.sse.sendmsg","sse发送消息"),
	SSE_SENDMSG_EXCEPRION("matridx.sse.sendmsg.exception","sse发送消息异常"),
	SSE_SENDMSG_EXCEPRION_WECHART("wechar.sse.sendmsg.exception","sse发送消息异常"),
	FLU_FILE_ANALYSIS("wechat.flu.file.analysis","甲乙流项目文件解析"),
	;
	private final String code;
	private final String value;
	
	MQTypeEnum(String code, String value) {
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
		for (MQTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
