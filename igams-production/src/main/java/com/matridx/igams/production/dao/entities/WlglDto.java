package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WlglDto")
public class WlglDto extends WlglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String wllbdm;	//物料类别代码
	private String wllbmc;	//物料类别名称
	private String lbmc;//物料质量类别名称
	private String wlbm;//物料编码
	private String lbdm;	//物料质量类别代码
	private String wlzlbdm;	//物料子类别代码
	private String wlzlbmc;	//物料子类别名称
	private String shsjstart;	//审核通过开始时间
	private String shsjend;		//审核通过结束时间
	private String lrryxm;		//录入人员姓名
	private String[] wllbs;	//检索用物料类别（多）
	private String[] wlzlbs;	//检索用物料子类别（多）
	private String[] sfwxps;	//检索用是否危险品（多）
	private String[] scbjs;	//检索用是否停用（多）
	private String[] zts;	//检索物料状态（多）
	private String[] wlzlbtcs;//检索物料子类别统称
	private String[] lbs; //检索库存类别
	private String auditModType; //审核物料修改类别
	private String lsid;	//物料临时表ID
	private String wlzt; //物料状态
	private String qxqg;  //取消请购
	private String sfwxpmc; //是否危险品mc
	private String sscplbmc;//所属产品类别名称
	private String sscplbdm;//所属产品类别代码
	private String[] sscplbs;	//所属产品类别（多）
	private String abclbmc;//abclb
	private String abclbdm;//abclb
	private String zjwlid;
	private String index;
	private String flag;//用于区分统计
	private String lx;//用于区分不同的物料
	private String lbcskz1;//物料质量类别参数扩展1
	private String syncFlag;//物料同步标记
	private String yhid;

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	private String sjxmhmc;

	public String getSjxmhmc() {
		return sjxmhmc;
	}

	public void setSjxmhmc(String sjxmhmc) {
		this.sjxmhmc = sjxmhmc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	//导出关联标记位
	//所选择的字段
	private String SqlParam;
	//物料类别基础数据
	private String wllb_flg;
	//物料子类别基础数据
	private String wlzlb_flg;
	//物料质量类别基础数据
	private String wlzllb_flg;
	//全部(查询条件)
	private String entire;
	//已放入采购车内物料种类数量
	private String ycgzlsl;
	//已放入采购车的物料
	private String qg_ids;
	//前缀
	private String prefix;
	//物料子类别统称代码
	private String wlzlbtcdm;
	//修改人真实姓名
	private String xgryzsxm;
	//参数扩展1(物料子类别cskz1)
	private String cskz1;
	private String cskz2;//物料子类别cskz2
	private String qgid;//请购id
	private String xmbm;//项目编码
	private String xmdl;//项目大类
	private String kcl;//库存量
	private String ckhwid;
	private String yds;//预定数
	//使用状态
	private String syzt;
	//是否贵重
	private String sfgz;
	private String scrq;
	private String ckmc;
	private String yxq;
	private String ckid;
	private String gylx;
	//路径标记
	private String ljbj;
	//物料库存量
	private String wlkcl;
	//供应商名称
	private String gysmc;
	//是否设置
	private String sfsz;
	private String cksl;
	private String szid;

	public String getSzid() {
		return szid;
	}

	public void setSzid(String szid) {
		this.szid = szid;
	}

	public String getCksl() {
		return cksl;
	}

	public void setCksl(String cksl) {
		this.cksl = cksl;
	}

	public String getSfsz() {
		return sfsz;
	}

	public void setSfsz(String sfsz) {
		this.sfsz = sfsz;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getWlkcl() {
		return wlkcl;
	}

	public void setWlkcl(String wlkcl) {
		this.wlkcl = wlkcl;
	}

	public String getLjbj() {
		return ljbj;
	}

	public void setLjbj(String ljbj) {
		this.ljbj = ljbj;
	}

	public String getGylx() {
		return gylx;
	}

	public void setGylx(String gylx) {
		this.gylx = gylx;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getSfgz() {
		return sfgz;
	}

	public void setSfgz(String sfgz) {
		this.sfgz = sfgz;
	}

	public String getSyzt() {
		return syzt;
	}

	public void setSyzt(String syzt) {
		this.syzt = syzt;
	}

	public String getYds() {
		return yds;
	}

	public void setYds(String yds) {
		this.yds = yds;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getSfwxpmc() {
		return sfwxpmc;
	}

	public void setSfwxpmc(String sfwxpmc) {
		this.sfwxpmc = sfwxpmc;
	}

	public String getAbclbmc() {
		return abclbmc;
	}

	public void setAbclbmc(String abclbmc) {
		this.abclbmc = abclbmc;
	}

	public String getAbclbdm() {
		return abclbdm;
	}

	public void setAbclbdm(String abclbdm) {
		this.abclbdm = abclbdm;
	}

	public String getQgid() {
		return qgid;
	}

	public void setQgid(String qgid) {
		this.qgid = qgid;
	}

	public String getXmbm() {
		return xmbm;
	}

	public void setXmbm(String xmbm) {
		this.xmbm = xmbm;
	}

	public String getXmdl() {
		return xmdl;
	}

	public void setXmdl(String xmdl) {
		this.xmdl = xmdl;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getXgryzsxm() {
		return xgryzsxm;
	}

	public void setXgryzsxm(String xgryzsxm) {
		this.xgryzsxm = xgryzsxm;
	}

	public String getWlzlbtcdm() {
		return wlzlbtcdm;
	}

	public void setWlzlbtcdm(String wlzlbtcdm) {
		this.wlzlbtcdm = wlzlbtcdm;
	}

	public String getSscplbmc() {
		return sscplbmc;
	}

	public void setSscplbmc(String sscplbmc) {
		this.sscplbmc = sscplbmc;
	}

	public String getSscplbdm() {
		return sscplbdm;
	}

	public void setSscplbdm(String sscplbdm) {
		this.sscplbdm = sscplbdm;
	}

	public String[] getLbs() {
		return lbs;
	}

	public void setLbs(String[] lbs) {
		this.lbs = lbs;
	}
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String[] getWlzlbtcs() {
		return wlzlbtcs;
	}

	public void setWlzlbtcs(String[] wlzlbtcs) {
		this.wlzlbtcs = wlzlbtcs;
	}

	public String getQg_ids() {
		return qg_ids;
	}

	public void setQg_ids(String qg_ids) {
		this.qg_ids = qg_ids;
	}

	public String getYcgzlsl() {
		return ycgzlsl;
	}

	public void setYcgzlsl(String ycgzlsl) {
		this.ycgzlsl = ycgzlsl;
	}

	public String getWllbmc() {
		return wllbmc;
	}

	public void setWllbmc(String wllbmc) {
		this.wllbmc = wllbmc;
	}

	public String getLbdm() {
		return lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}

	public String[] getWllbs() {
		return wllbs;
	}

	public void setWllbs(String[] wllbs) {
		this.wllbs = wllbs;
		for(int i=0;i<this.wllbs.length;i++)
		{
			this.wllbs[i] = this.wllbs[i].replace("'", "");
		}
	}

	public String[] getSfwxps() {
		return sfwxps;
	}

	public void setSfwxps(String[] sfwxps) {
		this.sfwxps = sfwxps;
		for(int i=0;i<this.sfwxps.length;i++)
		{
			this.sfwxps[i] = this.sfwxps[i].replace("'", "");
		}
	}

	public String[] getScbjs() {
		return scbjs;
	}

	public void setScbjs(String[] scbjs) {
		this.scbjs = scbjs;
		for(int i=0;i<this.scbjs.length;i++)
		{
			this.scbjs[i] = this.scbjs[i].replace("'", "");
		}
	}
	
	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++)
		{
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public String getWlzlbmc() {
		return wlzlbmc;
	}

	public void setWlzlbmc(String wlzlbmc) {
		this.wlzlbmc = wlzlbmc;
	}

	public String[] getWlzlbs() {
		return wlzlbs;
	}

	public void setWlzlbs(String[] wlzlbs) {
		this.wlzlbs = wlzlbs;
		for(int i=0;i<this.wlzlbs.length;i++)
		{
			this.wlzlbs[i] = this.wlzlbs[i].replace("'", "");
		}
	}

	public String getWllbdm() {
		return wllbdm;
	}

	public void setWllbdm(String wllbdm) {
		this.wllbdm = wllbdm;
	}

	public String getWlzlbdm() {
		return wlzlbdm;
	}

	public void setWlzlbdm(String wlzlbdm) {
		this.wlzlbdm = wlzlbdm;
	}

	public String getShsjstart() {
		return shsjstart;
	}

	public void setShsjstart(String shsjstart) {
		this.shsjstart = shsjstart;
	}

	public String getShsjend() {
		return shsjend;
	}

	public void setShsjend(String shsjend) {
		this.shsjend = shsjend;
	}

	public String getLrryxm() {
		return lrryxm;
	}

	public void setLrryxm(String lrryxm) {
		this.lrryxm = lrryxm;
	}

	public String getWllb_flg() {
		return wllb_flg;
	}

	public void setWllb_flg(String wllb_flg) {
		this.wllb_flg = wllb_flg;
	}

	public String getWlzlb_flg() {
		return wlzlb_flg;
	}

	public void setWlzlb_flg(String wlzlb_flg) {
		this.wlzlb_flg = wlzlb_flg;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getZjwlid() {
		return zjwlid;
	}

	public void setZjwlid(String zjwlid) {
		this.zjwlid = zjwlid;
	}

	public String getAuditModType() {
		return auditModType;
	}

	public void setAuditModType(String auditModType) {
		this.auditModType = auditModType;
	}

	public String getLsid() {
		return lsid;
	}

	public void setLsid(String lsid) {
		this.lsid = lsid;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getWlzt() {
		return wlzt;
	}

	public void setWlzt(String wlzt) {
		this.wlzt = wlzt;
	}

	public String getQxqg() {
		return qxqg;
	}

	public void setQxqg(String qxqg) {
		this.qxqg = qxqg;
	}

	public String getWlzllb_flg() {
		return wlzllb_flg;
	}

	public void setWlzllb_flg(String wlzllb_flg) {
		this.wlzllb_flg = wlzllb_flg;
	}
	public String[] getSscplbs() {
		return sscplbs;
	}

	public void setSscplbs(String[] sscplbs) {
		this.sscplbs = sscplbs;
		for(int i=0;i<this.sscplbs.length;i++)
		{
			this.sscplbs[i] = this.sscplbs[i].replace("'", "");
		}
	}
}
