package com.matridx.igams.common.enums;

/**
 * 信息同步枚举类
 */
public enum RabbitEnum {
	INSP_ADD("insp_add"), // 新增送检信息
	INSP_MOD("insp_mod"), // 修改送检信息
	INSP_IMP("insp_imp"), // 特检导入同步
	INSP_ADJ("insp_adj"), // 调整送检信息
	SFJS_UPD("sfjs_upd"),//修改送检信息表的是否接收，接收日期，接收人员为null，
	TSSQ_MOD("tssq_mod"), // 修改送检信息(特殊申请)
	INSP_UPD("insp_upd"), // 新增或修改送检信息(8086接收)
	INSP_DEL("insp_del"), // 删除送检信息
	AMOU_MOD("amou_mod"), // 检测项目付款维护
	INSP_CFM("insp_cfm"), // 更新确认信息
	PYIF_ADD("pyif_add"), // 新增支付信息
	PYIF_MOD("pyif_mod"), // 同步支付信息（送检）
	FDIF_MOD("fdif_mod"), // 同步支付信息（复检）
	PYIF_SUC("pyif_suc"), // 支付成功通知
	PYIF_RES("pyif_res"), // 手机付款结果(旧，应该不用了)
	FEED_BAC("feed_bac"), // 反馈消息同步
	SJHB_MOD("sjhb_mod"), // 同步合作伙伴信息
	SJSY_MOD("sjsy_mod"), // 同步修改送检实验信息
	SJSY_ADJ("sjsy_adj"), // 同步修改送检实验信息
	SJSY_ADD("sjsy_add"), // 同步新增送检实验信息
	XMSY_MOD("xmsy_mod"), // 同步修改项目实验信息
	CSKZ_UPD("cskz_upd"), // 同步扩展修改
	INSP_XHU("insp_xhu"), // 杏和接口修改
	BBSY_ADJ("bbsy_adj"),//样本实验调整
	FILE_SAV("file_sav"),//文件保存
	// 暂未使用
	WECH_MOD("wech_mod"), // 修改微信信息
	WECH_SUB("wech_sub"), // 订阅
	WECH_UNS("wech_uns"), // 取消订阅
	WECH_TXT("wech_txt"), // 文本消息
	WECH_ICK("wech_ick"), // 菜单点击
	
	XTYH_MOD("xtyh_mod"), //更新系统用户
	XTYH_MODLIST("xtyh_bmd"), //批量更新系统用户
	XTYH_ADDLIST("xtyh_bad"), //批量新增系统用户
	YHQT_ADD("yhqt_add"), //新增用户其他信息
	JGQX_ADD("jgqx_add"), //新增用户机构权限
	GWCY_ADD("gwcy_add"), //新增审批岗位成员
	GWCY_DEL("gwcy_del"), //删除审批岗位成员
	SSJG_ADD("ssjg_add"), //用户所属机构新增
	SSJG_MOD("ssjg_mod"), //用户所属机构修改
	YHJS_DEL("yhjs_del"), // 删除用户角色
	YHJS_ADD("yhjs_add"), //新增用户角色
	HMC_ADD("yhmc_add"), //新增花名册信息
	HMC_MOD("yhmc_mod"), //修改花名册信息
	HMC_PRO("yhmc_pro"), //处理花名册到期提醒
	YHT_ADD("yght_add"), //新增员工合同信息
	YHT_DEL("yght_del"), //删除员工合同信息
	YLZ_ADD("yglz_add"), //新增员工离职信息
	YLZ_DEL("yglz_del"), //删除员工离职信息

	CSXX_CSS("csxx_css"), //测试信息

