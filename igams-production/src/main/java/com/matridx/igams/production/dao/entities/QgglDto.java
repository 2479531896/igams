package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;


@Alias(value="QgglDto")
public class QgglDto extends QgglModel{
	//期望到货日期
	private String qwrq;
	//计量单位
	private String jldw;
	//保存条件
	private String bctj;
	//规格
	private String gg;
	//是否危险品
	private String sfwxp;
	//生产商
	private String scs;
	//物料子类别名称
	private String wlzlbmc; 
	//物料质量类别名称
	private String lbmc;	
	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//支付方式名称
	private String zffsmc;
	//付款方名称
	private String fkfmc;
	//录入人员名称
	private String lrrymc;
	//支付方式[多]
	private String[] zffss;
	//付款方[多]
	private String[] fkfs;
	//状态[多]
	private String[] zts;
	//申请日期开始时间
	private String sqrqstart;
	//领料类别
	private String lllb;
	//申请日期结束时间
	private String sqrqend;
	//部门代码(U8数据库保存此字段)
	private String sqbmdm;
	//项目编码名称
	private String xmbmmc;
	//项目大类名称
	private String xmdlmc;
	//项目编码[多]
	private String[] xmbms;
	//项目大类[多]
	private String[] xmdls;
	//采购明细list
	private List<QgmxDto> qgmxlist;
	//采购明细JSON数据
	private String qgmx_json;
	//项目名称
	private String xmmc;
	//项目编码代码
	private String xmbmdm;
	//项目大类代码
	private String xmdldm;
	//申请人钉钉ID
	private String ddid;
	//访问标记(判断是否为外部接口)
	private String fwbj;
	//钉钉审批人用户id
	private String sprid;
	//钉钉审批人用户姓名
	private String sprxm;
	//钉钉审批人钉钉ID
	private String sprddid;
	//钉钉审批人用户名
	private String spryhm;
	//钉钉审批人角色ID
	private String sprjsid;
	//钉钉审批人角色名称
	private String sprjsmc;
	//附件IDS
	private List<String> fjids;
	//业务ID
	private String ywlx;
	//物料ID，用于页面传递
	private String wlid;
	//保存标记,用于跳过后台验证
	private String bcbj;
	//请购明细ID
	private String qgmxid;
	//查看标记,用于附件查看页面控制显示内容
	private String ckbj;
	//审核人标记，用于判断是否更新U8审核人信息
	private String shrgxbj;
	//更新标记，判断审核回调是否需要更新本地以及U8数据(请购审核调用审核查看页面不需要更新)
	private String gxbj;
	//用于判断是修改还是复制操作
	private String flg;
	//选中的物料序号(用于采购相同物料进行区分)
	private String index;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//物料类别名称
	private String wllbmc;
	//数量
	private String sl;
	//价格
	private String jg;
	//明细备注
	private String mxbz;
	//原厂货号
	private String ychh;
	//机构代号(生成订单号)
	private String jgdh;
	//合同数量
	private String htsl;
	//合同id
	private String htid;
	//请购取消ID
	private String qgqxid;
	//rabbit同步标记
	private String prefixFlg;
	
