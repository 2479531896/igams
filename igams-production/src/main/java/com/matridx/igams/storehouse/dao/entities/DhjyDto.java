package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;
/**
 * 到货检验
 * @author lifan
 *
 */
@Alias(value="DhjyDto")
public class DhjyDto extends DhjyModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//检验负责人名称
	private String jyfzrmc;
	// 全部(查询条件)
	private String entire;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	//查询参数[多个条件]
	private String searchParam;
	// 检验日期结束日期
	private String shsjend;
	// 检验日期开始日期
	private String shsjstart;
	// 到货检验关联附件id
	private List<String> fjids;
	//类别参数扩展1
	private String lbcskz1;
	//保存标记
	private String bcbj;
	//审核类别
	private String shlb;
	//货物信息list
	private String hwxxlist;
	//删除的货物信息
	private String delids;
	//仓库代码
	private String ckdm;
	//录入人员名称
	private String llrymc;
	//U8调拨不合格关联id
	private String bhgglid;
	//真实姓名
	private String zsxm;
	//参数id
	private String csid;
	//参数名称
	private String csmc;
	// 检索用检验结果（多）
	private String[] jyjgs;
	//物料编码
	private String wlbm;
	//物料名称
	private String wlmc;
	//钉钉标记
	private String ddbj;
	private String[] zts;
	//
	private String chhwid;
	//调出货物id
	private String dchwid;
	//调入货物id
	private String drhwid;
	//到货单号
	private String dhdh;
		//已审核数
	private String yshs;
	//审核中数
	private String shzs;
	//未审核数
	private String wshs;
	//未通过数
	private String wtgs;
	//列表标记  0：检验列表  1：检验物料列表
	private String lbbj;
	//报告单号
	private String bgdh;
	//数量
	private String sl;
	//规格
	private String gg;
	//生产批号
	private String scph;
	//货物id
	private String hwid;
	//查看标记 viewmore
	private String flag;
	private String lbcskz2;

	public String getLbcskz2() {
		return lbcskz2;
	}

	public void setLbcskz2(String lbcskz2) {
		this.lbcskz2 = lbcskz2;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getHwid() {
		return hwid;
	}

	public void setHwid(String hwid) {
		this.hwid = hwid;
	}

	public String getBgdh() {
		return bgdh;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getLbbj() {
		return lbbj;
	}

	public void setLbbj(String lbbj) {
		this.lbbj = lbbj;
	}

	public String getWtgs() {
		return wtgs;
	}

	public void setWtgs(String wtgs) {
		this.wtgs = wtgs;
	}

	public String getYshs() {
		return yshs;
	}

	public void setYshs(String yshs) {
		this.yshs = yshs;
	}

	public String getShzs() {
		return shzs;
	}

	public void setShzs(String shzs) {
		this.shzs = shzs;
	}

	public String getWshs() {
		return wshs;
	}

	public void setWshs(String wshs) {
		this.wshs = wshs;
	}

	public String getDhdh() {
		return dhdh;
	}

	public void setDhdh(String dhdh) {
		this.dhdh = dhdh;
	}

	public String getChhwid() {
		return chhwid;
	}

	public void setChhwid(String chhwid) {
		this.chhwid = chhwid;
	}

	public String getDchwid() {
		return dchwid;
	}

	public void setDchwid(String dchwid) {
		this.dchwid = dchwid;
	}

	public String getDrhwid() {
		return drhwid;
	}

	public void setDrhwid(String drhwid) {
		this.drhwid = drhwid;
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

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
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

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}

	public String[] getJyjgs() {
		return jyjgs;
	}

	public void setJyjgs(String[] jyjgs) {
		this.jyjgs = jyjgs;
		for (int i = 0; i < jyjgs.length; i++) {
			this.jyjgs[i] = this.jyjgs[i].replace("'", "");
		}
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getBhgglid() {
		return bhgglid;
	}

	public void setBhgglid(String bhgglid) {
		this.bhgglid = bhgglid;
	}

	public String getLlrymc() {
		return llrymc;
	}

	public void setLlrymc(String llrymc) {
		this.llrymc = llrymc;
	}

	public String getCkdm() {
		return ckdm;
	}

	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}

	public String getShsjend() {
		return shsjend;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public void setShsjend(String shsjend) {
		this.shsjend = shsjend;
	}

	public String getShsjstart() {
		return shsjstart;
	}

	public void setShsjstart(String shsjstart) {
		this.shsjstart = shsjstart;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getJyfzrmc() {
		return jyfzrmc;
	}

	public void setJyfzrmc(String jyfzrmc) {
		this.jyfzrmc = jyfzrmc;
	}

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getBcbj() {
		return bcbj;
	}

	public void setBcbj(String bcbj) {
		this.bcbj = bcbj;
	}

	public String getShlb() {
		return shlb;
	}

	public void setShlb(String shlb) {
		this.shlb = shlb;
	}

	public String getHwxxlist() {
		return hwxxlist;
	}

	public void setHwxxlist(String hwxxlist) {
		this.hwxxlist = hwxxlist;
	}

	public String getDelids() {
		return delids;
	}

	public void setDelids(String delids) {
		this.delids = delids;
	}
	
}