	SHZT_MOD("shzt_mod"),//新冠审核状态修改
	CKQX_MOD("ckqx_mod"),//仓库权限修改
	HZXX_ADD("hzxx_add"),//增加患者信息
	HZXX_MOD("hzxx_mod"),//修改患者信息
	HZXX_DEL("hzxx_del"),//删除患者信息
	FZXX_ADD("fzxx_add"),//增加分子检测信息 新冠
	FZXX_MOD("fzxx_mod"),//修改分子检测信息 新冠
	FZXX_DEL("fzxx_del"),//删除分子检测信息 新冠
	FZXX_DIS("FZXX_DIS"),//废除分子检测信息 新冠
	FZXM_ADD("fzxm_add"),//增加分子检测项目 新冠
	FZXM_MOD("fzxm_mod"),//修改分子检测项目 新冠
	FZXM_DEL("fzxm_del"),//删除分子检测项目 新冠
	FZJG_MOD("fzjg_mod"),//修改分子检测结果 新冠
	FZJG_DEL("fzjg_del"),//删除分子检测结果 新冠
	FZJC_EDI("edit_edi"),
	HZXX_UPD("hzxx_upd"),//更新患者信息
	FZXX_CON("fzxx_con"),//确认分子检测信息
	YYXX_ADD("yyxx_add"),//增加患者信息、新增分子检测信息、新增分子检测项目
	YYXX_MOD("yyxx_mod"),//修改患者信息、新增分子检测信息、新增分子检测项目
	FKBJ_MOD("fkbj_mod"),//付款标记修改
	KFP_FAIL("kfp_fail"),//发票失败
	YYXX_UPD("yyxx_upd"),//修改患者信息、修改分子检测信息、删除分子检测项目、新增分子检测项目
	YYXX_DEL("yyxx_del"),//删除分子检测信息
	WXBD_MOD("wxbd_mod"), // 微信绑定修改
	WXBD_ADD("wxbd_add"), // 微信绑定新增
	FZXX_HQR("FZXX_HQR"),//混检确认信息
	YYRQ_ADD("yyrq_add"),//不可预约日期新增
	YYRQ_MOD("yyrq_mod"),//不可预约日期修改
	YYRQ_DEL("yyrq_del"),//不可预约日期删除
	AHYY_UPD("AHYY_UPD"),//阿里健康修改
	GHYY_UPD("GHYY_UPD"),//橄榄枝健康修改
	ALDD_UPD("ALDD_UPD"),//阿里健康订单信息修改
	SFLR_SUB("sflr_sub"),//身份录入，预约信息的保存
	YYJC_MOD("yyjc_mod"),//分子检测数据修改
	XGFK_MOD("xgfk_mod"),//分子检测支付数据修改
	FZXX_BAC("fzxx_bac"),//回滚分子检测数据
	BGWC_UPD("BGWC_UPD"),//更新报告完成日期
	FZXX_REP("FZXX_REP"),//生成新冠报告

	//消息对应
	XXDY_ADD("xxdy_add"),//新增信息对应
	XXDY_MOD("xxdy_mod"),//修改信息对应
	XXDY_DEL("xxdy_del"),//删除信息对应

	//普检
	PJXX_ADD("pjxx_add"),//普检新增
	PJXX_MOD("pjxx_mod"),//普检修改
	PJXX_DEL("pjxx_del"),//普检删除
	PJXX_DET("pjxx_det"),//普检实验
	PJXX_ACT("pjxx_act"),//普检接收
	PJXX_REP("pjxx_rep"),//普检报告
	PJXX_AUT("pjxx_aut"),//普检审核
	PJXX_RUM("pjxx_rum"),//普检结果修改
	PJXX_AUB("pjxx_aub"),//审核同步报告信息


	WEPJ_EDI("wepj_edi"),//普检修改
	WEPJ_DEL("wepj_del"),//普检删除

	YFJE_MODLIST("yfje_bmd"),//批量更新检测项目应付金额
	FKJE_MODLIST("fkje_bmd"),//批量更新送检信息付款金额
	LALCOL_SJADD("local_sjadd"),//本地化送检信息接收保存，中山二院等

	HBXXZ_ADD("hxxz_add"),//新增伙伴x限制信息
	HBXXZ_MOD("hxxz_mod"),//修改伙伴x限制信息
	HBXXZ_DEL("hxxz_del"),//删除伙伴x限制信息
	;

	private final String code;
	
	RabbitEnum(String code) {
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
}