package com.matridx.igams.wechat.dao.entities;

import java.util.List;
import java.util.Map;

public class WeChatInspectionReportModel {

	//标本编号
	private String ybbh;
	//检测类型
	private String detection_type;
	//检测子类型
	private String detection_subtype;
	//检测ID
	private String detection_cskz1;
	//标本ID
	private String sample_id;
	//返回状态
	private String status;
	//标签列表
	private List<WeChatInspectionSpeciesModel> pathogen;
	//标签列表
	private List<WeChatInspectionSpeciesModel> possible;
	//耐药性
	private List<WeChatInspectionResistanceModel> drug_resistance_stat;
	//结核非结核耐药性
	private List<SjwzxxDto> tbt_drug_resistance_stat;
	//耐药基因注释
	private List<WechatInspectionResistModel> drug_resist_gene;
	//结核非结核耐药基因注释
	private List<SjwzxxDto> tbt_drug_resist_gene;
	//高度关注说明
	private String pathogen_comment;
	//疑似说明
	private String possible_comment;
	//高度关注阳性指标
	private String pathogen_names;
	//疑似指标
	private String possible_names;
	//参考文献
	private String refs;
	//业务类型
	private String ywlx;
	//模板路径
	private String mblj;
	//模板文件名称
	private String mbwjmc;
	//Q20
	private String q20;
	//Q30
	private String q30;
	//送检id
	private String sjid;
	//定值信息物种list
	private List<WeChatInspectionSpeciesModel> background;
	//背景物种list
	private List<WeChatInspectionSpeciesModel> commensal;
	//临床症状指南list
	private List<WechatClinicalGuideModel> guidelines;
	//物种基础信息list
	private List<WeChatInspectionSpeciesModel> wzgllist;
	//参考文献list
	private List<WechatReferencesModel> papers;
	//人源指数
	private String host_index;
	//表示host index在同类型标本中的百分位数
	private String host_percentile;
	//人源指数位置
	private String host_position;
	//阈值
	private String spike_rpm;
	//是否去人员标记.1代表去人源;0代表不去人员
	private String sfqry;
	//GC含量
	private String gc;
	//总序列数
	private String total_reads;
	//宿主序列百分比
	private String homo_percentage;
	//微生物检出序列数
	private String microbial_reads;
	//有效序列数
	private String raw_reads;
	//页眉
	private String header;
	//报告类别
	private String report_type;
	//肿瘤风险
	private String review_result;
	//分子核型结果
	private String karyotype;
	//cnv_data
	private String cnv_data;
	//True 不自动去人源
	private String auto_rem_off;
	//检验人员
	private String jyry;
	//审核人员
	private String shry;
	//mngs标记
	private String mngsFlag;
	//核酸浓度
	private String nuc_concentration;
	//文库浓度
	private String lib_concentration;
	//去人源序列数
	private String nonhuman_reads;
	//毒力因子list
	private List<WechatVirulenceFactorStatModel> virulence_factor_stat;
	//审核备注
	private String audit_comment;
	//项目代码
	private String xmdm;
	//是否含有定值列表
	private Boolean has_commensal;
	//推荐用药信息
	private Map<String,List<Map<String,Object>>> species_drugs;
	//项目模板对应，用于信息对应cskz8和检测项目cskz3的对应
	private String xmmbdy;
	//检测项目参数扩展三，送检验证专用
	private String project_type;
	private String yzlb;
	private String jsrq;
	private List<SjwzxxDto> yzjgList;
	private String ddbh;
	private String sample_type_name;
	//微生物比例
	private String lowQ_percent;
	//重复序列比例
	private String duplicate_ratio;
	//rawreads得q30比例
	private String Q30_rawdata;
	//覆盖的
	private String coverage;
	//tngs泛感染和TBT合并发送标记，用于/pathogen/receiveInspectionGenerateReportSec接口中判断
	private String hbfs;
	private List<SjwzxxDto> zkjgList;
	private String jcxmmc;
	//扩增子信息
	private Map<String,WeChatInspectionAmpliconModel> amplicon;

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public List<SjwzxxDto> getZkjgList() {
		return zkjgList;
	}

	public void setZkjgList(List<SjwzxxDto> zkjgList) {
		this.zkjgList = zkjgList;
	}

	public String getSample_type_name() {
		return sample_type_name;
	}

	public void setSample_type_name(String sample_type_name) {
		this.sample_type_name = sample_type_name;
	}

	public String getDdbh() {
		return ddbh;
	}

	public void setDdbh(String ddbh) {
		this.ddbh = ddbh;
	}

	public String getYzlb() {
		return yzlb;
	}

	public void setYzlb(String yzlb) {
		this.yzlb = yzlb;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public List<SjwzxxDto> getYzjgList() {
		return yzjgList;
	}

	public void setYzjgList(List<SjwzxxDto> yzjgList) {
		this.yzjgList = yzjgList;
	}

	public String getProject_type() {
		return project_type;
	}

	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}

