package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Set;

@Alias(value = "WkcxDto")
public class WkcxDto extends WkcxModel {

	private static final long serialVersionUID = 1L;
	private String jgid ;
	private String rawreads ;
	private String totalreads ;
	private String wkbhlike;
	private String homo ;
	private String bacteria ;
	private String fungi ;
	private String viruses ;
	private String parasite ;
	private String others ;
	private String unmapped ;
	private String spikein ;
	private String spikeinrpm ;
	private String bbh ;
	private String spike ;
	private String ryzs ;
	private String rypw ;
	private String sfzwqckj;
	private String cxkssjstart;//测序开始时间起
	private String cxkssjend;//测序开始时间止
	private List<String> zts;//状态
	private String lrsjstart;
	private String lrsjend;
	private String hzxm;//患者姓名
	private String nl;//年龄
	private List<String> xgwxTaxids;//相关文献
	private List<String> zsxxIds;//注释信息
	private List<String> wswTaxids;//微生物
	private List<String> ysTaxids;//疑似
	private String xb;//性别
	private String statsType;//统计类别 day week month
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String sample_id;//合并后的文库ID
	//报告类型
	private String detection_type;
	private String taxid;
	private List<String> zdTaxids;//重点关注微生物
	private List<String> possibleTaxids;//关注微生物
	private List<String> backgroundTaxids;//背景微生物
	private String nlz;//年龄组
	private String yblxmc;//样本类型名称
	private String rpq;//rpm
	private String gra;//gra
	private String readsaccum;//ram
	private List<String> lrsjs;//录入时间
	private String library_type;
	private String db;//送检伙伴名称
	private String dwmc;//送检单位名称,医院信息表的dwmc
	private String dwid;//送检单位id
	private String yblx;//样本类型
	private String xpm;//芯片名即芯片编号
	private String nyjy;//耐药基因
	private List<NyfxjgDto> nyfxjglist;
	private String drug_resist_gene;
	private String gzd;//关注度
	private List<String> wkbhs;//文库编号
	private String wzmc;//物种名称
	private List<String> yblxs;
	private List<MngsmxjgDto> mngsmxjgDtos;
	private List<String> znids;//指南
	private String wxids;//文献
	private String nyjgids;//耐药结果ids
	private String shry;//审核人员
	private String jsry;//接收人员
	private String fm;//分母
	private String ybbh;//样本编号
	private List<String> taxids;
	private String hbxb;// 合并性别
	private String hbnl;//合并年龄
	private String hbyblx;//合并样本类型
	private String sfqry;//是否去人源
	private String mngsmx_json;//
	private String cz;//操作
	private String sjq20;//送检Q20
	private String dna_lib_id;//生信合并DNA
	private String rna_lib_id;//生信合并RNA
	private String version;
	private String param_md5;
	private String version_dates;
	private String last_result_version;
	private String entire;
	private List<String> ysmx;
	private List<String> scmxigids;
	private List<String> jgids;
	private String btwkcxid;
	private String zszlmc;
	private List<String> nyfx;
	private List<String> dlfx;
	private String hbbbh;
	private List<String> dlfxids;
	private List<String> dlzsids;
	private String cleanreads;
	private String hostratio;
	private String lxmc;//送检类型名称：加测、复测
	//送检IDS
	private Set<String> sjids;
	//是否权限筛选
	private String  sjqxsx;
	private List<String> userids;
	private List<String> jcdws;
	private List<String> sjhbs;//伙伴限制
	private String blastType;
	private String covIndex;
	private String cnvjgid;
	private String jyry;//检验人员
	private String nbbm;//内部编码
	private String zsxm;
	private String sysmc;//实验室名称
	private String wkzs;//文库总数
	private String fbgs;//发报告数
	private String tngs;//tngs数
	private String mngs;//mngs数
	private String tjs;//特检数
	private String sjqf;//送检区分
	private String lsxh;//临时序号
	private String sfws;//是否完善
	private String sfsearch;
	public String getSfws() {
		return sfws;
	}

	public void setSfws(String sfws) {
		this.sfws = sfws;
	}

	public String getLsxh() {
		return lsxh;
	}

	public void setLsxh(String lsxh) {
		this.lsxh = lsxh;
	}

	public String getSjqf() {
		return sjqf;
	}

	public void setSjqf(String sjqf) {
		this.sjqf = sjqf;
	}

	public String getSysmc() {
		return sysmc;
	}

	public void setSysmc(String sysmc) {
		this.sysmc = sysmc;
	}


