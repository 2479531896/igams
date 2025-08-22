package com.matridx.server.wechat.enums;
/**
 * 审核结果枚举类
 * @author xinyf
 * @date 2015-5-28
 */
public enum MQWechatTypeEnum {
	WECHAR_SUBSCIBE("wechar.subscibe","订阅"),
	WECHAR_UNSUBSCIBE("wechar.unsubscibe","取消订阅"),
	WECHAT_AUTHORIZE("wechat.authorize","微信用户授权"),
	WECHAT_TEXT("wechar.text","文本消息"),
	MENU_CLICK("wechar.menu.click","菜单点击"),
	ADD_CHECKUP("causative.check.add","新增病原体检查"),
	MOD_PARTNER("matridx.partner.mod","修改伙伴信息"),
	DEL_PARTNER("matridx.partner.del","删除伙伴信息"),
	MOD_PARTNER_TOLL("matridx.partner.toll.mod","伙伴收费修改"),
	BatchMod_SFBZ("matridx.partner.sfbz.batchmod","批量修改伙伴收费修改"),
	ADD_PARTNER("matridx.partner.add","新增伙伴信息"),
	ENABLE_OR_DISABLE_PARTNER("matridx.partner.enableOrDisable","启用或者停用伙伴信息"),

	ADD_FXWCSJ("matridx.partner.addfxwcsj","添加分析完成时间同步到85端"),
	COMMENT_INSPECTION("matridx.inspection.comment","送检结果说明"),
	RESISTANCE_INSPECTION("matridx.inspection.resistance","送检耐药性"),
	DETAILED_INSPECTION ("matridx.inspection.detailed","送检详细审核结果"),
	CONTRACE_BASIC_W2P ("mq.tran.basic.w2p","文件上传到服务器"),
	CONTRACT_TOPIC("doc2pdf_exchange","word转pdf"),
	REGISTER_SEND("matridx.register.result","注册信息同步"),
	WECHAR_USER_MOD("wechar.user.mod","更新微信用户信息"),
	ADD_INSPECTION_GUIDE("matridx.guide.add","新增送检临床指南"),
	SELECT_PAY_RESULT("wechat.select.pay.result","查询支付结果"),
	ADD_TRANSFER("wechat.transfer.add","新增接口调用信息"),
	OPERATE_INSPECTION("matridx.inspection.operate","送检信息操作同步"),
	OPERATE_INSPECT("matridx.inspect.operate","送检信息操作同步"),
	OPERATE_XXDY("matridx.xxdy.operate","信息对应操作同步"),
	OPERATE_BASICDATA("matridx.basicdata.push","基础数据同步"),
	SSE_SENDMSG_EXCEPRION("wechar.sse.sendmsg.exception","sse发送消息异常"),
	SSE_SENDMSG_EXCEPRION_MATRIDX("matridx.sse.sendmsg.exception","sse发送消息异常"),
	;
	
	
	private String code;
	private String value;
	
	private MQWechatTypeEnum(String code,String value) {
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
		for (MQWechatTypeEnum enumi : values()) {
			if(enumi.getCode().equals(code)){
				return enumi.getValue();
			}
		}
		return null;
	}
}