	public String getHbfs() {
		return hbfs;
	}

	public void setHbfs(String hbfs) {
		this.hbfs = hbfs;
	}

	public List<SjwzxxDto> getTbt_drug_resistance_stat() {
		return tbt_drug_resistance_stat;
	}

	public void setTbt_drug_resistance_stat(List<SjwzxxDto> tbt_drug_resistance_stat) {
		this.tbt_drug_resistance_stat = tbt_drug_resistance_stat;
	}

	public List<SjwzxxDto> getTbt_drug_resist_gene() {
		return tbt_drug_resist_gene;
	}

	public void setTbt_drug_resist_gene(List<SjwzxxDto> tbt_drug_resist_gene) {
		this.tbt_drug_resist_gene = tbt_drug_resist_gene;
	}

	public String getXmmbdy() {
		return xmmbdy;
	}

	public void setXmmbdy(String xmmbdy) {
		this.xmmbdy = xmmbdy;
	}

	public Map<String, List<Map<String, Object>>> getSpecies_drugs() {
		return species_drugs;
	}

	public void setSpecies_drugs(Map<String, List<Map<String, Object>>> species_drugs) {
		this.species_drugs = species_drugs;
	}

	public Boolean getHas_commensal() {
		return has_commensal;
	}

	public void setHas_commensal(Boolean has_commensal) {
		this.has_commensal = has_commensal;
	}

	public String getNuc_concentration() {
		return nuc_concentration;
	}

	public void setNuc_concentration(String nuc_concentration) {
		this.nuc_concentration = nuc_concentration;
	}

	public String getMngsFlag() {
		return mngsFlag;
	}