	//压缩附件IDs
	private List<String> ysfjids;
	//请购类型代码
	private String qglbdm;
	//请购类型名称
	private String qglbmc;
	//请购类别s
	private String[] qglbs;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//日期s
	private String[] rqs;
	//所属研发项目名称
	private String ssyfxmmc;
	//是否提供发票名称
	private String sftgfpmc;
	//全部查询内容
	private String entire;
	//是否是全部请购列表标记 1：全部请购列表 0：请购列表qbqgflg
	private String qbqgflg;
	//是否报销s
	private String[] sfbxs;
	//请购类别扩展参数1
	private String cskz1;
	//计划到货日期
	private String jhdhrq;
	//原计划到货日期
	private String yjhdhrq;
	//货物名称
	private String hwmc;
	//货物标准
	private String hwbz;
	//物料列表标记1:采购车   0:采购新增
	private String wlflag;
	//审核通过时间
	private String shtgsj;
	//提交时间
	private String tjsj;
	//部门经理审核时间
	private String bmjl;
	//采购管理员审核时间
	private String cggl;
	//公司领导审核时间
	private String gsld;
	//新增到提交用时
	private String xzdtj;
	//采购部经理审核时间
	private String cgjl;
	//提交到部门经理审核用时
	private String tjdbmjl;
	//部门经理到采购管理员审核用时
	private String jldcggl;
	//采购管理员到采购部经理审核用时
	private String cggldcgjl;
	//采购部经理到公司领导审核用时
	private String cgjldgsld;
	//审核通过时间
	private String tjdtg;
	//合同新增标记 0普通新增
	private String htaddflag;
	//请购单数
	private String qgds;
	//总数
	private String zs;
	//路径标记
	private String ljbj;
	//类型
	private String lx;
	//合同内部编号
	private String htnbbh;
	//总金额
	private String zje;
	//负责人名称
	private String fzrmc;
	//供应商名称
	private String gysmc;
	//付款dh
	private String fkdh;
	//付款总金额
	private String fkzje;
	//支付对象名称
	private String zfdxmc;
	//合同付款id
	private String htfkid;
	//发票号
	private String fph;
	//开票金额
	private String kpje;
	//发票id
	private String fpid;
	//到货单号
	private String dhdh;
	//到货id
	private String dhid;
	//入库类别名称
	private String rklbmc;
	//检验单号/借用单号
	private String jydh;
	//检验结果名称
	private String jyjgmc;
	//到货检验id
	private String dhjyid;
	//入库单号
	private String rkdh;
	//入库id
	private String rkid;
	//领料单号
	private String lldh;
	//领料id
	private String llid;
	//客户简称
	private String khjc;
	//借出借用id
	private String jcjyid;
	//oa销售单号
	private String oaxsdh;
	//销售总金额
	private String xszje;
	//销售id
	private String xsid;
	//发货单号
	private String fhdh;
	//收货地址
	private String shdz;
	//经手人名称
	private String jsrmc;
	//发货id
	private String fhid;
	//需求单号
	private String xqdh;
	//产品需求id
	private String cpxqid;
	//指令单号
	private String zldh;
	//序列号
	private String xlh;
	//生产指令明细id
	private String sczlmxid;
	//确认单号
	private String qrdh;
	//付款金额
	private String fkje;
	//入库人员名称
	private String rkrymc;
	//库位名称
	private String kwmc;
	//发料人名称
	private String flrymc;
	//
	List<QgmxDto> qgmxDtos;
	//
	Map<String,Object> map;
	//法务审核时间
	private String fwsh;
	//公司领导审核时间
	private String htgsld;
	//会计审核时间
	private String kjsh;
	private String htxzdtj;
	private String httjdcgjl;
	private String fwspdgsld;
	private String tjfshtg;
	private String httjfshtg;
	private String gslddhj;
	private String htcgjl;
	private String qgtjdhtshtg;
	private String cgjldfwsp;
	private String tjdshtg;
	private String htlrsj;
	private String httjsj;
	private String httjdshtg;
	//入库状态多
	private String[] rkzts;
	//入库数量
	private String rksl;
	//部门经理到采购部经理
	private String bmjldcgbjl;
	private String sfcs;//是否超时
	private String sqryhm;//申请人用户名
	//产品注册号
	private String cpzch;
	private String scmx_json;
	private String gdzcbh;//固定资产编号
	private String sbbh;//设备编号
	private String sbccbh;//设备出场编号
	
	//确认id
	private String qrid;
	//行政付款id
	private String fkid;
	//行政入库id
	private String xzrkid;
	//行政领料id
	private String xzllid;
	//业务员名称
	private String ywymc;
	//联系人
	private String lxr;

