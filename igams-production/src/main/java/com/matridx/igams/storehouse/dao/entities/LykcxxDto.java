package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="LykcxxDto")
public class LykcxxDto extends LykcxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wlbm;//物料编码
	private String wlmc;//物料名称
	private String bctj;//保存条件
	private String gg;//规格
	private String jldw;//单位
	private String ychh;//原厂货号
	private String scph;//生产批号
	private String cssjstart;
	private String cssjend;
	private String yxqstart;
	private String yxqsjend;
	private String kclbj;//库存量加减标记
	private String qyr;
	private String qyrmc;
	//附件IDS
	private List<String> fjids;
	private String entire;
	private String kczt;//库存状态
	private String sflyxj;//留样小结
	private String scgcrqstart;//上次观察日期开始
	private String scgcrqend;//上次观察日期结束
	private String gcjl;//观察记录

	public String getGcjl() {
		return gcjl;
	}

	public void setGcjl(String gcjl) {
		this.gcjl = gcjl;
	}

	public String getScgcrqstart() {
		return scgcrqstart;
	}

	public void setScgcrqstart(String scgcrqstart) {
		this.scgcrqstart = scgcrqstart;
	}

	public String getScgcrqend() {
		return scgcrqend;
	}

	public void setScgcrqend(String scgcrqend) {
		this.scgcrqend = scgcrqend;
	}

	public String getKczt() {
		return kczt;
	}

	public void setKczt(String kczt) {
		this.kczt = kczt;
	}

	public String getSflyxj() {
		return sflyxj;
	}

	public void setSflyxj(String sflyxj) {
		this.sflyxj = sflyxj;
	}

	public String getQyrmc() {
		return qyrmc;
	}

	public void setQyrmc(String qyrmc) {
		this.qyrmc = qyrmc;
	}

	public String getQyr() {
		return qyr;
	}

	public void setQyr(String qyr) {
		this.qyr = qyr;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getKclbj() {
		return kclbj;
	}

	public void setKclbj(String kclbj) {
		this.kclbj = kclbj;
	}

	public String getCssjstart() {
		return cssjstart;
	}

	public void setCssjstart(String cssjstart) {
		this.cssjstart = cssjstart;
	}

	public String getCssjend() {
		return cssjend;
	}

	public void setCssjend(String cssjend) {
		this.cssjend = cssjend;
	}

	public String getYxqstart() {
		return yxqstart;
	}

	public void setYxqstart(String yxqstart) {
		this.yxqstart = yxqstart;
	}

	public String getYxqsjend() {
		return yxqsjend;
	}

	public void setYxqsjend(String yxqsjend) {
		this.yxqsjend = yxqsjend;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
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

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	@Override
	public String getScph() {
		return scph;
	}

	@Override
	public void setScph(String scph) {
		this.scph = scph;
	}
}
