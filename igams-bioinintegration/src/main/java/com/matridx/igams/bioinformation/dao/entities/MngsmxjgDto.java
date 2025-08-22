package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;
import java.util.List;

@Alias(value = "MngsmxjgDto")
public class MngsmxjgDto extends MngsmxjgModel implements Cloneable{
    private static final long serialVersionUID = 1L;

    private List<MngsmxjgDto> children;
    private String dl;
    private String xl;
    private String num;
    private String g_num;
    //送检区分字段
    private String sjqf;
    private String genus_taxid;//父参数的taxid
    private String genus_name;//父参数的xm
    private String genus_cn_name;//父参数中文名
    private String genus_reads_accum;//父参数的reads_accum
    private String genus_abundance;//父参数的reads_accum
    private String q_position;
    private String zdz;//最大值
    private String zxz;//最小值
    private String highlight;//是否高亮
    private String coverage;
    private String coverlength;
    private String comment;
    private String depth;
    private String mapped_reads;
    private String map_ratio;
    private String accum;
    private String read_length;
    private String ref_length;
    private String library_type;
    private String abundance;
    private String name;
    private String cn_name;
    private String cov;
    private String reads_count;
    private String rank_code;
    private String sp_type;
    private String species_category;
    private String last_update;
    private String q_index;
    private String q_percentile;
    private List<MngsmxjgDto> pathogen;//重点关注微生物列表
    private List<MngsmxjgDto> possible;//关注微生物列表
    private List<MngsmxjgDto> background;//疑似背景微生物列表
    private List<String> wswTaxids;//微生物
    private List<String> times;//分表时候存储表名时间后缀
    private String fl;//0核心1扩展
    private String ryzs;
    private String zbx1;
    private String aijg1;
    private String aijg2;
    private String noai;
    private String noai1;
    private List<MngsmxjgDto> list;
    private List<MngsmxjgDto> list1;
    private String fxbb;
    private String version_dates;
    private String ybbbh;
    private String yblx;
    private String fwzlx;
    private String freadsaccum;
    private String fabd;
    private String fxm;
    private String fzwmm;
    private String wzywm;
    private String wzzwm;
    private String ratio;
    private String index;
    private String fmxjgid;
    private String fqz;
    private String fqzpw;
    private String fgzd;
    private String fwzfl;
    private String fbdlb;
    private String lssj;
    private String wkbhlike;
    private String staxid;
    private String thismonth;
    private String lastmonth;
    private String barcode;
    private String rnaflag;
    private String dnaflag;
    private String ysindex;
    private List<String> sysids;
    //查询条件(全部)
    private String entire;
    private String clade;

    public String getClade() {
        return clade;
    }