	public String getWkzs() {
		return wkzs;
	}

	public void setWkzs(String wkzs) {
		this.wkzs = wkzs;
	}

	public String getFbgs() {
		return fbgs;
	}

	public void setFbgs(String fbgs) {
		this.fbgs = fbgs;
	}

	public String getTngs() {
		return tngs;
	}

	public void setTngs(String tngs) {
		this.tngs = tngs;
	}

	public String getMngs() {
		return mngs;
	}

	public void setMngs(String mngs) {
		this.mngs = mngs;
	}

	public String getTjs() {
		return tjs;
	}

	public void setTjs(String tjs) {
		this.tjs = tjs;
	}

	public String getJyry() {
		return jyry;
	}

	public void setJyry(String jyry) {
		this.jyry = jyry;
	}

	public String getCnvjgid() {
		return cnvjgid;
	}

	public void setCnvjgid(String cnvjgid) {
		this.cnvjgid = cnvjgid;
	}

	public List<String> getSjhbs() {
		return sjhbs;
	}

	public void setSjhbs(List<String> sjhbs) {
		this.sjhbs = sjhbs;
	}

	public List<String> getJcdws() {
		return jcdws;
	}

	public void setJcdws(List<String> jcdws) {
		this.jcdws = jcdws;
	}

	public List<String> getUserids() {
		return userids;
	}

	public void setUserids(List<String> userids) {
		this.userids = userids;
	}

	public Set<String> getSjids() {
		return sjids;
	}

	public void setSjids(Set<String> sjids) {
		this.sjids = sjids;
	}

	public String getSjqxsx() {
		return sjqxsx;
	}

	public void setSjqxsx(String sjqxsx) {
		this.sjqxsx = sjqxsx;
	}

	public String getLxmc() {
		return lxmc;
	}

	public void setLxmc(String lxmc) {
		this.lxmc = lxmc;
	}

	public String getHostratio() {
		return hostratio;
	}

	public void setHostratio(String hostratio) {
		this.hostratio = hostratio;
	}

	public String getCleanreads() {
		return cleanreads;
	}

	public void setCleanreads(String cleanreads) {
		this.cleanreads = cleanreads;
	}

	public List<String> getDlzsids() {
		return dlzsids;
	}

	public void setDlzsids(List<String> dlzsids) {
		this.dlzsids = dlzsids;
	}

	public List<String> getDlfxids() {
		return dlfxids;
	}

	public void setDlfxids(List<String> dlfxids) {
		this.dlfxids = dlfxids;
	}

	public String getHbbbh() {
		return hbbbh;
	}

	public void setHbbbh(String hbbbh) {
		this.hbbbh = hbbbh;
	}

	public List<String> getNyfx() {
		return nyfx;
	}

	public void setNyfx(List<String> nyfx) {
		this.nyfx = nyfx;
	}

	public List<String> getDlfx() {
		return dlfx;
	}

	public void setDlfx(List<String> dlfx) {
		this.dlfx = dlfx;
	}

	public String getZszlmc() {
		return zszlmc;
	}

	public void setZszlmc(String zszlmc) {
		this.zszlmc = zszlmc;
	}

	public String getBtwkcxid() {
		return btwkcxid;
	}

	public void setBtwkcxid(String btwkcxid) {
		this.btwkcxid = btwkcxid;
	}

	public List<String> getJgids() {
		return jgids;
	}

	public void setJgids(List<String> jgids) {
		this.jgids = jgids;
	}

	public List<String> getScmxigids() {
		return scmxigids;
	}

	public void setScmxigids(List<String> scmxigids) {
		this.scmxigids = scmxigids;
	}

	public List<String> getYsmx() {
		return ysmx;
	}

