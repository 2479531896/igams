package com.matridx.igams.common.enums;
/**
 * 页面显示设置枚举类
 * 
 * @author shunl
 * @time 20170605
 */
public enum DisplaySettingEnum {
	/**项目类别*/
	AWARD_XMLB("AWARD","AWARD_XMLB",ValidateTypeEnum.SHOW.getType(),true),
	/**是否获奖*/
	AWARD_SFHJ("AWARD","AWARD_SFHJ",ValidateTypeEnum.SHOW.getType(),true),
	/**所室代码*/
	AWARD_SSDM("AWARD","AWARD_SSDM",ValidateTypeEnum.SHOW.getType(),true),
	/**具体部门/省*/
	AWARD_JTBMS("AWARD","AWARD_JTBMS",ValidateTypeEnum.SHOW.getType(),true),
	/**是否推荐*/
	AWARD_SFTJ("AWARD","AWARD_SFTJ",ValidateTypeEnum.SHOW.getType(),true),
	/**推荐日期*/
	AWARD_TJRQ("AWARD","AWARD_TJRQ",ValidateTypeEnum.SHOW.getType(),true),
	/**奖金*/
	AWARD_JJ("AWARD","AWARD_JJ",ValidateTypeEnum.SHOW.getType(),true),
	/**推荐单位*/
	AWARD_TJDW("AWARD","AWARD_TJDW",ValidateTypeEnum.SHOW.getType(),true),
	/**奖励文档*/
	AWARD_DOC_ATTACH("AWARD","AWARD_DOC_ATTACH",ValidateTypeEnum.SHOW.getType(),true),
	/**项目其他信息*/
	AWARD_QTXX("AWARD","AWARD_QTXX",ValidateTypeEnum.SHOW.getType(),true),
	/**获奖类型*/
	AWARD_HJLX("AWARD","AWARD_HJLX",ValidateTypeEnum.REQUIRED.getType(),false),
	/**论文收录添加按钮**/
	THESIS_INCLUDE_ADDBTN("THESIS_INCLUDE","THESIS_INCLUDE_ADDBTN",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别进入国家日期**/
	PATENT_SL_JRGJRQ("PATENT_SL","PATENT_SL_JRGJRQ",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别分案原申请号**/
	PATENT_SL_FAYSQH("PATENT_SL","PATENT_SL_FAYSQH",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别优先日**/
	PATENT_SL_YXR("PATENT_SL","PATENT_SL_YXR",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别优先权项**/
	PATENT_SL_YXQX("PATENT_SL","PATENT_SL_YXQX",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别主分类号**/
	PATENT_SQ_ZFLH("PATENT_SQ","PATENT_SQ_ZFLH",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别授权机构**/
	PATENT_SQ_SQJG("PATENT_SQ","PATENT_SQ_SQJG",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别专利证书号**/
	PATENT_SQ_ZLZSH("PATENT_SQ","PATENT_SQ_ZLZSH",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别主权项**/
	PATENT_SQ_ZQX("PATENT_SQ","PATENT_SQ_ZQX",ValidateTypeEnum.SHOW.getType(),true),
	/**专利国别审定授权说明书**/
	PATENT_SQ_SDSQSMS("PATENT_SQ","PATENT_SQ_SDSQSMS",ValidateTypeEnum.SHOW.getType(),true),
	/**著作出版期刊**/
	BOOK_CBQK("BOOK","BOOK_CBQK",ValidateTypeEnum.SHOW.getType(),true),
	/**著作全代理机构*/
	COPYRIGHT_DLJG("COPYRIGHT","COPYRIGHT_DLJG",ValidateTypeEnum.REQUIRED.getType(),true),
	/**著作权授权主权项*/
	COPYRIGHT_SQ_ZQX("COPYRIGHT_SQ","COPYRIGHT_SQ_ZQX",ValidateTypeEnum.SHOW.getType(),true),
	/**著作权授权证书号*/
	COPYRIGHT_SQ_ZSH("COPYRIGHT_SQ","COPYRIGHT_SQ_ZSH",ValidateTypeEnum.SHOW.getType(),true),
	/**论文维护页面**/
	/**语种**/
	THESIS_YZ("THESIS","THESIS_YZ",ValidateTypeEnum.SHOW.getType(),true),
	/**论文类型**/
	THESIS_LWLX("THESIS","THESIS_LWLX",ValidateTypeEnum.SHOW.getType(),true),
	/**上传DOI认证画面**/
	THESIS_DOIRZ("THESIS","THESIS_DOIRZ",ValidateTypeEnum.SHOW.getType(),true),
	/**出版年号**/
	THESIS_CBNH("THESIS","THESIS_CBNH",ValidateTypeEnum.SHOW.getType(),true),
	/**年号**/
	THESIS_NH("THESIS","THESIS_NH",ValidateTypeEnum.SHOW.getType(),true),
	/**总期号**/
	THESIS_ZQH("THESIS","THESIS_ZQH",ValidateTypeEnum.SHOW.getType(),true),
	/**收录信息**/
	THESIS_SLXX("THESIS","THESIS_SLXX",ValidateTypeEnum.SHOW.getType(),true),
	/**资助页面发票扫描件是否显示**/
	PATENT_AID_FPSMJ("PATENT_AID","PATENT_AID_FPSMJ",ValidateTypeEnum.SHOW.getType(),true),
	/**专利其他信息-专利缴费*/
	PATENT_QTXX_ZLJF("PATENT_QTXX","PATENT_QTXX_ZLJF",ValidateTypeEnum.SHOW.getType(),true),
	/**专利其他信息-专利奖励*/
	PATENT_QTXX_ZLJL("PATENT_QTXX","PATENT_QTXX_ZLJL",ValidateTypeEnum.SHOW.getType(),true),
	/**专利受理受理通知书*/
	PATENT_SL_SLTZS("PATENT_SL","PATENT_SL_SLTZS",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权申报-是否可转让*/
	IPRAPPLY_SFKZR("IPRAPPLY","IPRAPPLY_SFKZR",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权申报-建议转让金额*/
	IPRAPPLY_JYZRJE("IPRAPPLY","IPRAPPLY_JYZRJE",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权-子版合同*/
	IPRAPPLY_ZBHT("IPRAPPLY","IPRAPPLY_ZBHT",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权-是否PCT*/
	IPRAPPLY_SFPCT("IPRAPPLY","IPRAPPLY_SFPCT",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受理-进入国家日期*/
	IPR_SL_JRGJRQ("IPR_SL","IPR_SL_JRGJRQ",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受理-分案原申请号*/
	IPR_SL_FAYSQH("IPR_SL","IPR_SL_FAYSQH",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受理-优先日*/
	IPR_SL_YXR("IPR_SL","IPR_SL_YXR",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受理-优先权项*/
	IPR_SL_YXQX("IPR_SL","IPR_SL_YXQX",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受理-摘要/简介*/
	IPR_SL_JJ("IPR_SL","IPR_SL_JJ",ValidateTypeEnum.REQUIRED.getType(),true),
	/**知识产权受理-受理通知书*/
	IPR_SL_SLTZS("IPR_SL","IPR_SL_SLTZS",ValidateTypeEnum.REQUIRED.getType(),true),
	/**知识产权授权-主分类号*/
	IPR_SQ_ZFLH("IPR_SQ","IPR_SQ_ZFLH",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受权-授权机构*/
	IPR_SQ_SQJG("IPR_SQ","IPR_SQ_SQJG",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受权-证书号*/
	IPR_SQ_ZSH("IPR_SQ","IPR_SQ_ZSH",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受权-主权项*/
	IPR_SQ_ZQX("IPR_SQ","IPR_SQ_ZQX",ValidateTypeEnum.SHOW.getType(),true),
	/**知识产权受权-审定授权说明书*/
	IPR_SQ_SDSQSMS("IPR_SQ","IPR_SQ_SDSQSMS",ValidateTypeEnum.SHOW.getType(),true),
	/**论文收录页面**/
	/**论文收录页面-出版号**/
	THESIS_INCLUDE_CBH("THESIS_INCLUDE","THESIS_INCLUDE_CBH",ValidateTypeEnum.SHOW.getType(),true),
	/**论文收录页面-web链接**/
	THESIS_INCLUDE_WEBLJ("THESIS_INCLUDE","THESIS_INCLUDE_WEBLJ",ValidateTypeEnum.SHOW.getType(),true),
	/**项目申请页面-支付来源**/
	PROJECT_ZFLY("PROJECT","PROJECT_ZFLY",ValidateTypeEnum.SHOW.getType(),true),
	/**项目申请页面-预算页扫描件**/
	PROJECT_YSYSMJ("PROJECT","PROJECT_YSYSMJ",ValidateTypeEnum.SHOW.getType(),true),
	/**经费入账页面-已预借发票/票据备注说明**/
	FUNDSCARD_FPBZSM("FUNDSCARD","FUNDSCARD_FPBZSM",ValidateTypeEnum.SHOW.getType(),true),
	/**纵向项目审核页面-间接费是否建卡**/
	AUDIT_POPROJECT_JJFSFJK("AUDIT_POPROJECT","AUDIT_POPROJECT_JJFSFJK",ValidateTypeEnum.SHOW.getType(),true),
	/**纵向项目审核页面-公共资源费**/
	AUDIT_POPROJECT_GGZYF("AUDIT_POPROJECT","AUDIT_POPROJECT_GGZYF",ValidateTypeEnum.SHOW.getType(),true),
	/**纵向项目审核页面-水电费**/
	AUDIT_POPROJECT_SDF("AUDIT_POPROJECT","AUDIT_POPROJECT_SDF",ValidateTypeEnum.SHOW.getType(),true),
	/**研究所管理页面-机构类型**/
	INSTITUTE_JGLX("INSTITUTE","INSTITUTE_JGLX",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-机构子类型**/
	INSTITUTE_JGZLX("INSTITUTE","INSTITUTE_JGZLX",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-负责人**/
	INSTITUTE_FZR("INSTITUTE","INSTITUTE_FZR",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-批准部门**/
	INSTITUTE_PZBM("INSTITUTE","INSTITUTE_PZBM",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-成立时间**/
	INSTITUTE_CLSJ("INSTITUTE","INSTITUTE_CLSJ",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-研究活动类型**/
	INSTITUTE_YJHDLX("INSTITUTE","INSTITUTE_YJHDLX",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-组成方式**/
	INSTITUTE_ZCFS("INSTITUTE","INSTITUTE_ZCFS",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-学科门类**/
	INSTITUTE_XKML("INSTITUTE","INSTITUTE_XKML",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-服务的国民经济行业**/
	INSTITUTE_FWDGMJJHY("INSTITUTE","INSTITUTE_FWDGMJJHY",ValidateTypeEnum.REQUIRED.getType(),false),
	/**研究所管理页面-组成类型**/
	INSTITUTE_ZCLX("INSTITUTE","INSTITUTE_ZCLX",ValidateTypeEnum.REQUIRED.getType(),false),
	/**预借发票审核页面-开票日期**/
	PREBORROWNOTE_KPRQ("PREBORROWNOTE","PREBORROWNOTE_KPRQ",ValidateTypeEnum.SHOW.getType(),true),
	/**预借发票审核页面-票号**/
	PREBORROWNOTE_PH("PREBORROWNOTE","PREBORROWNOTE_PH",ValidateTypeEnum.SHOW.getType(),true),
	/**经费认领页面，是否跳转项目选择**/
	FUNDS_CLAIM("FUNDS_CLAIM","MENU_PRO",ValidateTypeEnum.SHOW.getType(),true),
	;
	
	private final String ywqf; //业务区分
	private final String code; //页面字段
	private final String xzlx; //限制类型
	private final boolean mrz; //默认值

	DisplaySettingEnum(String ywqf,String code,String xzlx,boolean mrz) {
		this.ywqf = ywqf;
		this.code = code;
		this.xzlx = xzlx;
		this.mrz = mrz;
	}
	
	public String getYwqf() {
		return ywqf;
	}

	public String getCode() {
		return code;
	}

	public String getXzlx() {
		return xzlx;
	}

	public boolean getMrz() {
		return mrz;
	}
	
	
}
