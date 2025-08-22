package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "MngsmxjgModel")
public class MngsmxjgModel extends BaseModel {

    private static final long serialVersionUID = 1L;
    private String mxjgid;
	private String jgid;
    private String wkbh;
    private String taxid;
    private String readscount;
    private String readsaccum;
    private String gzd;
    private String aijg;
    private String bbh;
    private String head;
    private String qz;
    private String qzpw;
    private String fgdxx;
	//具体序列信息
    private String jtxlxx;
    private String sfwswgs;
    private String wzlx;
    private String fldj;
    private String xm;
    private String zwmm;
    private String rpq;
    private String abd;
    private String grc;
    private String frequency;
    private String fl;
    private String sfgz;
    private String wzfl;
    private String fid;
    private String xh;
    private String ftaxid;
    private String wkcxid;
    private String wzdl;
	private String xpid;
	private String fz;
	private String xjlb;
	private String bdlb;
	private String zbx;
	private String cj;
	private String scid;
	private String gid;
	private String sfdc;
	private String dep;
	private String adjusted;
	private String wzzs;
	private String sfys;
	private String bjtppath;
	private String bg;
	private String fx;//分型
	public String getXjlb() {
		return xjlb;
	}

	public void setXjlb(String xjlb) {
		this.xjlb = xjlb;
	}

	public String getBdlb() {
		return bdlb;
	}

	public void setBdlb(String bdlb) {
		this.bdlb = bdlb;
	}

	public String getFz() {
		return fz;
	}

	public void setFz(String fz) {
		this.fz = fz;
	}

	public String getXpid() {
		return xpid;
	}

	public void setXpid(String xpid) {
		this.xpid = xpid;
	}

	public String getWzdl() {
		return wzdl;
	}

	public void setWzdl(String wzdl) {
		this.wzdl = wzdl;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getTaxid() {
		return taxid;
	}

	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}

	public String getSfwswgs() {
		return sfwswgs;
	}

	public void setSfwswgs(String sfwswgs) {
		this.sfwswgs = sfwswgs;
	}

	public String getWzlx() {
		return wzlx;
	}

	public void setWzlx(String wzlx) {
		this.wzlx = wzlx;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getZwmm() {
		return zwmm;
	}

	public void setZwmm(String zwmm) {
		this.zwmm = zwmm;
	}

	public String getSfgz() {
		return sfgz;
	}

	public void setSfgz(String sfgz) {
		this.sfgz = sfgz;
	}

	public String getWzfl() {
		return wzfl;
	}

	public void setWzfl(String wzfl) {
		this.wzfl = wzfl;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getMxjgid() {
		return mxjgid;
	}

	public void setMxjgid(String mxjgid) {
		this.mxjgid = mxjgid;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getReadscount() {
		return readscount;
	}

	public void setReadscount(String readscount) {
		this.readscount = readscount;
	}

	public String getReadsaccum() {
		return readsaccum;
	}

	public void setReadsaccum(String readsaccum) {
		this.readsaccum = readsaccum;
	}

	public String getGzd() {
		return gzd;
	}

	public void setGzd(String gzd) {
		this.gzd = gzd;
	}

	public String getAijg() {
		return aijg;
	}

	public void setAijg(String aijg) {
		this.aijg = aijg;
	}

	public String getBbh() {
		return bbh;
	}

	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getQz() {
		return qz;
	}

	public void setQz(String qz) {
		this.qz = qz;
	}

	public String getQzpw() {
		return qzpw;
	}

	public void setQzpw(String qzpw) {
		this.qzpw = qzpw;
	}

	public String getFgdxx() {
		return fgdxx;
	}

	public void setFgdxx(String fgdxx) {
		this.fgdxx = fgdxx;
	}

	public String getJtxlxx() {
		return jtxlxx;
	}

	public void setJtxlxx(String jtxlxx) {
		this.jtxlxx = jtxlxx;
	}

	public String getFldj() {
		return fldj;
	}

	public void setFldj(String fldj) {
		this.fldj = fldj;
	}

	public String getRpq() {
		return rpq;
	}

	public void setRpq(String rpq) {
		this.rpq = rpq;
	}

	public String getAbd() {
		return abd;
	}

	public void setAbd(String abd) {
		this.abd = abd;
	}

	public String getGrc() {
		return grc;
	}

	public void setGrc(String grc) {
		this.grc = grc;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFtaxid() {
		return ftaxid;
	}

	public void setFtaxid(String ftaxid) {
		this.ftaxid = ftaxid;
	}

	public String getZbx() {
		return zbx;
	}

	public void setZbx(String zbx) {
		this.zbx = zbx;
	}

	public String getCj() {
		return cj;
	}

	public void setCj(String cj) {
		this.cj = cj;
	}

	public String getScid() {
		return scid;
	}

	public void setScid(String scid) {
		this.scid = scid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSfdc() {
		return sfdc;
	}

	public void setSfdc(String sfdc) {
		this.sfdc = sfdc;
	}

	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public String getAdjusted() {
		return adjusted;
	}

	public void setAdjusted(String adjusted) {
		this.adjusted = adjusted;
	}

	public String getWzzs() {
		return wzzs;
	}

	public void setWzzs(String wzzs) {
		this.wzzs = wzzs;
	}

	public String getSfys() {
		return sfys;
	}

	public void setSfys(String sfys) {
		this.sfys = sfys;
	}

	public String getBjtppath() {
		return bjtppath;
	}

	public void setBjtppath(String bjtppath) {
		this.bjtppath = bjtppath;
	}

	public String getFx() {
		return fx;
	}

	public void setFx(String fx) {
		this.fx = fx;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}
}