    public void setClade(String clade) {
        this.clade = clade;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public List<String> getSysids() {
        return sysids;
    }

    public void setSysids(List<String> sysids) {
        this.sysids = sysids;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getThismonth() {
        return thismonth;
    }

    public void setThismonth(String thismonth) {
        this.thismonth = thismonth;
    }

    public String getLastmonth() {
        return lastmonth;
    }

    public void setLastmonth(String lastmonth) {
        this.lastmonth = lastmonth;
    }

    public String getWkbhlike() {
        return wkbhlike;
    }

    public void setWkbhlike(String wkbhlike) {
        this.wkbhlike = wkbhlike;
    }

    public String getStaxid() {
        return staxid;
    }

    public void setStaxid(String staxid) {
        this.staxid = staxid;
    }

    public String getFwzfl() {
        return fwzfl;
    }

    public void setFwzfl(String fwzfl) {
        this.fwzfl = fwzfl;
    }

    public String getFbdlb() {
        return fbdlb;
    }

    public void setFbdlb(String fbdlb) {
        this.fbdlb = fbdlb;
    }

    public String getFgzd() {
        return fgzd;
    }

    public void setFgzd(String fgzd) {
        this.fgzd = fgzd;
    }

    public String getFmxjgid() {
        return fmxjgid;
    }

    public void setFmxjgid(String fmxjgid) {
        this.fmxjgid = fmxjgid;
    }

    public String getFqz() {
        return fqz;
    }

    public void setFqz(String fqz) {
        this.fqz = fqz;
    }

    public String getFqzpw() {
        return fqzpw;
    }

    public void setFqzpw(String fqzpw) {
        this.fqzpw = fqzpw;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getWzywm() {
        return wzywm;
    }

    public void setWzywm(String wzywm) {
        this.wzywm = wzywm;
    }

    public String getWzzwm() {
        return wzzwm;
    }

    public void setWzzwm(String wzzwm) {
        this.wzzwm = wzzwm;
    }

    public String getFwzlx() {
        return fwzlx;
    }

    public void setFwzlx(String fwzlx) {
        this.fwzlx = fwzlx;
    }

    public String getFreadsaccum() {
        return freadsaccum;
    }

    public void setFreadsaccum(String freadsaccum) {
        this.freadsaccum = freadsaccum;
    }

    public String getFabd() {
        return fabd;
    }

    public void setFabd(String fabd) {
        this.fabd = fabd;
    }

    public String getFxm() {
        return fxm;
    }

    public void setFxm(String fxm) {
        this.fxm = fxm;
    }

    public String getFzwmm() {
        return fzwmm;
    }

    public void setFzwmm(String fzwmm) {
        this.fzwmm = fzwmm;
    }

    public String getYblx() {
        return yblx;
    }

    public void setYblx(String yblx) {
        this.yblx = yblx;
    }

    public String getYbbbh() {
        return ybbbh;
    }

    public void setYbbbh(String ybbbh) {
        this.ybbbh = ybbbh;
    }

    public String getVersion_dates() {
        return version_dates;
    }

    public void setVersion_dates(String version_dates) {
        this.version_dates = version_dates;
    }

    public String getFxbb() {
        return fxbb;
    }

    public void setFxbb(String fxbb) {
        this.fxbb = fxbb;
    }

    public List<MngsmxjgDto> getList() {
        return list;
    }

    public void setList(List<MngsmxjgDto> list) {
        this.list = list;
    }

    public String getCov() {
        return cov;
    }

    public void setCov(String cov) {
        this.cov = cov;
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

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getG_num() {
        return g_num;
    }

    public void setG_num(String g_num) {
        this.g_num = g_num;
    }

    public String getRank_code() {
        return rank_code;
    }

    public void setRank_code(String rank_code) {
        this.rank_code = rank_code;
    }

    public String getSp_type() {
        return sp_type;
    }

    public void setSp_type(String sp_type) {
        this.sp_type = sp_type;
    }

    public String getSpecies_category() {
        return species_category;
    }

    public void setSpecies_category(String species_category) {
        this.species_category = species_category;
    }

    public String getAbundance() {
        return abundance;
    }

    public void setAbundance(String abundance) {
        this.abundance = abundance;
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

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> getWswTaxids() {
        return wswTaxids;
    }

    public void setWswTaxids(List<String> wswTaxids) {
        this.wswTaxids = wswTaxids;
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

    public String getQ_position() {
        return q_position;
    }

    public void setQ_position(String q_position) {
        this.q_position = q_position;
    }

    public String getZdz() {
        return zdz;
    }

    public void setZdz(String zdz) {
        this.zdz = zdz;
    }

    public String getZxz() {
        return zxz;
    }

    public void setZxz(String zxz) {
        this.zxz = zxz;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getCoverlength() {
        return coverlength;
    }

    public void setCoverlength(String coverlength) {
        this.coverlength = coverlength;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getMapped_reads() {
        return mapped_reads;
    }

    public void setMapped_reads(String mapped_reads) {
        this.mapped_reads = mapped_reads;
    }

    public String getMap_ratio() {
        return map_ratio;
    }

    public void setMap_ratio(String map_ratio) {
        this.map_ratio = map_ratio;
    }

    public String getAccum() {
        return accum;
    }

    public void setAccum(String accum) {
        this.accum = accum;
    }

    public String getRead_length() {
        return read_length;
    }

    public void setRead_length(String read_length) {
        this.read_length = read_length;
    }

    public String getRef_length() {
        return ref_length;
    }

    public void setRef_length(String ref_length) {
        this.ref_length = ref_length;
    }

    public String getLibrary_type() {
        return library_type;
    }

    public void setLibrary_type(String library_type) {
        this.library_type = library_type;
    }

    public List<MngsmxjgDto> getPathogen() {
        return pathogen;
    }

    public void setPathogen(List<MngsmxjgDto> pathogen) {
        this.pathogen = pathogen;
    }

    public List<MngsmxjgDto> getPossible() {
        return possible;
    }

    public void setPossible(List<MngsmxjgDto> possible) {
        this.possible = possible;
    }

    public List<MngsmxjgDto> getBackground() {
        return background;
    }

    public void setBackground(List<MngsmxjgDto> background) {
        this.background = background;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }



    public List<MngsmxjgDto> getChildren() {
        return children;
    }

    public void setChildren(List<MngsmxjgDto> children) {
        this.children = children;
    }

    public String getSjqf() {
        return sjqf;
    }

    public void setSjqf(String sjqf) {
        this.sjqf = sjqf;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getRyzs() {
        return ryzs;
    }

    public void setRyzs(String ryzs) {
        this.ryzs = ryzs;
    }

    public String getZbx1() {
        return zbx1;
    }

    public void setZbx1(String zbx1) {
        this.zbx1 = zbx1;
    }

    public String getAijg1() {
        return aijg1;
    }

    public void setAijg1(String aijg1) {
        this.aijg1 = aijg1;
    }

    public String getNoai() {
        return noai;
    }

    public void setNoai(String noai) {
        this.noai = noai;
    }

    public String getNoai1() {
        return noai1;
    }

    public void setNoai1(String noai1) {
        this.noai1 = noai1;
    }

    public String getAijg2() {
        return aijg2;
    }

    public void setAijg2(String aijg2) {
        this.aijg2 = aijg2;
    }

    @Override
    public MngsmxjgDto clone() throws CloneNotSupportedException {
        return (MngsmxjgDto) super.clone();
    }

    public List<MngsmxjgDto> getList1() {
        return list1;
    }

    public void setList1(List<MngsmxjgDto> list1) {
        this.list1 = list1;
    }

    public String getLssj() {
        return lssj;
    }

    public void setLssj(String lssj) {
        this.lssj = lssj;
    }

    public String getDnaflag() {
        return dnaflag;
    }

    public void setDnaflag(String dnaflag) {
        this.dnaflag = dnaflag;
    }

    public String getRnaflag() {
        return rnaflag;
    }

    public void setRnaflag(String rnaflag) {
        this.rnaflag = rnaflag;
    }

    public String getYsindex() {
        return ysindex;
    }

    public void setYsindex(String ysindex) {
        this.ysindex = ysindex;
    }
}
