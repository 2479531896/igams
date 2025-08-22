package com.matridx.igams.wechat.dao.entities;

public class WeChatInspectionSpeciesModel {

	//物种分类ID
	private String taxid;
	//物种英文名
	private String name;
	//物种中文名
	private String cn_name;
	//读取数
	private String reads_count;
	//物种分类
	private String sp_type;
	//基因组覆盖度
	private String coverage;
	//相对丰度
	private String abundance;
	//细菌类型
	private String gram_stain;
	//病毒类型
	private String virus_type;
	//属的taxid
	private String genus_taxid;
	//属名
	private String genus_name;
	//属中文名
	private String genus_cn_name;
	//属的reads数
	private String genus_reads_accum;
	//属的相对丰度
	private String genus_abundance;
	//更新时间
	private String last_update;
	//物种类型
	private String species_category;
	//物种注释
	private String comment;
	//是否高亮
	private String highlight;
	//物种分类等级
	private String rank_code;
	//校正人源背景后该微生物的含量
	private String q_index;
	//表示q_index在同类型标本中检出的该物种中的百分位数，float格式的字符串，或‘None’。None表示参考数据集中未检出该物种，无法计算百分位数。
	private String q_percentile;
	//校正人源指数位置
	private String q_position;
	//传染病等级
	private String crbjb;
	//是否拦截
	private String sflj;
	//基因组总长度
	private String cover_length;
	//基因组覆盖长度
	private String ref_length;
	//结果类型
	private String jglx;
	//结果类型名称
	private String jglxmc;
	//荧光值
	private String ygz;
	//核酸类型
	private String library_type;
	//特殊分类
	private String special_genus;
	//rpm
	private String rpm;
	//物种报告配置
	private String wzbgpz;
	//物种图片路径
	private String bjtppath;
	//引用序号
	private String yyxh;
	//新冠分型
	private String clade;
	//物种报告配置(all，阳性、疑似、阴性)
	//物种报告配置(all，阳性、疑似、阴性)
	private String wzbgpzall;
	//技术模型
	private String project_type;
	//新增属性 accum
	private String accum;
	//新增属性 label
	private String label;
	//新增属性 impact
	private String impact;
	//新增属性 kingdom
	private String kingdom;
	//新增属性 genus_accum
	private String genus_accum;
	//新增属性 pathogenicity
	private String pathogenicity;

	private String reportType;

	//检出分类，为了区分是否为TBT项目的检出物种，后续可扩展使用
	private String detectType;

	public String getDetectType() {
		return detectType;
	}

