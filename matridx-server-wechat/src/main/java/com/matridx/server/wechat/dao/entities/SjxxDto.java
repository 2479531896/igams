package com.matridx.server.wechat.dao.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

@Alias(value="SjxxDto")
public class SjxxDto extends SjxxModel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//医生ID
	private String ysid;
	//微信ID
	private String wxid;
	//送检临床症状
	private List<String> lczzs;
	//检测项目ID复数
	private List<String> jcxmids;
	//检测项目ID复数
	private List<String> kyxmids;
	//关注病原复数
	private List<String> bys;
	//送检临床症状
	private List<SjlczzDto> sjlczzs;
	//检测项目ID复数
	private List<SjjcxmDto> sjjcxms;
	//前期检测复数
	private List<SjqqjcDto> sjqqjcs;
	//前期关注病原复数
	private List<SjgzbyDto> sjgzbys;
	//扫码验证用参数
	private String code;
	//扫码验证用参数
	private String state;
	//代表名
	private String dbm;
	//科室名称
	private String ksmc;
	//科室代码
	private String ksdm;
	//性别名称
	private String xbmc;
	//是否为新的标记
	private String newflg;
	//检测项目名称
	private String jcxmmc;
	//科研项目名称
	private String kyxmmc;
	//关注病原名称
	private String gzbymc;
	//前期检测名称
	private String qqjcmc;
	//临床症状名称
	private String lczzmc;
	//业务类型
	private String ywlx;
	//附件ID
	private String fjid;
	//附件ID复数
	private List<String> fjids;
	//查询收费的标记
	private String sfflg;
	//费用
	private String fy;
	//是否触摸（小程序）
	private String isTouchMove;
	//身份类型
	private String sflx;
	//加密sign
	private String sign;
	//开始日期
	private String jsrqstart;
	//结束日期
	private String jsrqend;
	//开始日期(月查询)
	private String jsrqMstart;
	//结束日期(月查询)
	private String jsrqMend;
	//开始日期(年查询)
	private String jsrqYstart;
	//结束日期(年查询)
	private String jsrqYend;
	//接收日期格式化
	private String fjsrq;
	//检索日期范围复数
	private List<String> rqs;
	//查询条件
	private String tj;
	//真实姓名
	private String zsxm;
	//送检单位复数
	private List<String> sjdws;
	//前期检测ID复数(小程序)
	private List<String> qqjcids;
	//检测值复数(小程序)
	private List<String> jczs;
	//统计周期
	private String tjzq;
	//参数扩展1
	private String cskz1;
	//参数扩展二
	private String cskz2;
	//参数扩展三
	private String cskz3;
	//参数扩展四
	private String cskz4;
	//参数扩展五
	private String cskz5;
	//是否正确
	private String sfzq;
	//临床反馈
	private String lcfk;
	//模糊查询所有用的参数
	private String entire;
	//查询数
	private String count;
	//开始位置
	private String start;
	//钉钉ID
	private String ddid;
	//需要删除的附件
	private List<String> dellist;
	//快递类型名称
	private String kdlxmc;
	//检测单位名称
	private String jcdwmc;
	//用于检索标本类型
	private String[] yblxs;
	//单位ID(复数)
	private String[] dwids;
	//快递类型(复数)
	private String[] kdlxs;
	//检测项目ID
	private String jcxmid;
	//采样日期(开始)
	private String cyrqstart;
	//采样日期(结束)
	private String cyrqend;
	//合作伙伴信息
	private List<SjhbxxDto> sjhbxxs;
	//录入人员list
	private List<String> lrrys;
	//检验结果
	private String[] jyjgs;
	//标本状态名称
	private String ybztmc;
	//是否接收名称
	private String sfjsmc;
	//是否统计名称
	private String sftjmc;
	//是否收费名称
	private String sfsfmc;
	//用户id
	private String yhid;
	//是否付款
	private String fkmc;
	//送检单位简称
	private String dwjc;
	//送检标本状态复数
	private List<SjybztDto> sjybzts;
	//起始页 对外接口用
	private String startpage;
	//申请起始日 对外接口用
	private String applydatestart;
	//申请结束日 对外接口用
	private String applydateend;
	//标本编号 对外接口用
	private String samplecode;
	//送检单位
	private String dwmc;
	//标本类型
	private String csmc;
	//文件类型
	private String w_ywlx;
	//Word版附件ID
	private String wordfjid;
	//Word版文件路径
	private String wordwjlj;
	//Word版文件名
	private String wordwjm;
	//附件ID复数
	private List<String> w_fjids;
	//已检项目
	private String yjxmmc;
	//检测项目扩展参数
	private String jcxmcskz;
	//所选择的字段
	private String SqlParam;
	//标本类型
	private String yblx_flg;
	//送检单位
	private String sjdwxx_flg;
	//关注病原名称
	private String gzbymc_flg;
	//前期检测名称
	private String qqjcmc_flg;
	//检测项目名称
	private String jcxmmc_flg;
	//临床症状名称
	private String lczzmc_flg;
	//付款日期
	private String fkrq;
	//文件路径
	private String wjlj;
	//文件名
	private String wjm;
	//关联用户的微信ID
	private String wechatid;
	//合作伙伴的伙伴类型
	private String hblx;
	//存放nl+nldw
	private String nls;
	//高级修改
	private String xg_flg;
	//返回状态
	private String status;
	//模板代码
	private String mbdm;
	//序号
	private int xh;
	//发送方式
	private String fsfs;
	//邮箱
	private String yx;
	//开始日期
	private String bgrqstart;
	//结束日期
	private String bgrqend;
	//开始日期(月查询)
	private String bgrqMstart;
	//结束日期(月查询)
	private String bgrqMend;
	//开始日期(年查询)
	private String bgrqYstart;
	//结束日期(年查询)
	private String bgrqYend;
	//标本来源城市
	private String lycs;
	//是否收样 0为否  1为是
	private String sfsy;
	//是否接收
	private String sfjs;
	//是否统计
	private String sftj;
	//标本状态
	private List<String> zts;
	//判断是否修改接收日期
	private String jstj;
	//合作伙伴参数扩展1
	private String hbcskz1;
	//合作伙伴参数扩展2
	private String hbcskz2;
	//合作伙伴集合
	private List<String> sjhbs;
	//用于检索送检扩展参数1
	private String[] sjkzcs1;
	//用于检索送检扩展参数2
	private String[] sjkzcs2;
	//用于检索送检扩展参数3
	private String[] sjkzcs3;
	//用于检索送检扩展参数4
	private String[] sjkzcs4;
	//检测项目（用于导出）
	private String jcxm;
	//排序
	private String px;
	//标本编号list
	private List<String> ybbhs;
	//伙伴分类
	private String hbfl;
	//伙伴子分类
	private String hbzfl;
	//周期
	private String zq;
	//代表复数
	private List<String> dbs;
	//伙伴分类复数
	private List<String> hbfls;
	//复检原因
	private String fjyy;
	//参数扩展1标记
	private String cskz1_flg;
	//参数扩展2标记
	private String cskz2_flg;
	//参数扩展3标记
	private String cskz3_flg;
	//参数扩展4标记
	private String cskz4_flg;
	//复检原因
	private String yy;
	//复检类型
	private String lx;
	//检测项目扩展参数
	private String jcxmkzcs;
	//RNA实验日期(格式为'yy-mm-dd')
	private String trsyrq;
	//DNA实验日期(格式为'yy-mm-dd')
	private String tdsyrq;
	//录入时间开始时间
	private String lrsjStart;
	//录入时间结束时间
	private String lrsjEnd;
	//报告标记
	private String bgbj;
	//检测类型
	private String jclx;
	//pdf报告类型
	private String pdflx;
	//word报告类型
	private String wordlx;
	//收费标准
	private String sfbz;
	//自定义统计参数集
	private Map<String, String> param;
	//统计条件
	private String tjtj;
	//统计字段数组 主字段
	private String[] tjzds;
	//统计字段数组 主表的字段
	private String[] mainSelects;
	//统计字段数组 主表group的字段
	private String[] mainGroups;
	//group条件 主要是去除检索字段里的AS信息，用于统合
	private String[] groupzds;
	//检索字段 主要是增加AS信息，用于显示名称
	private String[] selectzds;
	//查询条件
	private String whereParam;
	//销售分类
	private String fl;
	//销售分类
	private String zfl;
	//报告模板
	private String bgmb;
	//报告模板名称
	private String bgmbmc;
	//个数
	private String cn;
	//分类名称
	private String flmc;
	//子分类名称
	private String zflmc;
	//是否正确名称
	private String sfzqmc;
	//盖章类型
	private String gzlx;
	//盖章类型名称
	private String gzlxmc;
	//流水号
	private String serial;
	//检测项目代码
	private String jcdm;
	//标本类型代码
	private String lxdm;
	//调用打印机标记
	private String print_flg;
	//标本状态标记
	private String ybzt_flg;
	//钉钉小程序传递查询关键字
	private String cxnr;
	//文件名(复数)
	private List<String> wjms;
	//起运日期
	private String qyrq;
	//运达日期
	private String ydrq;
	//预计运达日期
	private String yjydrq;
	//起运时间开始
	private String qyrqstart;
	//起运时间结束
	private String qyrqend;
	//运达时间开始
	private String ydrqstart;
	//运达时间结束
	private String ydrqend;
	//打印份数
	private String print_num;
	//检测单位
	private String jcdw;
	//检测单位（多）
	private String[] jcdws;
	//确认页面打印机ip
	private String print_confirm_ip;
	//转管打印机ip
	private String print_demise_ip;
	//微信ID(复数)
	private List<String> wxids;
	//单位限制标记，0：不限制单位，1：限制单位
	private String dwxzbj;
	//复检状态
	private String fjzt;
	//加测状态
	private String jczt;
	//复检ID
	private String fcid;
	//加测ID
	private String jcid;
	//物种分类
	private String wzfl;
	//物种分类类型
	private String  wzfllx;
	//检测单位代码
	private String jcdwdm;
	//科室扩展参数
	private String kskzcs;
	//统计数量
	private String num;
	//自免目的地名称
	private String zmmddmc;
	//送检查看权限列表
	private List<SjqxDto> sjqxDtos;
	//检测项目代码s
	private String jcxmdms;
	//检测项目代码
	private String jcxmdm;
	//送检区分名称
	private String sjqfmc;
	//开票申请名称
	private String kpsqmc;
	//送检单位标记
	private String sjdwbj;
	//医院名称
	private String yymc;
	//检测项目类别
	private String jcxmlb;
	//送检单位简称
	private String sjdwjc;
	//数据同步标记
	private int tbbj;
	//付款标记名称
	private String fkbjmc;
	//判断是否是个人清单 single_flag=1 为个人清单  single_flag=0 or single_flag=null 为全部清单
	private String single_flag;
	//用于个人清单判断lrry [yhid, ddid, wxid]
	private List<String> userids;
	//角色检测单位限制
	private List<String> jcdwxz;
	//检测项目[多]
	private String[] jcxms;
	//医院名称(送检单位名称)
	private String hospitalname;
	//用于检索盖章类型
	private String[] gzlxs;
	//实验日期标记 高级检索用
	private String syrqflg;
	//实验日期开始
	private String syrqstart;
	//实验日期结束
	private String syrqend;
	//模糊查询全部参数
	private String all_param;
	//送检其他检测标记
	private String qtjcbj;
	//送检其他实验日期
	private String qtsyrq;
	//检测子项目名称
	private String jczxmmc;
	private String sfjeStart;
	private String sfjeEnd;
	private String sjpdid;
	private String jcdwjc;
	private String jczxmid;
	//检测子项目ID复数
	private List<String> jczxmids;
	//标本类型代码
	private String yblxdm;
	//处理标记
	private String clbj;
	//项目管理IDs
	private List<String> xmglids;
	private String jcxmmct;

	public String getJcxmmct() {
		return jcxmmct;
	}

	public void setJcxmmct(String jcxmmct) {
		this.jcxmmct = jcxmmct;
	}

	public List<String> getXmglids() {
		return xmglids;
	}

	public void setXmglids(List<String> xmglids) {
		this.xmglids = xmglids;
	}

	public String getClbj() {
		return clbj;
	}

	public void setClbj(String clbj) {
		this.clbj = clbj;
	}

	public String getYblxdm() {
		return yblxdm;
	}

	public void setYblxdm(String yblxdm) {
		this.yblxdm = yblxdm;
	}

	public List<String> getJczxmids() {
		return jczxmids;
	}

	public void setJczxmids(List<String> jczxmids) {
		this.jczxmids = jczxmids;
	}

	public String getJcdwjc() {
		return jcdwjc;
	}

	public void setJcdwjc(String jcdwjc) {
		this.jcdwjc = jcdwjc;
	}
	
	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	public String getSjpdid() {
		return sjpdid;
	}

	public void setSjpdid(String sjpdid) {
		this.sjpdid = sjpdid;
	}

	//合作伙伴分类[多]
	private String[] sjhbfls;
	//合作伙伴子分类[多]
	private String[] sjhbzfls;
	//送检日期
	private String sjrq;

	public String getSjrq() {
		return sjrq;
	}

	public void setSjrq(String sjrq) {
		this.sjrq = sjrq;
	}

	public String[] getSjhbfls() {
		return sjhbfls;
	}

	public void setSjhbfls(String[] sjhbfls) {
		this.sjhbfls = sjhbfls;
		for (int i = 0; i < sjhbfls.length; i++){
			this.sjhbfls[i]=this.sjhbfls[i].replace("'","");
		}
	}

	public String[] getSjhbzfls() {
		return sjhbzfls;
	}

	public void setSjhbzfls(String[] sjhbzfls) {
		this.sjhbzfls = sjhbzfls;
		for (int i = 0; i < sjhbzfls.length; i++){
			this.sjhbzfls[i]=this.sjhbzfls[i].replace("'","");
		}
	}
	public List<String> getKyxmids() {
		return kyxmids;
	}

	public void setKyxmids(List<String> kyxmids) {
		this.kyxmids = kyxmids;
	}

	public String getKyxmmc() {
		return kyxmmc;
	}

	public void setKyxmmc(String kyxmmc) {
		this.kyxmmc = kyxmmc;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public String getQtjcbj() {
		return qtjcbj;
	}

	public void setQtjcbj(String qtjcbj) {
		this.qtjcbj = qtjcbj;
	}

	public String getQtsyrq() {
		return qtsyrq;
	}

	public void setQtsyrq(String qtsyrq) {
		this.qtsyrq = qtsyrq;
	}

	public String getDwjc() {
		return dwjc;
	}

	public void setDwjc(String dwjc) {
		this.dwjc = dwjc;
	}

	public String getAll_param() {
		return all_param;
	}

	public void setAll_param(String all_param) {
		this.all_param = all_param;
	}

	public String getSyrqstart() {
		return syrqstart;
	}

	public void setSyrqstart(String syrqstart) {
		this.syrqstart = syrqstart;
	}

	public String getSyrqend() {
		return syrqend;
	}

	public void setSyrqend(String syrqend) {
		this.syrqend = syrqend;
	}

	public String getSyrqflg() {
		return syrqflg;
	}

	public void setSyrqflg(String syrqflg) {
		this.syrqflg = syrqflg;
	}

	public String[] getGzlxs() {
		return gzlxs;
	}

	public void setGzlxs(String[] gzlxs) {
		this.gzlxs = gzlxs;
		for (int i = 0; i < gzlxs.length; i++){
			this.gzlxs[i]=this.gzlxs[i].replace("'","");
		}
	}

	public String getHospitalname() {
		return hospitalname;
	}

	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String[] getJcxms() {
		return jcxms;
	}

	public void setJcxms(String[] jcxms) {
		this.jcxms = jcxms;
		for (int i = 0; i < jcxms.length; i++){
			this.jcxms[i]=this.jcxms[i].replace("'","");
		}
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}

	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}

	public String getSingle_flag() {
		return single_flag;
	}

	public void setSingle_flag(String single_flag) {
		this.single_flag = single_flag;
	}

	public String getFkbjmc() {
		return fkbjmc;
	}

	public void setFkbjmc(String fkbjmc) {
		this.fkbjmc = fkbjmc;
	}

	public int getTbbj() {
		return tbbj;
	}

	public void setTbbj(int tbbj) {
		this.tbbj = tbbj;
	}

	public String getSjdwjc() {
		return sjdwjc;
	}

	public void setSjdwjc(String sjdwjc) {
		this.sjdwjc = sjdwjc;
	}

	public String getJcxmlb() {
		return jcxmlb;
	}

	public void setJcxmlb(String jcxmlb) {
		this.jcxmlb = jcxmlb;
	}

	public String getYymc() {
		return yymc;
	}

	public void setYymc(String yymc) {
		this.yymc = yymc;
	}

	public String getSjdwbj() {
		return sjdwbj;
	}

	public void setSjdwbj(String sjdwbj) {
		this.sjdwbj = sjdwbj;
	}

	public String getKpsqmc() {
		return kpsqmc;
	}

	public void setKpsqmc(String kpsqmc) {
		this.kpsqmc = kpsqmc;
	}

	public String getSjqfmc() {
		return sjqfmc;
	}

	public void setSjqfmc(String sjqfmc) {
		this.sjqfmc = sjqfmc;
	}

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getJcxmdms() {
		return jcxmdms;
	}

	public void setJcxmdms(String jcxmdms) {
		this.jcxmdms = jcxmdms;
	}

	public List<SjqxDto> getSjqxDtos() {
		return sjqxDtos;
	}

	public void setSjqxDtos(List<SjqxDto> sjqxDtos) {
		this.sjqxDtos = sjqxDtos;
	}

	public String getZmmddmc() {
		return zmmddmc;
	}

	public void setZmmddmc(String zmmddmc) {
		this.zmmddmc = zmmddmc;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getKskzcs() {
		return kskzcs;
	}

	public void setKskzcs(String kskzcs) {
		this.kskzcs = kskzcs;
	}

	public List<SjybztDto> getSjybzts() {
		return sjybzts;
	}

	public void setSjybzts(List<SjybztDto> sjybzts) {
		this.sjybzts = sjybzts;
	}

	public String getStartpage() {
		return startpage;
	}

	public void setStartpage(String startpage) {
		this.startpage = startpage;
	}

	public String getApplydatestart() {
		return applydatestart;
	}

	public void setApplydatestart(String applydatestart) {
		this.applydatestart = applydatestart;
	}

	public String getApplydateend() {
		return applydateend;
	}

	public void setApplydateend(String applydateend) {
		this.applydateend = applydateend;
	}

	public String getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(String samplecode) {
		this.samplecode = samplecode;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}

	public String getW_ywlx() {
		return w_ywlx;
	}

	public void setW_ywlx(String w_ywlx) {
		this.w_ywlx = w_ywlx;
	}

	public String getWordfjid() {
		return wordfjid;
	}

	public void setWordfjid(String wordfjid) {
		this.wordfjid = wordfjid;
	}

	public String getWordwjlj() {
		return wordwjlj;
	}

	public void setWordwjlj(String wordwjlj) {
		this.wordwjlj = wordwjlj;
	}

	public String getWordwjm() {
		return wordwjm;
	}

	public void setWordwjm(String wordwjm) {
		this.wordwjm = wordwjm;
	}

	public List<String> getW_fjids() {
		return w_fjids;
	}

	public void setW_fjids(List<String> w_fjids) {
		this.w_fjids = w_fjids;
	}

	public String getYjxmmc() {
		return yjxmmc;
	}

	public void setYjxmmc(String yjxmmc) {
		this.yjxmmc = yjxmmc;
	}

	public String getJcxmcskz() {
		return jcxmcskz;
	}

	public void setJcxmcskz(String jcxmcskz) {
		this.jcxmcskz = jcxmcskz;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getYblx_flg() {
		return yblx_flg;
	}

	public void setYblx_flg(String yblx_flg) {
		this.yblx_flg = yblx_flg;
	}

	public String getSjdwxx_flg() {
		return sjdwxx_flg;
	}

	public void setSjdwxx_flg(String sjdwxx_flg) {
		this.sjdwxx_flg = sjdwxx_flg;
	}

	public String getGzbymc_flg() {
		return gzbymc_flg;
	}

	public void setGzbymc_flg(String gzbymc_flg) {
		this.gzbymc_flg = gzbymc_flg;
	}

	public String getQqjcmc_flg() {
		return qqjcmc_flg;
	}

	public void setQqjcmc_flg(String qqjcmc_flg) {
		this.qqjcmc_flg = qqjcmc_flg;
	}

	public String getJcxmmc_flg() {
		return jcxmmc_flg;
	}

	public void setJcxmmc_flg(String jcxmmc_flg) {
		this.jcxmmc_flg = jcxmmc_flg;
	}

	public String getLczzmc_flg() {
		return lczzmc_flg;
	}

	public void setLczzmc_flg(String lczzmc_flg) {
		this.lczzmc_flg = lczzmc_flg;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public String getWechatid() {
		return wechatid;
	}

	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}

	public String getHblx() {
		return hblx;
	}

	public void setHblx(String hblx) {
		this.hblx = hblx;
	}

	public String getNls() {
		return nls;
	}

	public void setNls(String nls) {
		this.nls = nls;
	}

	public String getXg_flg() {
		return xg_flg;
	}

	public void setXg_flg(String xg_flg) {
		this.xg_flg = xg_flg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMbdm() {
		return mbdm;
	}

	public void setMbdm(String mbdm) {
		this.mbdm = mbdm;
	}

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public String getFsfs() {
		return fsfs;
	}

	public void setFsfs(String fsfs) {
		this.fsfs = fsfs;
	}

	public String getYx() {
		return yx;
	}

	public void setYx(String yx) {
		this.yx = yx;
	}

	public String getBgrqstart() {
		return bgrqstart;
	}

	public void setBgrqstart(String bgrqstart) {
		this.bgrqstart = bgrqstart;
	}

	public String getBgrqend() {
		return bgrqend;
	}

	public void setBgrqend(String bgrqend) {
		this.bgrqend = bgrqend;
	}

	public String getBgrqMstart() {
		return bgrqMstart;
	}

	public void setBgrqMstart(String bgrqMstart) {
		this.bgrqMstart = bgrqMstart;
	}

	public String getBgrqMend() {
		return bgrqMend;
	}

	public void setBgrqMend(String bgrqMend) {
		this.bgrqMend = bgrqMend;
	}

	public String getBgrqYstart() {
		return bgrqYstart;
	}

	public void setBgrqYstart(String bgrqYstart) {
		this.bgrqYstart = bgrqYstart;
	}

	public String getBgrqYend() {
		return bgrqYend;
	}

	public void setBgrqYend(String bgrqYend) {
		this.bgrqYend = bgrqYend;
	}

	public String getLycs() {
		return lycs;
	}

	public void setLycs(String lycs) {
		this.lycs = lycs;
	}

	public String getSfsy() {
		return sfsy;
	}

	public void setSfsy(String sfsy) {
		this.sfsy = sfsy;
	}

	public String getSfjs() {
		return sfjs;
	}

	public void setSfjs(String sfjs) {
		this.sfjs = sfjs;
	}

	public String getSftj() {
		return sftj;
	}

	public void setSftj(String sftj) {
		this.sftj = sftj;
	}

	public List<String> getZts() {
		return zts;
	}

	public void setZts(List<String> zts) {
		this.zts = zts;
	}

	public String getJstj() {
		return jstj;
	}

	public void setJstj(String jstj) {
		this.jstj = jstj;
	}

	public String getHbcskz1() {
		return hbcskz1;
	}

	public void setHbcskz1(String hbcskz1) {
		this.hbcskz1 = hbcskz1;
	}

	public String getHbcskz2() {
		return hbcskz2;
	}

	public void setHbcskz2(String hbcskz2) {
		this.hbcskz2 = hbcskz2;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public String[] getSjkzcs1()
	{
		return sjkzcs1;
	}
	public void setSjkzcs1(String[] sjkzcs1)
	{
		this.sjkzcs1 = sjkzcs1;
		for (int i = 0; i < sjkzcs1.length; i++){
			this.sjkzcs1[i]=this.sjkzcs1[i].replace("'","");
		}
	}

	public String[] getSjkzcs2()
	{
		return sjkzcs2;
	}
	public void setSjkzcs2(String[] sjkzcs2)
	{
		this.sjkzcs2 = sjkzcs2;
		for (int i = 0; i < sjkzcs2.length; i++){
			this.sjkzcs2[i]=this.sjkzcs2[i].replace("'","");
		}
	}
	public String[] getSjkzcs3()
	{
		return sjkzcs3;

	}
	public void setSjkzcs3(String[] sjkzcs3)
	{
		this.sjkzcs3 = sjkzcs3;
		for (int i = 0; i < sjkzcs3.length; i++){
			this.sjkzcs3[i]=this.sjkzcs3[i].replace("'","");
		}
	}
	public String[] getSjkzcs4()
	{
		return sjkzcs4;
	}
	public void setSjkzcs4(String[] sjkzcs4)
	{
		this.sjkzcs4 = sjkzcs4;
		for (int i = 0; i < sjkzcs4.length; i++){
			this.sjkzcs4[i]=this.sjkzcs4[i].replace("'","");
		}
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	public List<String> getYbbhs() {
		return ybbhs;
	}

	public void setYbbhs(List<String> ybbhs) {
		this.ybbhs = ybbhs;
	}

	public String getHbfl() {
		return hbfl;
	}

	public void setHbfl(String hbfl) {
		this.hbfl = hbfl;
	}

	public String getHbzfl() {
		return hbzfl;
	}

	public void setHbzfl(String hbzfl) {
		this.hbzfl = hbzfl;
	}

	public String getZq() {
		return zq;
	}

	public void setZq(String zq) {
		this.zq = zq;
	}

	public List<String> getDbs() {
		return dbs;
	}

	public void setDbs(List<String> dbs) {
		this.dbs = dbs;
	}

	public List<String> getHbfls() {
		return hbfls;
	}

	public void setHbfls(List<String> hbfls) {
		this.hbfls = hbfls;
	}

	public String getFjyy() {
		return fjyy;
	}

	public void setFjyy(String fjyy) {
		this.fjyy = fjyy;
	}

	public String getCskz1_flg() {
		return cskz1_flg;
	}

	public void setCskz1_flg(String cskz1_flg) {
		this.cskz1_flg = cskz1_flg;
	}

	public String getCskz2_flg() {
		return cskz2_flg;
	}

	public void setCskz2_flg(String cskz2_flg) {
		this.cskz2_flg = cskz2_flg;
	}

	public String getCskz3_flg() {
		return cskz3_flg;
	}

	public void setCskz3_flg(String cskz3_flg) {
		this.cskz3_flg = cskz3_flg;
	}

	public String getCskz4_flg() {
		return cskz4_flg;
	}

	public void setCskz4_flg(String cskz4_flg) {
		this.cskz4_flg = cskz4_flg;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getJcxmkzcs() {
		return jcxmkzcs;
	}

	public void setJcxmkzcs(String jcxmkzcs) {
		this.jcxmkzcs = jcxmkzcs;
	}

	public String getTrsyrq() {
		return trsyrq;
	}

	public void setTrsyrq(String trsyrq) {
		this.trsyrq = trsyrq;
	}

	public String getTdsyrq() {
		return tdsyrq;
	}

	public void setTdsyrq(String tdsyrq) {
		this.tdsyrq = tdsyrq;
	}

	public String getLrsjStart() {
		return lrsjStart;
	}

	public void setLrsjStart(String lrsjStart) {
		this.lrsjStart = lrsjStart;
	}

	public String getLrsjEnd() {
		return lrsjEnd;
	}

	public void setLrsjEnd(String lrsjEnd) {
		this.lrsjEnd = lrsjEnd;
	}

	public String getBgbj() {
		return bgbj;
	}

	public void setBgbj(String bgbj) {
		this.bgbj = bgbj;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getPdflx() {
		return pdflx;
	}

	public void setPdflx(String pdflx) {
		this.pdflx = pdflx;
	}

	public String getWordlx() {
		return wordlx;
	}

	public void setWordlx(String wordlx) {
		this.wordlx = wordlx;
	}

	public String getSfbz() {
		return sfbz;
	}

	public void setSfbz(String sfbz) {
		this.sfbz = sfbz;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public String getTjtj() {
		return tjtj;
	}

	public void setTjtj(String tjtj) {
		this.tjtj = tjtj;
	}

	public String[] getTjzds() {
		return tjzds;
	}
	public void setTjzds(String[] tjzds) {
		this.tjzds = tjzds;
		for (int i = 0; i < tjzds.length; i++){
			this.tjzds[i]=this.tjzds[i].replace("\"","").replace("[","").replace("]","").replace("，",",");
		}
	}

	public String[] getMainSelects() {
		return mainSelects;
	}

	public void setMainSelects(String[] mainSelects) {
		this.mainSelects = mainSelects;
	}

	public String[] getMainGroups() {
		return mainGroups;
	}

	public void setMainGroups(String[] mainGroups) {
		this.mainGroups = mainGroups;
	}

	public String[] getGroupzds() {
		return groupzds;
	}

	public void setGroupzds(String[] groupzds) {
		this.groupzds = groupzds;
	}

	public String[] getSelectzds() {
		return selectzds;
	}

	public void setSelectzds(String[] selectzds) {
		this.selectzds = selectzds;
	}

	public String getWhereParam() {
		return whereParam;
	}

	public void setWhereParam(String whereParam) {
		this.whereParam = whereParam;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getZfl() {
		return zfl;
	}

	public void setZfl(String zfl) {
		this.zfl = zfl;
	}

	public String getBgmb() {
		return bgmb;
	}

	public void setBgmb(String bgmb) {
		this.bgmb = bgmb;
	}

	public String getBgmbmc() {
		return bgmbmc;
	}

	public void setBgmbmc(String bgmbmc) {
		this.bgmbmc = bgmbmc;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getFlmc() {
		return flmc;
	}

	public void setFlmc(String flmc) {
		this.flmc = flmc;
	}

	public String getZflmc() {
		return zflmc;
	}

	public void setZflmc(String zflmc) {
		this.zflmc = zflmc;
	}

	public String getSfzqmc() {
		return sfzqmc;
	}

	public void setSfzqmc(String sfzqmc) {
		this.sfzqmc = sfzqmc;
	}

	public String getGzlx() {
		return gzlx;
	}

	public void setGzlx(String gzlx) {
		this.gzlx = gzlx;
	}

	public String getGzlxmc() {
		return gzlxmc;
	}

	public void setGzlxmc(String gzlxmc) {
		this.gzlxmc = gzlxmc;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getJcdm() {
		return jcdm;
	}

	public void setJcdm(String jcdm) {
		this.jcdm = jcdm;
	}

	public String getLxdm() {
		return lxdm;
	}

	public void setLxdm(String lxdm) {
		this.lxdm = lxdm;
	}

	public String getPrint_flg() {
		return print_flg;
	}

	public void setPrint_flg(String print_flg) {
		this.print_flg = print_flg;
	}

	public String getYbzt_flg() {
		return ybzt_flg;
	}

	public void setYbzt_flg(String ybzt_flg) {
		this.ybzt_flg = ybzt_flg;
	}

	public String getCxnr() {
		return cxnr;
	}

	public void setCxnr(String cxnr) {
		this.cxnr = cxnr;
	}

	public List<String> getWjms() {
		return wjms;
	}

	public void setWjms(List<String> wjms) {
		this.wjms = wjms;
	}

	public String getQyrq() {
		return qyrq;
	}

	public void setQyrq(String qyrq) {
		this.qyrq = qyrq;
	}

	public String getYdrq() {
		return ydrq;
	}

	public void setYdrq(String ydrq) {
		this.ydrq = ydrq;
	}

	public String getYjydrq() {
		return yjydrq;
	}

	public void setYjydrq(String yjydrq) {
		this.yjydrq = yjydrq;
	}

	public String getQyrqstart() {
		return qyrqstart;
	}

	public void setQyrqstart(String qyrqstart) {
		this.qyrqstart = qyrqstart;
	}

	public String getQyrqend() {
		return qyrqend;
	}

	public void setQyrqend(String qyrqend) {
		this.qyrqend = qyrqend;
	}

	public String getYdrqstart() {
		return ydrqstart;
	}

	public void setYdrqstart(String ydrqstart) {
		this.ydrqstart = ydrqstart;
	}

	public String getYdrqend() {
		return ydrqend;
	}

	public void setYdrqend(String ydrqend) {
		this.ydrqend = ydrqend;
	}

	public String getPrint_num() {
		return print_num;
	}

	public void setPrint_num(String print_num) {
		this.print_num = print_num;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String[] getJcdws()
	{
		return jcdws;
	}

	public void setJcdws(String[] jcdws)
	{
		this.jcdws = jcdws;
		for (int i = 0; i <jcdws.length; i++){
			this.jcdws[i]=this.jcdws[i].replace("'","");
		}
	}

	public String getPrint_confirm_ip() {
		return print_confirm_ip;
	}

	public void setPrint_confirm_ip(String print_confirm_ip) {
		this.print_confirm_ip = print_confirm_ip;
	}

	public String getPrint_demise_ip() {
		return print_demise_ip;
	}

	public void setPrint_demise_ip(String print_demise_ip) {
		this.print_demise_ip = print_demise_ip;
	}

	public List<String> getWxids() {
		return wxids;
	}

	public void setWxids(List<String> wxids) {
		this.wxids = wxids;
	}

	public String getDwxzbj() {
		return dwxzbj;
	}

	public void setDwxzbj(String dwxzbj) {
		this.dwxzbj = dwxzbj;
	}

	public String getFjzt() {
		return fjzt;
	}

	public void setFjzt(String fjzt) {
		this.fjzt = fjzt;
	}

	public String getJczt() {
		return jczt;
	}

	public void setJczt(String jczt) {
		this.jczt = jczt;
	}

	public String getFcid() {
		return fcid;
	}

	public void setFcid(String fcid) {
		this.fcid = fcid;
	}

	public String getJcid() {
		return jcid;
	}

	public void setJcid(String jcid) {
		this.jcid = jcid;
	}

	public String getWzfl() {
		return wzfl;
	}

	public void setWzfl(String wzfl) {
		this.wzfl = wzfl;
	}

	public String getWzfllx() {
		return wzfllx;
	}

	public void setWzfllx(String wzfllx) {
		this.wzfllx = wzfllx;
	}

	public String getJcdwdm() {
		return jcdwdm;
	}

	public void setJcdwdm(String jcdwdm) {
		this.jcdwdm = jcdwdm;
	}

	public String getFkmc() {
		return fkmc;
	}

	public void setFkmc(String fkmc) {
		this.fkmc = fkmc;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getSfjsmc() {
		return sfjsmc;
	}

	public void setSfjsmc(String sfjsmc) {
		this.sfjsmc = sfjsmc;
	}

	public String getSftjmc() {
		return sftjmc;
	}

	public void setSftjmc(String sftjmc) {
		this.sftjmc = sftjmc;
	}

	public String getSfsfmc() {
		return sfsfmc;
	}

	public void setSfsfmc(String sfsfmc) {
		this.sfsfmc = sfsfmc;
	}

	public String getYbztmc() {
		return ybztmc;
	}

	public void setYbztmc(String ybztmc) {
		this.ybztmc = ybztmc;
	}

	public String[] getJyjgs()
	{
		return jyjgs;
	}
	public void setJyjgs(String[] jyjgs)
	{
		this.jyjgs = jyjgs;
		for (int i = 0; i < jyjgs.length; i++){
			this.jyjgs[i]=this.jyjgs[i].replace("'","");
		}
	}

	public List<String> getLrrys() {
		return lrrys;
	}

	public void setLrrys(String lrrys) {
		List<String> list = new ArrayList<String>();
		String str[] = lrrys.split(",");
		list = Arrays.asList(str);
		this.lrrys = list;
	}
	public void setLrrys(List<String> lrrys) {
		if(lrrys!=null && lrrys.size() > 0){
			for(int i=0;i<lrrys.size();i++){
				lrrys.set(i,lrrys.get(i).replace("[", "").replace("]", "").trim());
			}
		}
		this.lrrys = lrrys;
	}

	public List<SjhbxxDto> getSjhbxxs() {
		return sjhbxxs;
	}

	public void setSjhbxxs(List<SjhbxxDto> sjhbxxs) {
		this.sjhbxxs = sjhbxxs;
	}

	public String getCyrqstart() {
		return cyrqstart;
	}

	public void setCyrqstart(String cyrqstart) {
		this.cyrqstart = cyrqstart;
	}

	public String getCyrqend() {
		return cyrqend;
	}

	public void setCyrqend(String cyrqend) {
		this.cyrqend = cyrqend;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String[] getYblxs() {
		return yblxs;
	}

	public void setYblxs(String[] yblxs){
		this.yblxs = yblxs;
		for (int i = 0; i < yblxs.length; i++){
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}



	public String[] getDwids() {
		return dwids;
	}

	public void setDwids(String[] dwids) {
		this.dwids=dwids;
		for (int i = 0; i < dwids.length; i++){
			this.dwids[i]=this.dwids[i].replace("'","");
		}
	}

	public String[] getKdlxs()
	{
		return kdlxs;
	}

	public void setKdlxs(String[] kdlxs)
	{
		this.kdlxs = kdlxs;
		for (int i = 0; i < kdlxs.length; i++){
			this.kdlxs[i]=this.kdlxs[i].replace("'","");
		}
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getCskz4() {
		return cskz4;
	}

	public void setCskz4(String cskz4) {
		this.cskz4 = cskz4;
	}

	public String getCskz5() {
		return cskz5;
	}

	public void setCskz5(String cskz5) {
		this.cskz5 = cskz5;
	}

	public String getKdlxmc() {
		return kdlxmc;
	}

	public void setKdlxmc(String kdlxmc) {
		this.kdlxmc = kdlxmc;
	}

	public List<String> getDellist() {
		return dellist;
	}

	public void setDellist(List<String> dellist) {
		this.dellist = dellist;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEntire()
	{
		return entire;
	}

	public void setEntire(String entire)
	{
		this.entire = entire;
	}

	public String getSfzq()
	{
		return sfzq;
	}

	public void setSfzq(String sfzq)
	{
		this.sfzq = sfzq;
	}

	public String getLcfk()
	{
		return lcfk;
	}

	public void setLcfk(String lcfk)
	{
		this.lcfk = lcfk;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getFjsrq() {
		return fjsrq;
	}

	public void setFjsrq(String fjsrq) {
		this.fjsrq = fjsrq;
	}

	public String getTjzq()
	{
		return tjzq;
	}

	public void setTjzq(String tjzq)
	{
		this.tjzq = tjzq;
	}

	public List<String> getQqjcids() {
		return qqjcids;
	}

	public void setQqjcids(List<String> qqjcids) {
		this.qqjcids = qqjcids;
	}

	public List<String> getJczs() {
		return jczs;
	}

	public void setJczs(List<String> jczs) {
		this.jczs = jczs;
	}

	public List<String> getSjdws()
	{
		return sjdws;
	}

	public void setSjdws(List<String> sjdws)
	{
		this.sjdws = sjdws;
	}

	public String getZsxm()
	{
		return zsxm;
	}

	public void setZsxm(String zsxm)
	{
		this.zsxm = zsxm;
	}

	public String getTj()
	{
		return tj;
	}

	public void setTj(String tj)
	{
		this.tj = tj;
	}

	public List<String> getRqs()
	{
		return rqs;
	}

	public void setRqs(List<String> rqs)
	{
		this.rqs = rqs;
	}

	public String getSflx()
	{
		return sflx;
	}

	public void setSflx(String sflx)
	{
		this.sflx = sflx;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}
	public String getYsid() {
		return ysid;
	}

	public void setYsid(String ysid) {
		this.ysid = ysid;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public List<String> getLczzs() {
		return lczzs;
	}

	public void setLczzs(List<String> lczzs) {
		this.lczzs = lczzs;
	}

	public List<String> getJcxmids() {
		return jcxmids;
	}

	public void setJcxmids(List<String> jcxmids) {
		this.jcxmids = jcxmids;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public String getDbm() {
		return dbm;
	}

	public void setDbm(String dbm) {
		this.dbm = dbm;
	}

	public List<SjlczzDto> getSjlczzs() {
		return sjlczzs;
	}

	public void setSjlczzs(List<SjlczzDto> sjlczzs) {
		this.sjlczzs = sjlczzs;
	}

	public List<SjjcxmDto> getSjjcxms() {
		return sjjcxms;
	}

	public void setSjjcxms(List<SjjcxmDto> sjjcxms) {
		this.sjjcxms = sjjcxms;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getKsdm() {
		return ksdm;
	}

	public void setKsdm(String ksdm) {
		this.ksdm = ksdm;
	}

	public String getXbmc() {
		return xbmc;
	}

	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}

	public List<String> getBys() {
		return bys;
	}

	public void setBys(List<String> bys) {
		this.bys = bys;
	}

	public String getNewflg() {
		return newflg;
	}

	public void setNewflg(String newflg) {
		this.newflg = newflg;
	}

	public List<SjqqjcDto> getSjqqjcs() {
		return sjqqjcs;
	}

	public void setSjqqjcs(List<SjqqjcDto> sjqqjcs) {
		this.sjqqjcs = sjqqjcs;
	}

	public List<SjgzbyDto> getSjgzbys() {
		return sjgzbys;
	}

	public void setSjgzbys(List<SjgzbyDto> sjgzbys) {
		this.sjgzbys = sjgzbys;
	}

	public String getGzbymc() {
		return gzbymc;
	}

	public void setGzbymc(String gzbymc) {
		this.gzbymc = gzbymc;
	}

	public String getQqjcmc() {
		return qqjcmc;
	}

	public void setQqjcmc(String qqjcmc) {
		this.qqjcmc = qqjcmc;
	}

	public String getLczzmc() {
		return lczzmc;
	}

	public void setLczzmc(String lczzmc) {
		this.lczzmc = lczzmc;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getSfflg() {
		return sfflg;
	}

	public void setSfflg(String sfflg) {
		this.sfflg = sfflg;
	}

	public String getFy() {
		return fy;
	}

	public void setFy(String fy) {
		this.fy = fy;
	}

	public String getIsTouchMove() {
		return isTouchMove;
	}

	public void setIsTouchMove(String isTouchMove) {
		this.isTouchMove = isTouchMove;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getJsrqstart()
	{
		return jsrqstart;
	}

	public void setJsrqstart(String jsrqstart)
	{
		this.jsrqstart = jsrqstart;
	}

	public String getJsrqend()
	{
		return jsrqend;
	}

	public void setJsrqend(String jsrqend)
	{
		this.jsrqend = jsrqend;
	}

	public String getJsrqMstart()
	{
		return jsrqMstart;
	}

	public void setJsrqMstart(String jsrqMstart)
	{
		this.jsrqMstart = jsrqMstart;
	}

	public String getJsrqMend()
	{
		return jsrqMend;
	}

	public void setJsrqMend(String jsrqMend)
	{
		this.jsrqMend = jsrqMend;
	}

	public String getJsrqYstart()
	{
		return jsrqYstart;
	}

	public void setJsrqYstart(String jsrqYstart)
	{
		this.jsrqYstart = jsrqYstart;
	}

	public String getJsrqYend()
	{
		return jsrqYend;
	}

	public void setJsrqYend(String jsrqYend)
	{
		this.jsrqYend = jsrqYend;
	}

	public String getSfjeStart() {
		return sfjeStart;
	}

	public void setSfjeStart(String sfjeStart) {
		this.sfjeStart = sfjeStart;
	}

	public String getSfjeEnd() {
		return sfjeEnd;
	}

	public void setSfjeEnd(String sfjeEnd) {
		this.sfjeEnd = sfjeEnd;
	}
}