	private String fgld;//分管领导

	private String fglddcggl;//分管领导到采购管理

	private String jldfgld;//经理到分管领导
	private String jgfwmc;
	private String shys;
	private String postStart;
	private String postEnd;
	private String timeLag;
	private String postCg;
	private String overTime;
	private String overdueTime;
	private String rule;
	private String unit;
	private String cszt;
	private String title;
	private String csbz;
	public String getCszt() {
		return cszt;
	}

	public void setCszt(String cszt) {
		this.cszt = cszt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCsbz() {
		return csbz;
	}

	public void setCsbz(String csbz) {
		this.csbz = csbz;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(String overdueTime) {
		this.overdueTime = overdueTime;
	}

	public String getPostCg() {
		return postCg;
	}

	public void setPostCg(String postCg) {
		this.postCg = postCg;
	}

	public String getPostStart() {
		return postStart;
	}

	public void setPostStart(String postStart) {
		this.postStart = postStart;
	}

	public String getPostEnd() {
		return postEnd;
	}

	public void setPostEnd(String postEnd) {
		this.postEnd = postEnd;
	}

	public String getTimeLag() {
		return timeLag;
	}

	public void setTimeLag(String timeLag) {
		this.timeLag = timeLag;
	}

	public String getShys() {
		return shys;
	}

	public void setShys(String shys) {
		this.shys = shys;
	}

	public String getJgfwmc() {
		return jgfwmc;
	}

	public void setJgfwmc(String jgfwmc) {
		this.jgfwmc = jgfwmc;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getSbccbh() {
		return sbccbh;
	}

	public void setSbccbh(String sbccbh) {
		this.sbccbh = sbccbh;
	}

	public String getScmx_json() {
		return scmx_json;
	}

	public void setScmx_json(String scmx_json) {
		this.scmx_json = scmx_json;
	}

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}

	public String getSqryhm() {
		return sqryhm;
	}

	public void setSqryhm(String sqryhm) {
		this.sqryhm = sqryhm;
	}

	public String getSfcs() {
		return sfcs;
	}

	public void setSfcs(String sfcs) {
		this.sfcs = sfcs;
	}

	public String getBmjldcgbjl() {
		return bmjldcgbjl;
	}

	public void setBmjldcgbjl(String bmjldcgbjl) {
		this.bmjldcgbjl = bmjldcgbjl;
	}
	public String getRksl() {
		return rksl;
	}

	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String[] getRkzts() {
		return rkzts;
	}

	public void setRkzts(String[] rkzts) {
		this.rkzts = rkzts;
	}
	public String getHttjdshtg() {
		return httjdshtg;
	}

	public void setHttjdshtg(String httjdshtg) {
		this.httjdshtg = httjdshtg;
	}

	public String getHtlrsj() {
		return htlrsj;
	}

	public void setHtlrsj(String htlrsj) {
		this.htlrsj = htlrsj;
	}

	public String getHttjsj() {
		return httjsj;
	}

	public void setHttjsj(String httjsj) {
		this.httjsj = httjsj;
	}

	public String getTjdshtg() {
		return tjdshtg;
	}

	public void setTjdshtg(String tjdshtg) {
		this.tjdshtg = tjdshtg;
	}

	public String getFwsh() {
		return fwsh;
	}

	public void setFwsh(String fwsh) {
		this.fwsh = fwsh;
	}

	public String getHtgsld() {
		return htgsld;
	}

	public void setHtgsld(String htgsld) {
		this.htgsld = htgsld;
	}

	public String getKjsh() {
		return kjsh;
	}

	public void setKjsh(String kjsh) {
		this.kjsh = kjsh;
	}

	public String getHtxzdtj() {
		return htxzdtj;
	}

	public void setHtxzdtj(String htxzdtj) {
		this.htxzdtj = htxzdtj;
	}

	public String getHttjdcgjl() {
		return httjdcgjl;
	}

	public void setHttjdcgjl(String httjdcgjl) {
		this.httjdcgjl = httjdcgjl;
	}

	public String getFwspdgsld() {
		return fwspdgsld;
	}

	public void setFwspdgsld(String fwspdgsld) {
		this.fwspdgsld = fwspdgsld;
	}

	public String getTjfshtg() {
		return tjfshtg;
	}

	public void setTjfshtg(String tjfshtg) {
		this.tjfshtg = tjfshtg;
	}

	public String getHttjfshtg() {
		return httjfshtg;
	}

	public void setHttjfshtg(String httjfshtg) {
		this.httjfshtg = httjfshtg;
	}

	public String getGslddhj() {
		return gslddhj;
	}

	public void setGslddhj(String gslddhj) {
		this.gslddhj = gslddhj;
	}

	public String getHtcgjl() {
		return htcgjl;
	}

	public void setHtcgjl(String htcgjl) {
		this.htcgjl = htcgjl;
	}

	public String getQgtjdhtshtg() {
		return qgtjdhtshtg;
	}

	public void setQgtjdhtshtg(String qgtjdhtshtg) {
		this.qgtjdhtshtg = qgtjdhtshtg;
	}

	public String getCgjldfwsp() {
		return cgjldfwsp;
	}

	public void setCgjldfwsp(String cgjldfwsp) {
		this.cgjldfwsp = cgjldfwsp;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<QgmxDto> getQgmxDtos() {
		return qgmxDtos;
	}

	public void setQgmxDtos(List<QgmxDto> qgmxDtos) {
		this.qgmxDtos = qgmxDtos;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getFkdh() {
		return fkdh;
	}

	public void setFkdh(String fkdh) {
		this.fkdh = fkdh;
	}

	public String getFkzje() {
		return fkzje;
	}

	public void setFkzje(String fkzje) {
		this.fkzje = fkzje;
	}

	public String getZfdxmc() {
		return zfdxmc;
	}

	public void setZfdxmc(String zfdxmc) {
		this.zfdxmc = zfdxmc;
	}

	public String getHtfkid() {
		return htfkid;
	}

	public void setHtfkid(String htfkid) {
		this.htfkid = htfkid;
	}

	public String getFph() {
		return fph;
	}

	public void setFph(String fph) {
		this.fph = fph;
	}

	public String getKpje() {
		return kpje;
	}

	public void setKpje(String kpje) {
		this.kpje = kpje;
	}

	public String getFpid() {
		return fpid;
	}

	public void setFpid(String fpid) {
		this.fpid = fpid;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getDhid() {
		return dhid;
	}

	public void setDhid(String dhid) {
		this.dhid = dhid;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}

	public String getJyjgmc() {
		return jyjgmc;
	}

	public void setJyjgmc(String jyjgmc) {
		this.jyjgmc = jyjgmc;
	}

	public String getDhjyid() {
		return dhjyid;
	}

	public void setDhjyid(String dhjyid) {
		this.dhjyid = dhjyid;
	}

	public String getRkdh() {
		return rkdh;
	}

	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}

	public String getRkid() {
		return rkid;
	}

	public void setRkid(String rkid) {
		this.rkid = rkid;
	}

	public String getLldh() {
		return lldh;
	}

	public void setLldh(String lldh) {
		this.lldh = lldh;
	}

	public String getLlid() {
		return llid;
	}

	public void setLlid(String llid) {
		this.llid = llid;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getJcjyid() {
		return jcjyid;
	}

	public void setJcjyid(String jcjyid) {
		this.jcjyid = jcjyid;
	}

	public String getOaxsdh() {
		return oaxsdh;
	}

	public void setOaxsdh(String oaxsdh) {
		this.oaxsdh = oaxsdh;
	}

	public String getXszje() {
		return xszje;
	}

	public void setXszje(String xszje) {
		this.xszje = xszje;
	}

	public String getXsid() {
		return xsid;
	}

	public void setXsid(String xsid) {
		this.xsid = xsid;
	}

	public String getFhdh() {
		return fhdh;
	}

	public void setFhdh(String fhdh) {
		this.fhdh = fhdh;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getJsrmc() {
		return jsrmc;
	}

	public void setJsrmc(String jsrmc) {
		this.jsrmc = jsrmc;
	}

	public String getFhid() {
		return fhid;
	}

	public void setFhid(String fhid) {
		this.fhid = fhid;
	}

	public String getXqdh() {
		return xqdh;
	}

	public void setXqdh(String xqdh) {
		this.xqdh = xqdh;
	}

	public String getCpxqid() {
		return cpxqid;
	}

	public void setCpxqid(String cpxqid) {
		this.cpxqid = cpxqid;
	}

	public String getZldh() {
		return zldh;
	}

	public void setZldh(String zldh) {
		this.zldh = zldh;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getSczlmxid() {
		return sczlmxid;
	}

	public void setSczlmxid(String sczlmxid) {
		this.sczlmxid = sczlmxid;
	}

	public String getQrdh() {
		return qrdh;
	}

	public void setQrdh(String qrdh) {
		this.qrdh = qrdh;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getRkrymc() {
		return rkrymc;
	}

	public void setRkrymc(String rkrymc) {
		this.rkrymc = rkrymc;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}

	public String getFlrymc() {
		return flrymc;
	}

	public void setFlrymc(String flrymc) {
		this.flrymc = flrymc;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}

	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getQgds() {
		return qgds;
	}

	public void setQgds(String qgds) {
		this.qgds = qgds;
	}

	public String getHtaddflag() {
		return htaddflag;
	}

	public void setHtaddflag(String htaddflag) {
		this.htaddflag = htaddflag;
	}

	public String getTjdtg() {
		return tjdtg;
	}

	public void setTjdtg(String tjdtg) {
		this.tjdtg = tjdtg;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}

	public String getBmjl() {
		return bmjl;
	}

	public void setBmjl(String bmjl) {
		this.bmjl = bmjl;
	}

	public String getCggl() {
		return cggl;
	}

	public void setCggl(String cggl) {
		this.cggl = cggl;
	}

	public String getGsld() {
		return gsld;
	}

	public void setGsld(String gsld) {
		this.gsld = gsld;
	}

	public String getXzdtj() {
		return xzdtj;
	}

	public void setXzdtj(String xzdtj) {
		this.xzdtj = xzdtj;
	}

	public String getCgjl() {
		return cgjl;
	}

	public void setCgjl(String cgjl) {
		this.cgjl = cgjl;
	}

	public String getTjdbmjl() {
		return tjdbmjl;
	}

	public void setTjdbmjl(String tjdbmjl) {
		this.tjdbmjl = tjdbmjl;
	}

	public String getJldcggl() {
		return jldcggl;
	}

	public void setJldcggl(String jldcggl) {
		this.jldcggl = jldcggl;
	}

	public String getCggldcgjl() {
		return cggldcgjl;
	}

	public void setCggldcgjl(String cggldcgjl) {
		this.cggldcgjl = cggldcgjl;
	}

	public String getCgjldgsld() {
		return cgjldgsld;
	}

	public void setCgjldgsld(String cgjldgsld) {
		this.cgjldgsld = cgjldgsld;
	}


	public String getShtgsj() {
		return shtgsj;
	}

	public void setShtgsj(String shtgsj) {
		this.shtgsj = shtgsj;
	}

	public String getWlflag() {
		return wlflag;
	}

	public void setWlflag(String wlflag) {
		this.wlflag = wlflag;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}
	public String getJhdhrq() {
		return jhdhrq;
	}

	public void setJhdhrq(String jhdhrq) {
		this.jhdhrq = jhdhrq;
	}

	public String getYjhdhrq() {
		return yjhdhrq;
	}

	public void setYjhdhrq(String yjhdhrq) {
		this.yjhdhrq = yjhdhrq;
	}

	public String[] getSfbxs() {
		return sfbxs;
	}

	public void setSfbxs(String[] sfbxs) {
		this.sfbxs = sfbxs;
		for(int i=0;i<this.sfbxs.length;i++)
		{
			this.sfbxs[i] = this.sfbxs[i].replace("'", "");
		}
	}
	public String getQbqgflg() {
		return qbqgflg;
	}
	public void setQbqgflg(String qbqgflg) {
		this.qbqgflg = qbqgflg;
	}
	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getSftgfpmc() {
		return sftgfpmc;
	}

	public void setSftgfpmc(String sftgfpmc) {
		this.sftgfpmc = sftgfpmc;
	}

	public String getSsyfxmmc() {
		return ssyfxmmc;
	}

	public void setSsyfxmmc(String ssyfxmmc) {
		this.ssyfxmmc = ssyfxmmc;
	}

	public String[] getRqs() {
		return rqs;
	}

	public void setRqs(String[] rqs) {
		this.rqs = rqs;
		for(int i=0;i<this.rqs.length;i++)
		{
			this.rqs[i] = this.rqs[i].replace("'", "");
		}
	}

	public String getLllb() {
		return lllb;
	}

	public void setLllb(String lllb) {
		this.lllb = lllb;
	}

	public String getWlzlbtcmc() {
		return wlzlbtcmc;
	}

	public void setWlzlbtcmc(String wlzlbtcmc) {
		this.wlzlbtcmc = wlzlbtcmc;
	}

	public String[] getQglbs() {
		return qglbs;
	}

	public void setQglbs(String[] qglbs) {
		this.qglbs = qglbs;
		for(int i=0;i<this.qglbs.length;i++)
		{
			this.qglbs[i] = this.qglbs[i].replace("'", "");
		}
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}
	public List<String> getYsfjids() {
		return ysfjids;
	}
	public void setYsfjids(List<String> ysfjids) {
		this.ysfjids = ysfjids;
	}

	public String getQgqxid() {
		return qgqxid;
	}

	public void setQgqxid(String qgqxid) {
		this.qgqxid = qgqxid;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getHtsl() {
		return htsl;
	}

	public void setHtsl(String htsl) {
		this.htsl = htsl;
	}

	public String getJgdh() {
		return jgdh;
	}

	public void setJgdh(String jgdh) {
		this.jgdh = jgdh;
	}

	public String getQwrq() {
		return qwrq;
	}

	public void setQwrq(String qwrq) {
		this.qwrq = qwrq;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getSfwxp() {
		return sfwxp;
	}

	public void setSfwxp(String sfwxp) {
		this.sfwxp = sfwxp;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
	}

	public String getWlzlbmc() {
		return wlzlbmc;
	}

	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getMxbz() {
		return mxbz;
	}

	public void setMxbz(String mxbz) {
		this.mxbz = mxbz;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getGxbj() {
		return gxbj;
	}

	public void setGxbj(String gxbj) {
		this.gxbj = gxbj;
	}

	public String getShrgxbj() {
		return shrgxbj;
	}

	public void setShrgxbj(String shrgxbj) {
		this.shrgxbj = shrgxbj;
	}

	public String getCkbj() {
		return ckbj;
	}

	public void setCkbj(String ckbj) {
		this.ckbj = ckbj;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getBcbj() {
		return bcbj;
	}

	public void setBcbj(String bcbj) {
		this.bcbj = bcbj;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
	}

	public String getSprid() {
		return sprid;
	}

	public void setSprid(String sprid) {
		this.sprid = sprid;
	}

	public String getSprxm() {
		return sprxm;
	}

	public void setSprxm(String sprxm) {
		this.sprxm = sprxm;
	}

	public String getSprddid() {
		return sprddid;
	}

	public void setSprddid(String sprddid) {
		this.sprddid = sprddid;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getXmbmdm() {
		return xmbmdm;
	}

	public void setXmbmdm(String xmbmdm) {
		this.xmbmdm = xmbmdm;
	}

	public String getXmdldm() {
		return xmdldm;
	}

	public void setXmdldm(String xmdldm) {
		this.xmdldm = xmdldm;
	}

	public String getSqbmdm() {
		return sqbmdm;
	}

	public void setSqbmdm(String sqbmdm) {
		this.sqbmdm = sqbmdm;
	}
 		public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getZffsmc() {
		return zffsmc;
	}

	public void setZffsmc(String zffsmc) {
		this.zffsmc = zffsmc;
	}

	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}
	
	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String[] getZffss() {
		return zffss;
	}

	public void setZffss(String[] zffss) {
		this.zffss = zffss;
		for(int i=0;i<this.zffss.length;i++)
		{
			this.zffss[i] = this.zffss[i].replace("'", "");
		}
	}

	public String[] getFkfs() {
		return fkfs;
	}

	public void setFkfs(String[] fkfs) {
		this.fkfs = fkfs;
		for(int i=0;i<this.fkfs.length;i++)
		{
			this.fkfs[i] = this.fkfs[i].replace("'", "");
		}
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++)
		{
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public String getSqrqstart() {
		return sqrqstart;
	}

	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}

	public String getSqrqend() {
		return sqrqend;
	}

	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}


	public String getXmbmmc() {
		return xmbmmc;
	}

	public void setXmbmmc(String xmbmmc) {
		this.xmbmmc = xmbmmc;
	}

	public String getXmdlmc() {
		return xmdlmc;
	}

	public void setXmdlmc(String xmdlmc) {
		this.xmdlmc = xmdlmc;
	}

	public String[] getXmbms() {
		return xmbms;
	}

	public void setXmbms(String[] xmbms) {
		this.xmbms = xmbms;
		for(int i=0;i<this.xmbms.length;i++)
		{
			this.xmbms[i] = this.xmbms[i].replace("'", "");
		}
	}

	public String[] getXmdls() {
		return xmdls;
	}

	public void setXmdls(String[] xmdls) {
		this.xmdls = xmdls;
		for(int i=0;i<this.xmdls.length;i++)
		{
			this.xmdls[i] = this.xmdls[i].replace("'", "");
		}
	}


	public List<QgmxDto> getQgmxlist() {
		return qgmxlist;
	}

	public void setQgmxlist(List<QgmxDto> qgmxlist) {
		this.qgmxlist = qgmxlist;
	}

	public String getQgmx_json() {
		return qgmx_json;
	}

	public void setQgmx_json(String qgmx_json) {
		this.qgmx_json = qgmx_json;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getYwymc() {
		return ywymc;
	}

	public void setYwymc(String ywymc) {
		this.ywymc = ywymc;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}
	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}

	public String getFkid() {
		return fkid;
	}

	public void setFkid(String fkid) {
		this.fkid = fkid;
	}

	public String getXzrkid() {
		return xzrkid;
	}

	public void setXzrkid(String xzrkid) {
		this.xzrkid = xzrkid;
	}

	public String getXzllid() {
		return xzllid;
	}

	public void setXzllid(String xzllid) {
		this.xzllid = xzllid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getJldfgld() {
		return jldfgld;
	}

	public void setJldfgld(String jldfgld) {
		this.jldfgld = jldfgld;
	}

	public String getFglddcggl() {
		return fglddcggl;
	}

	public void setFglddcggl(String fglddcggl) {
		this.fglddcggl = fglddcggl;
	}

	public String getFgld() {
		return fgld;
	}

	public void setFgld(String fgld) {
		this.fgld = fgld;
	}
}
