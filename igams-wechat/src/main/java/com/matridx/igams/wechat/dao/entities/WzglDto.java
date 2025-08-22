package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WzglDto")
public class WzglDto extends WzglModel{

	//患者姓名
	private String hzxm;
	//送检单位
	private String sjdw;
	//结果类型
	private String jglx;
	//报告日期
	private String bgrq;
	//内部编码
	private String nbbm;
	//合作伙伴
	private String sjhb;
	//标本编号
	private String ybbh;
	//模糊查询
	private String entire;
	//报告开始日期
	private String bgrqstart;
	//报告结束日期
	private String bgrqend;
	//代表
	private String db;
	//拦截ID
	private String ljid;
	//送检ID
	private String sjid;
	//性别
	private String xb;
	//科室
	private String ks;
	private String wzmc;//物种名称，可能是wzid ，也可能是物种的中文或英文
	private String wkbh;
	private String bbh;
	private String gzd;
	private String rank_code;
	private String name;
	private String cn_name;
	private String species_category;
	private String genus_taxid;
	private String genus_name;
	private String genus_cn_name;
	private String sp_type;
	private String highlight;
	private String comment;
	private String last_update;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getLast_update() {
		return last_update;
	}

	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	public String getRank_code() {
		return rank_code;
	}

	public void setRank_code(String rank_code) {
		this.rank_code = rank_code;
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

	public String getSpecies_category() {
		return species_category;
	}

	public void setSpecies_category(String species_category) {
		this.species_category = species_category;
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

	public String getSp_type() {
		return sp_type;
	}

	public void setSp_type(String sp_type) {
		this.sp_type = sp_type;
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

	public String getWzmc() {
		return wzmc;
	}

	public void setWzmc(String wzmc) {
		this.wzmc = wzmc;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getGzd() {
		return gzd;
	}

	public void setGzd(String gzd) {
		this.gzd = gzd;
	}

	public String getKs() {
		return ks;
	}

	public void setKs(String ks) {
		this.ks = ks;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getLjid() {
		return ljid;
	}

	public void setLjid(String ljid) {
		this.ljid = ljid;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
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

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getJglx() {
		return jglx;
	}

	public void setJglx(String jglx) {
		this.jglx = jglx;
	}


	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getSjhb() {
		return sjhb;
	}

	public void setSjhb(String sjhb) {
		this.sjhb = sjhb;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
}
