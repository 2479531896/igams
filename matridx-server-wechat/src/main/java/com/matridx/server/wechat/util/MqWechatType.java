package com.matridx.server.wechat.util;

public class MqWechatType {
    //启用伙伴信息
    public static final String ENABLE_OR_DISABLE_PARTNER = "matridx.partner.enableOrDisable";
    //添加分析完成时间
    public static final String ADD_FXWCSJ = "matridx.partner.addfxwcsj";
    //新增伙伴信息
    public static final String ADD_PARTNER = "matridx.partner.add";
    //修改伙伴信息
    public static final String MOD_PARTNER = "matridx.partner.mod";
    //删除伙伴信息
    public static final String DEL_PARTNER = "matridx.partner.del";
    //伙伴收费修改
    public static final String MOD_PARTNER_TOLL = "matridx.partner.toll.mod";
    //送检结果
    public static final String RESULT_INSPECTION = "matridx.inspection.result";
    //送检报告说明
    public static final String COMMENT_INSPECTION = "matridx.inspection.comment";
    //送检耐药性
    public static final String RESISTANCE_INSPECTION = "matridx.inspection.resistance";
    //送检详细审核报告
    public static final String DETAILED_INSPECTION = "matridx.inspection.detailed";
    //添加送检临床指南
    public static final String ADD_INSPECTION_GUIDE = "matridx.guide.add";
    //修改送检自免结果
    public static final String MOD_INSPECTION_SELFRESULT = "matridx.selfresult.mod";
    //新增或修改医院信息
    public static final String MOD_HOSPITAL = "matridx.hospital.mod";
    //删除医院信息
    public static final String DEL_HOSPITAL = "matridx.hospital.del";
    //新增送检验证信息
    public static final String ADD_INSPECTION_VERIFICATION = "matridx.inspect.verification.add";
    //修改送检验证信息
    public static final String MOD_INSPECTION_VERIFICATION = "matridx.inspect.verification.mod";
    //删除送检验证信息
    public static final String DEL_INSPECTION_VERIFICATION = "matridx.inspect.verification.del";
    //查询支付结果
    public static final String SELECT_PAY_RESULT = "wechat.select.pay.result";

    //送检信息同步(公司发送，微信接收)
    public static final String OPERATE_INSPECT = "matridx.inspect.operate";
    //基础数据同步
    public static final String OPERATE_BASICDATA = "matridx.basicdata.push";
    //批量修改收费标准
  	public static final String BatchMod_SFBZ = "matridx.partner.sfbz.batchmod";
    //消息对应同步
    public static final String OPERATE_XXDY = "matridx.xxdy.operate";

    public static final String SSE_SENDMSG_EXCEPRION = "wechar.sse.sendmsg.exception"; 
}
