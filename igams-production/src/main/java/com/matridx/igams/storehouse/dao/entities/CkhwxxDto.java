package com.matridx.igams.storehouse.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value = "CkhwxxDto")
public class CkhwxxDto extends CkhwxxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 物料名称(wlgl.wlmc)
	private String wlmc;
	// 物料编码(wlgl.wlbm)
	private String wlbm;
	//仓库代码
	private String ckdm;
	// 物料规格(wlgl.gg)
	private String gg;
	// 原厂货号(wlgl.ychh)
	private String ychh;
	// 计量单位(wlgl.jldw)
	private String jldw;
	// 已放入领料车的物料
	private String ll_ids;
	// 已领料的物料种类数量
	private String yllzlsl;
	// 生产商名称(wlgl.scs)
	private String scs;
	// 物料类别(wlgl.wllb)
	private String wllb;
	// 物料子类别(wlgl.wlzlb)
	private String wlzlb;
	// 安全库存(wlgl.aqkc)
	private String aqkc;
	// 警戒提示
	private String jjts;
	//查询条件
	private String entire;
	//kcbj=1 有库存，kcbj=0 库存未0
	private String kcbj;
	//可请领数
	private String kqls;
	//预定数标记 1:加  0：减
	private String ydsbj;
	// 分组(wlgl.lb)
	private String lb;
	//生产批号
	private String scph;
	//追溯号
	private String zsh;
	// 到货日期(dhxx.dhrq)
	private String dhrq;
	// 到货数量(hwxx.dhsl)
	private String dhsl;
	//到货单号
	private String dhdh;
	//到货id
	private String dhid;
	// 有效期(hwxx.yxq)
	private String yxq;
	//转出仓库
	private String zcck;
	// 供货商
	private String gys;
	// 当前库存量
	private String dqkcl;
	// 入库人员
	private String rkry;
	//到货检验id
	private String dhjyid;
	//产品类型
	private String cplx;
	//仓库ids
	private List<String> ckids;
	//限制标记 库存领料限制用
	private String xzbj;
	//角色id
	private String jsid;
	//仓库 多
	private String[] cks;

	private String yxqstart;
	private String yxqend;

	public String getYxqstart() {
		return yxqstart;
	}

	public void setYxqstart(String yxqstart) {
		this.yxqstart = yxqstart;
	}

	public String getYxqend() {
		return yxqend;
	}

	public void setYxqend(String yxqend) {
		this.yxqend = yxqend;
	}

	public String[] getCks() {
		return cks;
	}

	public void setCks(String[] cks) {
		this.cks = cks;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getXzbj() {
		return xzbj;
	}

	public void setXzbj(String xzbj) {
		this.xzbj = xzbj;
	}

	public List<String> getCkids() {
		return ckids;
	}
	public void setCkids(String ckids) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(ckids)) {
			String[] str = ckids.split(",");
			list = Arrays.asList(str);
		}
		this.ckids = list;
	}
	public void setCkids(List<String> ckids) {
		if(ckids!=null && !ckids.isEmpty()){
			ckids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.ckids = ckids;
	}

	public String getCplx() {
		return cplx;
	}

	public void setCplx(String cplx) {
		this.cplx = cplx;
	}

	public String getDhjyid() {
		return dhjyid;
	}

	public void setDhjyid(String dhjyid) {
		this.dhjyid = dhjyid;
	}

	//高级筛选用数组
	private String[] jjtss;
	private String[] lbs;
	private String[] fzs;
	//导出关联标记为所选择的字段
	private String sqlParam;
	//关联表标记
	private String ckhwxx_flg;
	//代理供应商(wlgl.dlgys)
	private String dlgys;
	//保存条件
	private String bctj;
	//是否危险品
	private String sfwxp;
	//保质期
	private String bzq;
	//备注
	private String bz;
	//旧物料编码
	private String jwlbm;
	//物料类别名称
	private String wllbmc;
	//物料子类别名称
	private String wlzlbmc;
	//物料子类别统称名称
	private String wlzlbtcmc;
	//类别名称
	private String lbmc;
	//仓库名称
	private String ckmc;
	//库位
	private String kw;
	//货物id
	private String hwid;
	//生产日期
	private String scrq;
	//检验单号
	private String jydh;
	//物料类别多
	private String[] wllbs;
	private String[] wlzlbs;
	private String[] sfwxps;
	//出库日期
	private String ckrq;

	public String getCkrq() {
		return ckrq;
	}

	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getLbmc() {
		return lbmc;
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc;
	}

	//项目编码
	private String xmbm;
	//项目大类
	private String xmdl;
	//可请领数量
	private String klsl;
	//扩展参数2
	private String kzcs2;
	//扩展参数1
	private String cskz1;
	//仓库分类ids
	private String ckqx;
	//仓库分类
	private List<String> ckqxlxs;
	//仓库分类名称
	private String ckqxmc;
	//库存量标记 1+，0-
	private String kclbj;
	//是否危险品名称
	private String sfwxpmc;
	//到货仓库名称
	private String dhckmc;
	//库存量
	private String kcl;
	//初验退回数量
	private String cythsl;
	//质检退回数量
	private String zjthsl;
	//物料质量类别参数扩展1
	private String lbcskz1;
	private String[] bms;
	private String kczt;//库存状态

	public String getKczt() {
		return kczt;
	}

	public void setKczt(String kczt) {
		this.kczt = kczt;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String[] getBms() {
		return bms;
	}

	public void setBms(String[] bms) {
		this.bms = bms;
		for (int i = 0; i < bms.length; i++){
			this.bms[i]=this.bms[i].replace("'","");
		}
	}
	public String getJydh() {
		return jydh;
	}

	public void setJydh(String jydh) {
		this.jydh = jydh;
	}

	@Override
	public String getKcl() {
		return kcl;
	}

	@Override
	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getCythsl() {
		return cythsl;
	}

	public void setCythsl(String cythsl) {
		this.cythsl = cythsl;
	}

	public String getZjthsl() {
		return zjthsl;
	}

	public void setZjthsl(String zjthsl) {
		this.zjthsl = zjthsl;
	}

	public String getDhckmc() {
		return dhckmc;
	}

	public void setDhckmc(String dhckmc) {
		this.dhckmc = dhckmc;
	}

	public String getSfwxpmc() {
		return sfwxpmc;
	}

	public void setSfwxpmc(String sfwxpmc) {
		this.sfwxpmc = sfwxpmc;
	}

	public String getKclbj() {
		return kclbj;
	}

	public void setKclbj(String kclbj) {
		this.kclbj = kclbj;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getZcck() {
		return zcck;
	}

	public void setZcck(String zcck) {
		this.zcck = zcck;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	private List<String> qxlxids;
	public List<String> getQxlxids() {
		return qxlxids;
	}
	public void setQxlxids(String qxlxids) {
		List<String> list;
		String[] str = qxlxids.split(",");
		list = Arrays.asList(str);
		this.qxlxids = list;
	}
	public void setQxlxids(List<String> qxlxids) {
		if(!CollectionUtils.isEmpty(qxlxids)){
            qxlxids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.qxlxids = qxlxids;
	}
	public String getCkqxmc() {
		return ckqxmc;
	}

	public void setCkqxmc(String ckqxmc) {
		this.ckqxmc = ckqxmc;
	}

	public List<String> getCkqxlxs() {
		return ckqxlxs;
	}

	public void setCkqxlxs(List<String> ckqxlxs) {
		this.ckqxlxs = ckqxlxs;
	}

	public String getCkqx() {
		return ckqx;
	}

	public void setCkqx(String ckqx) {
		this.ckqx = ckqx;
	}

	public String getKzcs2() {
		return kzcs2;
	}

	public void setKzcs2(String kzcs2) {
		this.kzcs2 = kzcs2;
	}

	public String getKlsl() {
		return klsl;
	}

	public void setKlsl(String klsl) {
		this.klsl = klsl;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getDhid() {
		return dhid;
	}

	public void setDhid(String dhid) {
		this.dhid = dhid;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
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

	public String getJwlbm() {
		return jwlbm;
	}

	public void setJwlbm(String jwlbm) {
		this.jwlbm = jwlbm;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getBctj() {
		return bctj;
	}

	public void setBctj(String bctj) {
		this.bctj = bctj;
	}

	public String getSfwxp() {
		return sfwxp;
	}

	public void setSfwxp(String sfwxp) {
		this.sfwxp = sfwxp;
	}

	public String getBzq() {
		return bzq;
	}

	public void setBzq(String bzq) {
		this.bzq = bzq;
	}

	public String getDlgys() {
		return dlgys;
	}

	public void setDlgys(String dlgys) {
		this.dlgys = dlgys;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getCkhwxx_flg() {
		return ckhwxx_flg;
	}

	public void setCkhwxx_flg(String ckhwxx_flg) {
		this.ckhwxx_flg = ckhwxx_flg;
	}

	public String getRkry() {
		return rkry;
	}

	public void setRkry(String rkry) {
		this.rkry = rkry;
	}

	public String getDqkcl() {
		return dqkcl;
	}

	public void setDqkcl(String dqkcl) {
		this.dqkcl = dqkcl;
	}

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	public String getZsh() {
		return zsh;
	}

	public void setZsh(String zsh) {
		this.zsh = zsh;
	}

	public String getDhrq() {
		return dhrq;
	}

	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}

	public String getDhsl() {
		return dhsl;
	}

	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getGys() {
		return gys;
	}

	public void setGys(String gys) {
		this.gys = gys;
	}

	public String[] getJjtss() {
		return jjtss;
	}

	public void setJjtss(String[] jjtss) {
		this.jjtss = jjtss;
		for (int i = 0; i < jjtss.length; i++){
			this.jjtss[i]=this.jjtss[i].replace("'","");
		}
	}

	public String[] getLbs() {
		return lbs;
	}

	public void setLbs(String[] lbs) {
		this.lbs = lbs;
		for (int i = 0; i < lbs.length; i++){
			this.lbs[i]=this.lbs[i].replace("'","");
		}
	}

	public String[] getFzs() {
		return fzs;
	}

	public void setFzs(String[] fzs) {
		this.fzs = fzs;
		for (int i = 0; i < fzs.length; i++){
			this.fzs[i]=this.fzs[i].replace("'","");
		}
	}

	public String getYdsbj() {
		return ydsbj;
	}

	public void setYdsbj(String ydsbj) {
		this.ydsbj = ydsbj;
	}

	public String getKcbj() {
		return kcbj;
	}

	public void setKcbj(String kcbj) {
		this.kcbj = kcbj;
	}

	public String getKqls() {
		return kqls;
	}

	public void setKqls(String kqls) {
		this.kqls = kqls;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getAqkc() {
		return aqkc;
	}

	public void setAqkc(String aqkc) {
		this.aqkc = aqkc;
	}

	public String getJjts() {
		return jjts;
	}

	public void setJjts(String jjts) {
		this.jjts = jjts;
	}

	public String getYllzlsl() {
		return yllzlsl;
	}

	public void setYllzlsl(String yllzlsl) {
		this.yllzlsl = yllzlsl;
	}

	public String getLl_ids() {
		return ll_ids;
	}

	public void setLl_ids(String ll_ids) {
		this.ll_ids = ll_ids;
	}

	public String getWlmc() {
		return wlmc;
	}

	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}

	public String getWlbm() {
		return wlbm;
	}

	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getYchh() {
		return ychh;
	}

	public void setYchh(String ychh) {
		this.ychh = ychh;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getScs() {
		return scs;
	}

	public void setScs(String scs) {
		this.scs = scs;
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

	public String[] getWllbs() {
		return wllbs;
	}

	public void setWllbs(String[] wllbs) {
		this.wllbs = wllbs;
	}

	public String[] getWlzlbs() {
		return wlzlbs;
	}

	public void setWlzlbs(String[] wlzlbs) {
		this.wlzlbs = wlzlbs;
	}

	public String[] getSfwxps() {
		return sfwxps;
	}

	public void setSfwxps(String[] sfwxps) {
		this.sfwxps = sfwxps;
	}
}
