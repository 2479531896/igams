package com.matridx.igams.storehouse.dao.entities;


import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="RkglDto")
public class RkglDto extends RkglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//入库类型代码
	private String rklxdm;
	//入库类型名称
	private String rklxmc;
	//请购单据号
	private String djh;
	//请购申请日期
	private String qgsqrq;
	//供应商代码
	private String gysdm;
	//供应商名称
	private String gysmc;
	// 入库部门名称
	private String sqbmmc;
	// 标识
	private String bs;
	//入库部门代码
	private String jgdm;
	//合同管理负责人
	private String htfzr;
	//请购申请人
	private String qgsqr;
	//仓库权限类型
	private String ckqxlx;
	//合同明细税率
	private String htmxsl;
	//合同币种
	private String htbz;
	//仓库名称
	private String ckmc;
	//申请人员名称：取了hwxx表中的入库人员字段
	private String sqry;
	//申请日期：取了hwxx表中的入库日期字段
	private String sqrq;
	//物料名称
	private String wlmc;
	//物料代码
	private String wlbm;
	//采购类型名称
	private String cglxmc;
	//货物信息json
	private String hwxx_json;
	//仓库代码
	private String ckdm;
	// 入库日期结束日期
	private String shsjend;
	// 入库日期开始日期
	private String shsjstart;
	// 检索用删除标记
	private String[] scbjs;
	//请购记录编号
	private String jlbh;
	//合同关联U8id
	private String u8poid;
	//导出关联标记位//所选择的字段
	private String SqlParam; 	
	//查询参数[多个条件]
	private String searchParam;
	//合同内部编号
	private String htnbbh;
	//录入人员名称
	private String lrrymc;
	//真实姓名
	private String zsxm;
	//入库类别扩展参数
	private String cskz1;
	//钉钉标记 1从钉钉发来请求，不做修改 
	private String ddbj;
	//用户钉钉ID
	private String ddid;
	//访问标记
	private String fwbj;
	//借出归还ID
	private String jcghid;
	//U8入库单号
	private String u8rkdh;
	private String entire;
	//审核状态名称
	private String ztmc;
	//入库类型多
	private String[] rklxs;
	//状态多
	private String[] zts;
		//合同类型
	private String htlx;
	//汇率
	private String hl;
	//合同关联id
	//入库ids
	private List<String> rkids;
	private String sqryhm;//申请人用户名

	public String getSqryhm() {
		return sqryhm;
	}

	public void setSqryhm(String sqryhm) {
		this.sqryhm = sqryhm;
	}
	public List<String> getRkids() {
		return rkids;
	}

	public void setRkids(List<String> rkids) {
		this.rkids = rkids;
	}

	public String[] getRklxs() {
		return rklxs;
	}
	private String u8omid;
	public void setRklxs(String[] rklxs) {
		this.rklxs = rklxs;
	}
	

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String getZtmc() {
		return ztmc;
	}

	public void setZtmc(String ztmc) {
		this.ztmc = ztmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getU8omid() {
		return u8omid;
	}

	public void setU8omid(String u8omid) {
		this.u8omid = u8omid;
	}

	public String getHl() {
		return hl;
	}

	public void setHl(String hl) {
		this.hl = hl;
	}

	public String getHtlx() {
		return htlx;
	}

	public void setHtlx(String htlx) {
		this.htlx = htlx;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public String getBs() {
		return bs;
	}

	public void setBs(String bs) {
		this.bs = bs;
	}
	public String getU8rkdh() {
		return u8rkdh;
	}

	public void setU8rkdh(String u8rkdh) {
		this.u8rkdh = u8rkdh;
	}

	public String getJcghid() {
		return jcghid;
	}

	public void setJcghid(String jcghid) {
		this.jcghid = jcghid;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	//废弃标记（判断删除的数据是否为废弃）
	private String fqbj;
	//rabbit标记
	private String prefixFlg;

	public String getFqbj() {
		return fqbj;
	}

	public void setFqbj(String fqbj) {
		this.fqbj = fqbj;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getDdbj() {
		return ddbj;
	}
	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getLrrymc() {
		return lrrymc;
	}
	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
	public String getHtnbbh() {
		return htnbbh;
	}
	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
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
	public String getU8poid() {
		return u8poid;
	}
	public void setU8poid(String u8poid) {
		this.u8poid = u8poid;
	}
	public String getJlbh() {
		return jlbh;
	}
	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}
	public String getShsjend() {
		return shsjend;
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
	public String[] getScbjs() {
		return scbjs;
	}
	public void setScbjs(String[] scbjs) {
		this.scbjs = scbjs;
		for (int i = 0; i < scbjs.length; i++) {
			this.scbjs[i] = this.scbjs[i].replace("'", "");
		}
	}
	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}
	public String getHwxx_json() {
		return hwxx_json;
	}
	public void setHwxx_json(String hwxx_json) {
		this.hwxx_json = hwxx_json;
	}
	public String getCglxmc() {
		return cglxmc;
	}
	public void setCglxmc(String cglxmc) {
		this.cglxmc = cglxmc;
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
	public String getSqry() {
		return sqry;
	}
	public void setSqry(String sqry) {
		this.sqry = sqry;
	}
	public String getSqrq() {
		return sqrq;
	}
	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}
	public String getCkmc() {
		return ckmc;
	}
	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getHtbz() {
		return htbz;
	}
	public void setHtbz(String htbz) {
		this.htbz = htbz;
	}
	public String getHtmxsl() {
		return htmxsl;
	}
	public void setHtmxsl(String htmxsl) {
		this.htmxsl = htmxsl;
	}
	public String getQgsqr() {
		return qgsqr;
	}
	public void setQgsqr(String qgsqr) {
		this.qgsqr = qgsqr;
	}

	public String getHtfzr() {
		return htfzr;
	}
	public void setHtfzr(String htfzr) {
		this.htfzr = htfzr;
	}
	public String getSqbmmc() {
		return sqbmmc;
	}
	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}
	
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	public String getGysdm() {
		return gysdm;
	}
	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}
	public String getDjh() {
		return djh;
	}
	public void setDjh(String djh) {
		this.djh = djh;
	}
	public String getQgsqrq() {
		return qgsqrq;
	}
	public void setQgsqrq(String qgsqrq) {
		this.qgsqrq = qgsqrq;
	}
	public String getRklxdm() {
		return rklxdm;
	}
	public void setRklxdm(String rklxdm) {
		this.rklxdm = rklxdm;
	}
	public String getRklxmc() {
		return rklxmc;
	}
	public void setRklxmc(String rklxmc) {
		this.rklxmc = rklxmc;
	}
	
}