	public void setDetectType(String detectType) {
		this.detectType = detectType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getProject_type() {
		return project_type;
	}

	public void setProject_type(String project_type) {
		this.project_type = project_type;
	}

	public String getWzbgpzall() {
		return wzbgpzall;
	}

	public void setWzbgpzall(String wzbgpzall) {
		this.wzbgpzall = wzbgpzall;
	}

	public String getYgz() {
		return ygz;
	}

	public void setYgz(String ygz) {
		this.ygz = ygz;
	}

	public String getJglxmc() {
		return jglxmc;
	}

	public void setJglxmc(String jglxmc) {
		this.jglxmc = jglxmc;
	}

	public String getClade() {
		return clade;
	}

	public void setClade(String clade) {
		this.clade = clade;
	}

	public String getYyxh() {
		return yyxh;
	}

	public void setYyxh(String yyxh) {
		this.yyxh = yyxh;
	}

	public String getBjtppath() {
		return bjtppath;
	}

	public void setBjtppath(String bjtppath) {
		this.bjtppath = bjtppath;
	}

	public String getWzbgpz() {
		return wzbgpz;
	}

	public void setWzbgpz(String wzbgpz) {
		this.wzbgpz = wzbgpz;
	}

	public String getRpm() {
		return rpm;
	}

	public void setRpm(String rpm) {
		this.rpm = rpm;
	}

	public String getSpecial_genus() {
		return special_genus;
	}

	public void setSpecial_genus(String special_genus) {
		this.special_genus = special_genus;
	}

	public String getLibrary_type() {
		return library_type;
	}

	public void setLibrary_type(String library_type) {
		this.library_type = library_type;
	}
	public String getCover_length() {
		return cover_length;
	}

	public void setCover_length(String cover_length) {
		this.cover_length = cover_length;
	}

	public String getRef_length() {
		return ref_length;
	}

	public void setRef_length(String ref_length) {
		this.ref_length = ref_length;
	}

	public String getJglx() {
		return jglx;
	}
	public void setJglx(String jglx) {
		this.jglx = jglx;
	}
	public String getCrbjb() {
		return crbjb;
	}
	public void setCrbjb(String crbjb) {
		this.crbjb = crbjb;
	}
	public String getSflj() {
		return sflj;
	}
	public void setSflj(String sflj) {
		this.sflj = sflj;
	}
	public String getQ_position() {
		return q_position;
	}
	public void setQ_position(String q_position) {
		this.q_position = q_position;
	}
	public String getQ_index() {
		return q_index;
	}
	public void setQ_index(String q_index) {
		this.q_index = q_index;
	}
	public String getQ_percentile() {
		return q_percentile;
	}
	public void setQ_percentile(String q_percentile) {
		this.q_percentile = q_percentile;
	}
	public String getRank_code() {
		return rank_code;
	}
	public void setRank_code(String rank_code) {
		this.rank_code = rank_code;
	}
	public String getHighlight() {
		return highlight;
	}
	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSpecies_category() {
		return species_category;
	}
	public void setSpecies_category(String species_category) {
		this.species_category = species_category;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCn_name() {
		return cn_name;
	}
	public void setCn_name(String cn_name) {
		this.cn_name = cn_name;
	}
	public String getReads_count() {
		return reads_count;
	}
	public void setReads_count(String reads_count) {
		this.reads_count = reads_count;
	}
	public String getSp_type() {
		return sp_type;
	}
	public void setSp_type(String sp_type) {
		this.sp_type = sp_type;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public String getAbundance() {
		return abundance;
	}
	public void setAbundance(String abundance) {
		this.abundance = abundance;
	}
	public String getGram_stain() {
		return gram_stain;
	}
	public void setGram_stain(String gram_stain) {
		this.gram_stain = gram_stain;
	}
	public String getVirus_type() {
		return virus_type;
	}
	public void setVirus_type(String virus_type) {
		this.virus_type = virus_type;
	}
	public String getGenus_taxid() {
		return genus_taxid;
	}
	public void setGenus_taxid(String genus_taxid) {
		this.genus_taxid = genus_taxid;
	}
	public String getGenus_name() {
		return genus_name;
	}
	public void setGenus_name(String genus_name) {
		this.genus_name = genus_name;
	}
	public String getGenus_cn_name() {
		return genus_cn_name;
	}
	public void setGenus_cn_name(String genus_cn_name) {
		this.genus_cn_name = genus_cn_name;
	}
	public String getGenus_reads_accum() {
		return genus_reads_accum;
	}
	public void setGenus_reads_accum(String genus_reads_accum) {
		this.genus_reads_accum = genus_reads_accum;
	}
	public String getGenus_abundance() {
		return genus_abundance;
	}
	public void setGenus_abundance(String genus_abundance) {
		this.genus_abundance = genus_abundance;
	}

	public String getAccum() {
		return accum;
	}

	public void setAccum(String accum) {
		this.accum = accum;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getGenus_accum() {
		return genus_accum;
	}

	public void setGenus_accum(String genus_accum) {
		this.genus_accum = genus_accum;
	}

	public String getPathogenicity() {
		return pathogenicity;
	}

	public void setPathogenicity(String pathogenicity) {
		this.pathogenicity = pathogenicity;
	}
}