	public void setMngsFlag(String mngsFlag) {
		this.mngsFlag = mngsFlag;
	}
	public String getXmdm() {
		return xmdm;
	}

	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}

	public String getAudit_comment() {
		return audit_comment;
	}

	public void setAudit_comment(String audit_comment) {
		this.audit_comment = audit_comment;
	}

	public List<WechatVirulenceFactorStatModel> getVirulence_factor_stat() {
		return virulence_factor_stat;
	}

	public void setVirulence_factor_stat(List<WechatVirulenceFactorStatModel> virulence_factor_stat) {
		this.virulence_factor_stat = virulence_factor_stat;
	}
	
	public String getLib_concentration() {
		return lib_concentration;
	}

	public void setLib_concentration(String lib_concentration) {
		this.lib_concentration = lib_concentration;
	}

	public String getNonhuman_reads() {
		return nonhuman_reads;
	}

	public void setNonhuman_reads(String nonhuman_reads) {
		this.nonhuman_reads = nonhuman_reads;
	}

	public String getJyry() {
		return jyry;
	}

	public void setJyry(String jyry) {
		this.jyry = jyry;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getAuto_rem_off() {
		return auto_rem_off;
	}

	public void setAuto_rem_off(String auto_rem_off) {
		this.auto_rem_off = auto_rem_off;
	}

	public String getCnv_data() {
		return cnv_data;
	}

	public void setCnv_data(String cnv_data) {
		this.cnv_data = cnv_data;
	}

	public String getKaryotype() {
		return karyotype;
	}

	public void setKaryotype(String karyotype) {
		this.karyotype = karyotype;
	}

	public String getReview_result() {
		return review_result;
	}

	public void setReview_result(String review_result) {
		this.review_result = review_result;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getRaw_reads() {
		return raw_reads;
	}
	public void setRaw_reads(String raw_reads) {
		this.raw_reads = raw_reads;
	}
	public String getGc() {
		return gc;
	}
	public void setGc(String gc) {
		this.gc = gc;
	}
	public String getTotal_reads() {
		return total_reads;
	}
	public void setTotal_reads(String total_reads) {
		this.total_reads = total_reads;
	}
	public String getHomo_percentage() {
		return homo_percentage;
	}
	public void setHomo_percentage(String homo_percentage) {
		this.homo_percentage = homo_percentage;
	}
	public String getMicrobial_reads() {
		return microbial_reads;
	}
	public void setMicrobial_reads(String microbial_reads) {
		this.microbial_reads = microbial_reads;
	}
	public String getSfqry() {
		return sfqry;
	}
	public void setSfqry(String sfqry) {
		this.sfqry = sfqry;
	}

	public String getDetection_cskz1() {
		return detection_cskz1;
	}
	public void setDetection_cskz1(String detection_cskz1) {
		this.detection_cskz1 = detection_cskz1;
	}
	public String getSpike_rpm() {
		return spike_rpm;
	}
	public void setSpike_rpm(String spike_rpm) {
		this.spike_rpm = spike_rpm;
	}
	public String getHost_position() {
		return host_position;
	}
	public void setHost_position(String host_position) {
		this.host_position = host_position;
	}
	public String getHost_index() {
		return host_index;
	}
	public void setHost_index(String host_index) {
		this.host_index = host_index;
	}
	public String getHost_percentile() {
		return host_percentile;
	}
	public void setHost_percentile(String host_percentile) {
		this.host_percentile = host_percentile;
	}
	public List<WechatInspectionResistModel> getDrug_resist_gene() {
		return drug_resist_gene;
	}
	public void setDrug_resist_gene(List<WechatInspectionResistModel> drug_resist_gene) {
		this.drug_resist_gene = drug_resist_gene;
	}
	public List<WechatClinicalGuideModel> getGuidelines() {
		return guidelines;
	}
	public void setGuidelines(List<WechatClinicalGuideModel> guidelines) {
		this.guidelines = guidelines;
	}
	public List<WeChatInspectionSpeciesModel> getWzgllist() {
		return wzgllist;
	}
	public void setWzgllist(List<WeChatInspectionSpeciesModel> wzgllist) {
		this.wzgllist = wzgllist;
	}
	public List<WechatReferencesModel> getPapers() {
		return papers;
	}
	public void setPapers(List<WechatReferencesModel> papers) {
		this.papers = papers;
	}
	public List<WeChatInspectionSpeciesModel> getBackground() {
		return background;
	}
	public void setBackground(List<WeChatInspectionSpeciesModel> background) {
		this.background = background;
	}
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid) {
		this.sjid = sjid;
	}
	public String getQ20() {
		return q20;
	}
	public void setQ20(String q20) {
		this.q20 = q20;
	}
	public String getQ30() {
		return q30;
	}
	public void setQ30(String q30) {
		this.q30 = q30;
	}
	public String getMbwjmc() {
		return mbwjmc;
	}
	public void setMbwjmc(String mbwjmc) {
		this.mbwjmc = mbwjmc;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getMblj() {
		return mblj;
	}
	public void setMblj(String mblj) {
		this.mblj = mblj;
	}
	public String getDetection_type() {
		return detection_type;
	}
	public void setDetection_type(String detection_type) {
		this.detection_type = detection_type;
	}
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}
	public String getSample_id() {
		return sample_id;
	}
	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<WeChatInspectionSpeciesModel> getPathogen() {
		return pathogen;
	}
	public void setPathogen(List<WeChatInspectionSpeciesModel> pathogen) {
		this.pathogen = pathogen;
	}
	public List<WeChatInspectionSpeciesModel> getPossible() {
		return possible;
	}
	public void setPossible(List<WeChatInspectionSpeciesModel> possible) {
		this.possible = possible;
	}
	public String getPathogen_comment() {
		return pathogen_comment;
	}
	public void setPathogen_comment(String pathogen_comment) {
		this.pathogen_comment = pathogen_comment;
	}
	public String getPossible_comment() {
		return possible_comment;
	}
	public void setPossible_comment(String possible_comment) {
		this.possible_comment = possible_comment;
	}
	public String getPathogen_names() {
		return pathogen_names;
	}
	public void setPathogen_names(String pathogen_names) {
		this.pathogen_names = pathogen_names;
	}
	public String getPossible_names() {
		return possible_names;
	}
	public void setPossible_names(String possible_names) {
		this.possible_names = possible_names;
	}
	public String getRefs() {
		return refs;
	}
	public void setRefs(String refs) {
		this.refs = refs;
	}
	public List<WeChatInspectionResistanceModel> getDrug_resistance_stat() {
		return drug_resistance_stat;
	}
	public void setDrug_resistance_stat(List<WeChatInspectionResistanceModel> drug_resistance_stat) {
		this.drug_resistance_stat = drug_resistance_stat;
	}

	public List<WeChatInspectionSpeciesModel> getCommensal() {
		return commensal;
	}

	public void setCommensal(List<WeChatInspectionSpeciesModel> commensal) {
		this.commensal = commensal;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		coverage = coverage;
	}

	public String getQ30_rawdata() {
		return Q30_rawdata;
	}

	public void setQ30_rawdata(String q30_rawdata) {
		Q30_rawdata = q30_rawdata;
	}

	public String getDuplicate_ratio() {
		return duplicate_ratio;
	}

	public void setDuplicate_ratio(String duplicate_ratio) {
		this.duplicate_ratio = duplicate_ratio;
	}

	public String getLowQ_percent() {
		return lowQ_percent;
	}

	public void setLowQ_percent(String lowQ_percent) {
		this.lowQ_percent = lowQ_percent;
	}

    public String getDetection_subtype() {
        return detection_subtype;
    }

    public void setDetection_subtype(String detection_subtype) {
        this.detection_subtype = detection_subtype;
    }

    public Map<String, WeChatInspectionAmpliconModel> getAmplicon() {
        return amplicon;
    }

    public void setAmplicon(Map<String, WeChatInspectionAmpliconModel> amplicon) {
        this.amplicon = amplicon;
    }
}
