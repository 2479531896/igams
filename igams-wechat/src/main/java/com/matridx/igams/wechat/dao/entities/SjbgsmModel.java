package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjbgsmModel")
public class SjbgsmModel extends BaseModel{
	//送检ID
	private String sjid;
	//高关注度说明
	private String ggzdsm;
	//疑似说明
	private String yssm;
	//高关注度指标
	private String ggzdzb;
	//疑似指标
	private String yszb;
	//参考文献
	private String ckwx;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//报告日期
	private String bgrq;
	//人源指数
	private String ryzs;
	//人源指数百分比
	private String ryzsbfb;
	//人源指数位置
	private String ryzswz;
	//文库编号
	private String wkbh;
	//阈值
	private String spikerpm;
	//GC含量
	private String gc;
	//总序列数
	private String zxls;
	//宿主序列百分比
	private String szxlbfb;
	//微生物检出序列数
	private String wswjcxls;
	//有效序列数
	private String yxxls;
	//参数扩展1
	private String cskz1;
	//参数扩展2
	private String cskz2;
	//参数扩展3
	private String cskz3;
	//分子核型结果
	private String fzhxjg;
	//检验人员
	private String jyry;
	//审核人员
	private String shry;

	//检验结果
	private String jyjg;
	//rpm
	private String sprpm;
	private String scbgrq;//首次报告日期
	//定值指标
	private String dzzb;
	//定值说明
	private String dzsm;

	public String getScbgrq() {
		return scbgrq;
	}

	public void setScbgrq(String scbgrq) {
		this.scbgrq = scbgrq;
	}

	public String getSprpm() {
		return sprpm;
	}

	public void setSprpm(String sprpm) {
		this.sprpm = sprpm;
	}

	public String getJyjg() {
		return jyjg;
	}

	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
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

	public String getFzhxjg() {
		return fzhxjg;
	}

	public void setFzhxjg(String fzhxjg) {
		this.fzhxjg = fzhxjg;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
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

	public String getYxxls() {
		return yxxls;
	}

	public void setYxxls(String yxxls) {
		this.yxxls = yxxls;
	}

	public String getGc() {
		return gc;
	}

	public void setGc(String gc) {
		this.gc = gc;
	}

	public String getZxls() {
		return zxls;
	}

	public void setZxls(String zxls) {
		this.zxls = zxls;
	}

	public String getSzxlbfb() {
		return szxlbfb;
	}

	public void setSzxlbfb(String szxlbfb) {
		this.szxlbfb = szxlbfb;
	}

	public String getWswjcxls() {
		return wswjcxls;
	}

	public void setWswjcxls(String wswjcxls) {
		this.wswjcxls = wswjcxls;
	}

	public String getSpikerpm() {
		return spikerpm;
	}

	public void setSpikerpm(String spikerpm) {
		this.spikerpm = spikerpm;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}
	public String getRyzswz() {
		return ryzswz;
	}
	public void setRyzswz(String ryzswz) {
		this.ryzswz = ryzswz;
	}
	public String getRyzs() {
		return ryzs;
	}
	public void setRyzs(String ryzs) {
		this.ryzs = ryzs;
	}
	public String getRyzsbfb() {
		return ryzsbfb;
	}
	public void setRyzsbfb(String ryzsbfb) {
		this.ryzsbfb = ryzsbfb;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//高关注度说明
	public String getGgzdsm() {
		return ggzdsm;
	}
	public void setGgzdsm(String ggzdsm){
		this.ggzdsm = ggzdsm;
	}
	//疑似说明
	public String getYssm() {
		return yssm;
	}
	public void setYssm(String yssm){
		this.yssm = yssm;
	}
	//高关注度指标
	public String getGgzdzb() {
		return ggzdzb;
	}
	public void setGgzdzb(String ggzdzb){
		this.ggzdzb = ggzdzb;
	}
	//疑似指标
	public String getYszb() {
		return yszb;
	}
	public void setYszb(String yszb){
		this.yszb = yszb;
	}
	//参考文献
	public String getCkwx() {
		return ckwx;
	}
	public void setCkwx(String ckwx){
		this.ckwx = ckwx;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	public String getBgrq() {
		return bgrq;
	}
	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getDzzb() {
		return dzzb;
	}

	public void setDzzb(String dzzb) {
		this.dzzb = dzzb;
	}

	public String getDzsm() {
		return dzsm;
	}

	public void setDzsm(String dzsm) {
		this.dzsm = dzsm;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
