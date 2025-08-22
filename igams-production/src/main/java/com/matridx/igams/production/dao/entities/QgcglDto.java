package com.matridx.igams.production.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="QgcglDto")
public class QgcglDto extends QgcglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		//物料编码
		private String wlbm;
		//物料类别
		private String wllb;
		//物料子类别
		private String wlzlb;
		//物料名称
		private String wlmc;
		//代理供应商
		private String dlgys;
		//审核时间
		private String shsj;
		//生产商
		private String scs;
		//原厂货号
		private String ychh;
		//规格
		private String gg;
		//计量单位
		private String jldw;
		//保存条件
		private String bctj;
		//保质期
		private String bzq;
		//保质期标记
		private String bzqflg;
		//是否危险品
		private String sfwxp;
		//备注
		private String bz;
		//审核状态   00:未提交 10:审核中  15:审核未通过  80:审核通过
		private String zt;
		//旧物料编码
		private String jwlbm;
		//类别
		private String lb;
		//安全库存
		private String aqkc;
		//起订量
		private String qdl;
		//货期
		private String hq;
		//最低采购量
		private String zdcgl;
		//物料子类别统称
		private String wlzlbtc;
		//物料类别名称
		private String wllbmc;
		//物料子类别名称
		private String wlzlbmc;
		//物料子类别统称名称
		private String wlzlbtcmc;
		//物料编码s
		private List<String> wlbms;
		//物料子类别参数扩展1
		private String cskz1;
		//请购类别代码
		private String qglbdm;
		//物料子类别参数扩展2
		private String cskz2;
		//物料库存量
		private String wlkcl;
		//产品注册号
		private String cpzch;

	public String getCpzch() {
		return cpzch;
	}

	public void setCpzch(String cpzch) {
		this.cpzch = cpzch;
	}

	public String getWlkcl() {
		return wlkcl;
	}

	public void setWlkcl(String wlkcl) {
		this.wlkcl = wlkcl;
	}

	public String getCskz2() {
			return cskz2;
		}

		public void setCskz2(String cskz2) {
			this.cskz2 = cskz2;
		}

		public String getQglbdm() {
				return qglbdm;
			}
		public void setQglbdm(String qglbdm) {
			this.qglbdm = qglbdm;
		}
		public String getCskz1() {
			return cskz1;
		}
		public void setCskz1(String cskz1) {
			this.cskz1 = cskz1;
		}
		public List<String> getWlbms() {
			return wlbms;
		}
		public void setWlbms(List<String> wlbms) {
			this.wlbms = wlbms;
		}
		public String getWllbmc() {
			return wllbmc;
		}
		public void setWllbmc(String wllbmc) {
			this.wllbmc = wllbmc;
		}
		public String getWlzlbmc() {
			return wlzlbmc;
		}
		public void setWlzlbmc(String wlzlbmc) {
			this.wlzlbmc = wlzlbmc;
		}
		public String getWlzlbtcmc() {
			return wlzlbtcmc;
		}
		public void setWlzlbtcmc(String wlzlbtcmc) {
			this.wlzlbtcmc = wlzlbtcmc;
		}
		public String getWlzlbtc() {
			return wlzlbtc;
		}
		public void setWlzlbtc(String wlzlbtc) {
			this.wlzlbtc = wlzlbtc;
		}
		public String getWlbm() {
			return wlbm;
		}
		public void setWlbm(String wlbm) {
			this.wlbm = wlbm;
		}
		public String getWllb() {
			return wllb;
		}
		public void setWllb(String wllb) {
			this.wllb = wllb;
		}
		public String getWlzlb() {
			return wlzlb;
		}
		public void setWlzlb(String wlzlb) {
			this.wlzlb = wlzlb;
		}
		public String getWlmc() {
			return wlmc;
		}
		public void setWlmc(String wlmc) {
			this.wlmc = wlmc;
		}
		public String getDlgys() {
			return dlgys;
		}
		public void setDlgys(String dlgys) {
			this.dlgys = dlgys;
		}
		public String getShsj() {
			return shsj;
		}
		public void setShsj(String shsj) {
			this.shsj = shsj;
		}
		public String getScs() {
			return scs;
		}
		public void setScs(String scs) {
			this.scs = scs;
		}
		public String getYchh() {
			return ychh;
		}
		public void setYchh(String ychh) {
			this.ychh = ychh;
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
		public String getBctj() {
			return bctj;
		}
		public void setBctj(String bctj) {
			this.bctj = bctj;
		}
		public String getBzq() {
			return bzq;
		}
		public void setBzq(String bzq) {
			this.bzq = bzq;
		}
		public String getBzqflg() {
			return bzqflg;
		}
		public void setBzqflg(String bzqflg) {
			this.bzqflg = bzqflg;
		}
		public String getSfwxp() {
			return sfwxp;
		}
		public void setSfwxp(String sfwxp) {
			this.sfwxp = sfwxp;
		}
		public String getBz() {
			return bz;
		}
		public void setBz(String bz) {
			this.bz = bz;
		}
		public String getZt() {
			return zt;
		}
		public void setZt(String zt) {
			this.zt = zt;
		}
		public String getJwlbm() {
			return jwlbm;
		}
		public void setJwlbm(String jwlbm) {
			this.jwlbm = jwlbm;
		}
		public String getLb() {
			return lb;
		}
		public void setLb(String lb) {
			this.lb = lb;
		}
		public String getAqkc() {
			return aqkc;
		}
		public void setAqkc(String aqkc) {
			this.aqkc = aqkc;
		}
		public String getQdl() {
			return qdl;
		}
		public void setQdl(String qdl) {
			this.qdl = qdl;
		}
		public String getHq() {
			return hq;
		}
		public void setHq(String hq) {
			this.hq = hq;
		}
		public String getZdcgl() {
			return zdcgl;
		}
		public void setZdcgl(String zdcgl) {
			this.zdcgl = zdcgl;
		}
		
}