	public void setYsmx(List<String> ysmx) {
		this.ysmx = ysmx;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLast_result_version() {
		return last_result_version;
	}

	public void setLast_result_version(String last_result_version) {
		this.last_result_version = last_result_version;
	}

	public String getVersion_dates() {
		return version_dates;
	}

	public void setVersion_dates(String version_dates) {
		this.version_dates = version_dates;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getParam_md5() {
		return param_md5;
	}

	public void setParam_md5(String param_md5) {
		this.param_md5 = param_md5;
	}

	public String getSjq20() {
		return sjq20;
	}

	public void setSjq20(String sjq20) {
		this.sjq20 = sjq20;
	}

	public String getDna_lib_id() {
		return dna_lib_id;
	}

	public void setDna_lib_id(String dna_lib_id) {
		this.dna_lib_id = dna_lib_id;
	}

	public String getRna_lib_id() {
		return rna_lib_id;
	}

	public void setRna_lib_id(String rna_lib_id) {
		this.rna_lib_id = rna_lib_id;
	}

	public String getWkbhlike() {
		return wkbhlike;
	}

	public void setWkbhlike(String wkbhlike) {
		this.wkbhlike = wkbhlike;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getMngsmx_json() {
		return mngsmx_json;
	}

	public void setMngsmx_json(String mngsmx_json) {
		this.mngsmx_json = mngsmx_json;
	}

	public String getSfqry() {
		return sfqry;
	}

	public void setSfqry(String sfqry) {
		this.sfqry = sfqry;
	}

	public String getHbxb() {
		return hbxb;
	}

	public void setHbxb(String hbxb) {
		this.hbxb = hbxb;
	}

	public String getHbnl() {
		return hbnl;
	}

	public void setHbnl(String hbnl) {
		this.hbnl = hbnl;
	}

	public String getHbyblx() {
		return hbyblx;
	}

	public void setHbyblx(String hbyblx) {
		this.hbyblx = hbyblx;
	}

	public List<String> getZnids() {
		return znids;
	}

	public void setZnids(List<String> znids) {
		this.znids = znids;
	}

	public List<String> getTaxids() {
		return taxids;
	}

	public void setTaxids(List<String> taxids) {
		this.taxids = taxids;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getFm() {
		return fm;
	}

	public void setFm(String fm) {
		this.fm = fm;
	}


	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getJsry() {
		return jsry;
	}

	public void setJsry(String jsry) {
		this.jsry = jsry;
	}


	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getRpq() {
		return rpq;
	}

	public void setRpq(String rpq) {
		this.rpq = rpq;
	}

	public String getGra() {
		return gra;
	}

	public void setGra(String gra) {
		this.gra = gra;
	}

	public String getReadsaccum() {
		return readsaccum;
	}

	public void setReadsaccum(String readsaccum) {
		this.readsaccum = readsaccum;
	}

	public List<String> getLrsjs() {
		return lrsjs;
	}

	public void setLrsjs(List<String> lrsjs) {
		this.lrsjs = lrsjs;
	}

	public String getNyjgids() {
		return nyjgids;
	}

	public void setNyjgids(String nyjgids) {
		this.nyjgids = nyjgids;
	}

	public String getWxids() {
		return wxids;
	}

	public void setWxids(String wxids) {
		this.wxids = wxids;
	}


	public List<MngsmxjgDto> getMngsmxjgDtos() {
		return mngsmxjgDtos;
	}

	public void setMngsmxjgDtos(List<MngsmxjgDto> mngsmxjgDtos) {
		this.mngsmxjgDtos = mngsmxjgDtos;
	}

	public String getDrug_resist_gene() {
		return drug_resist_gene;
	}

	public void setDrug_resist_gene(String drug_resist_gene) {
		this.drug_resist_gene = drug_resist_gene;
	}

	public List<String> getYblxs() {
		return yblxs;
	}

	public void setYblxs(List<String> yblxs) {
		this.yblxs = yblxs;
	}

	public String getWzmc() {
		return wzmc;
	}

	public void setWzmc(String wzmc) {
		this.wzmc = wzmc;
	}

	public List<String> getWkbhs() {
		return wkbhs;
	}

	public void setWkbhs(List<String> wkbhs) {
		this.wkbhs = wkbhs;
	}

	public String getGzd() {
		return gzd;
	}

	public void setGzd(String gzd) {
		this.gzd = gzd;
	}

	public List<NyfxjgDto> getNyfxjglist() {
		return nyfxjglist;
	}

	public void setNyfxjglist(List<NyfxjgDto> nyfxjglist) {
		this.nyfxjglist = nyfxjglist;
	}

	public String getNyjy() {
		return nyjy;
	}

	public void setNyjy(String nyjy) {
		this.nyjy = nyjy;
	}

	public String getXpm() {
		return xpm;
	}

	public void setXpm(String xpm) {
		this.xpm = xpm;
	}

	public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}



	public String getLibrary_type() {
		return library_type;
	}

	public void setLibrary_type(String library_type) {
		this.library_type = library_type;
	}

	public String getNlz() {
		return nlz;
	}

	public void setNlz(String nlz) {
		this.nlz = nlz;
	}

	public List<String> getZdTaxids() {
		return zdTaxids;
	}

	public void setZdTaxids(List<String> zdTaxids) {
		this.zdTaxids = zdTaxids;
	}

	public List<String> getPossibleTaxids() {
		return possibleTaxids;
	}

	public void setPossibleTaxids(List<String> possibleTaxids) {
		this.possibleTaxids = possibleTaxids;
	}

	public List<String> getBackgroundTaxids() {
		return backgroundTaxids;
	}

	public void setBackgroundTaxids(List<String> backgroundTaxids) {
		this.backgroundTaxids = backgroundTaxids;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getSample_id() {
		return sample_id;
	}

	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}

	public String getDetection_type() {
		return detection_type;
	}

	public void setDetection_type(String detection_type) {
		this.detection_type = detection_type;
	}


	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	public List<String> getZts() {
		return zts;
	}

	public void setZts(List<String> zts) {
		this.zts = zts;
	}

	public String getCxkssjstart() {
		return cxkssjstart;
	}

	public void setCxkssjstart(String cxkssjstart) {
		this.cxkssjstart = cxkssjstart;
	}

	public String getCxkssjend() {
		return cxkssjend;
	}

	public void setCxkssjend(String cxkssjend) {
		this.cxkssjend = cxkssjend;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getRawreads() {
		return rawreads;
	}

	public void setRawreads(String rawreads) {
		this.rawreads = rawreads;
	}

	public String getTotalreads() {
		return totalreads;
	}

	public void setTotalreads(String totalreads) {
		this.totalreads = totalreads;
	}

	public String getHomo() {
		return homo;
	}

	public void setHomo(String homo) {
		this.homo = homo;
	}

	public String getBacteria() {
		return bacteria;
	}

	public void setBacteria(String bacteria) {
		this.bacteria = bacteria;
	}

	public String getFungi() {
		return fungi;
	}

	public void setFungi(String fungi) {
		this.fungi = fungi;
	}

	public String getViruses() {
		return viruses;
	}

	public void setViruses(String viruses) {
		this.viruses = viruses;
	}

	public String getParasite() {
		return parasite;
	}

	public void setParasite(String parasite) {
		this.parasite = parasite;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getUnmapped() {
		return unmapped;
	}

	public void setUnmapped(String unmapped) {
		this.unmapped = unmapped;
	}

	public String getSpikein() {
		return spikein;
	}

	public void setSpikein(String spikein) {
		this.spikein = spikein;
	}

	public String getSpikeinrpm() {
		return spikeinrpm;
	}

	public void setSpikeinrpm(String spikeinrpm) {
		this.spikeinrpm = spikeinrpm;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getSpike() {
		return spike;
	}

	public void setSpike(String spike) {
		this.spike = spike;
	}

	public String getRyzs() {
		return ryzs;
	}

	public void setRyzs(String ryzs) {
		this.ryzs = ryzs;
	}

	public String getRypw() {
		return rypw;
	}

	public void setRypw(String rypw) {
		this.rypw = rypw;
	}

	public String getSfzwqckj() {
		return sfzwqckj;
	}

	public void setSfzwqckj(String sfzwqckj) {
		this.sfzwqckj = sfzwqckj;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public List<String> getXgwxTaxids() {
		return xgwxTaxids;
	}

	public void setXgwxTaxids(List<String> xgwxTaxids) {
		this.xgwxTaxids = xgwxTaxids;
	}

	public List<String> getZsxxIds() {
		return zsxxIds;
	}

	public void setZsxxIds(List<String> zsxxIds) {
		this.zsxxIds = zsxxIds;
	}

	public List<String> getWswTaxids() {
		return wswTaxids;
	}

	public void setWswTaxids(List<String> wswTaxids) {
		this.wswTaxids = wswTaxids;
	}

	public List<String> getYsTaxids() {
		return ysTaxids;
	}

	public void setYsTaxids(List<String> ysTaxids) {
		this.ysTaxids = ysTaxids;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getStatsType() {
		return statsType;
	}

	public void setStatsType(String statsType) {
		this.statsType = statsType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBlastType() {
		return blastType;
	}

	public void setBlastType(String blastType) {
		this.blastType = blastType;
	}

	public String getCovIndex() {
		return covIndex;
	}

	public void setCovIndex(String covIndex) {
		this.covIndex = covIndex;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getSfsearch() {
		return sfsearch;
	}

	public void setSfsearch(String sfsearch) {
		this.sfsearch = sfsearch;
	}
}
