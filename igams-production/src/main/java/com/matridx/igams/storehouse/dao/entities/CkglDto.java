package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CkglDto")
public class CkglDto extends CkglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//仓库代码
	private String ckdm;
	//出库类别参数代码
	private String cklbcsdm;
	//部门代码
	private String bmdm;
	//单据类型
	private String djlxmc;
	//单据类型代码
	private String djlxdm;
	//发料人名称
	private String flrmc;
	// 类别 数组
	private String[] lbs;
	//领料人名称
	private String llrmc;
	//仓库名称
	private String ckmc;
	//c库类别名称
	private String cklbmc;
	//entire
	private String entire;
	//出库 ids
	private List<String> ckids;
	private String sum_qlsl;
	private String sum_slsl;
	// 货物领料信息
	private List<HwllxxDto> hwllxxDtos;
	//领料日期
	private String sqrq;
	//领料部门
	private String sqbmmc;
	//部门名称
	private String bmmc;

	//领料单号
	private String lldh;
	//机构代码
	private String jgdm;
	//真实姓名
	private String zsxm;
	//机构名称
	private String jgmc;
	//货物信息json
	private String hwxx_json;
	//钉钉标记
	private String ddbj;
	//所属机构
	private String ssjg;
	//委外出库Json
	private String ckmx_json;
	//仓库类别参数扩展1
	private String cklbcskz1;
	// 仓库明细
	private List<CkmxDto> ckmxDtos;
	
		//领料申请人
	private String sqrmc;
	private String sqbmdm;
	private String sqbm;
	//销售部门名称
	private String xsbmmc;
	//客户
	private String kh;
	//审核日期
	private String shrq;
	//合计数量
	private String sum_sl;
	//制单人名称
	private String zdrmc;
	//审核人员名称
	private String shrymc;
	//收货地址
	private String shdz;
	//U8发货单号
	private String U8fhdh;

	public String getU8fhdh() {
		return U8fhdh;
	}

	public void setU8fhdh(String u8fhdh) {
		U8fhdh = u8fhdh;
	}

	public String getShdz() {
		return shdz;
	}

	public void setShdz(String shdz) {
		this.shdz = shdz;
	}

	public String getZdrmc() {
		return zdrmc;
	}

	public void setZdrmc(String zdrmc) {
		this.zdrmc = zdrmc;
	}

	public String getShrymc() {
		return shrymc;
	}

	public void setShrymc(String shrymc) {
		this.shrymc = shrymc;
	}

	public String getSum_sl() {
		return sum_sl;
	}

	public void setSum_sl(String sum_sl) {
		this.sum_sl = sum_sl;
	}

	public String getKh() {
		return kh;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public String getShrq() {
		return shrq;
	}

	public void setShrq(String shrq) {
		this.shrq = shrq;
	}

	public String getXsbmmc() {
		return xsbmmc;
	}

	public void setXsbmmc(String xsbmmc) {
		this.xsbmmc = xsbmmc;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSqbmdm() {
		return sqbmdm;
	}

	public void setSqbmdm(String sqbmdm) {
		this.sqbmdm = sqbmdm;
	}

	public void setCkmx_json(String ckmx_json) {
		this.ckmx_json = ckmx_json;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public List<CkmxDto> getCkmxDtos() {
		return ckmxDtos;
	}

	public void setCkmxDtos(List<CkmxDto> ckmxDtos) {
		this.ckmxDtos = ckmxDtos;
	}

	public String getCklbcskz1() {
		return cklbcskz1;
	}

	public void setCklbcskz1(String cklbcskz1) {
		this.cklbcskz1 = cklbcskz1;
	}

	public String getCkmx_json() {
		return ckmx_json;
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

	public String getDjlxdm() {
		return djlxdm;
	}

	public void setDjlxdm(String djlxdm) {
		this.djlxdm = djlxdm;
	}


	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getDjlxmc() {
		return djlxmc;
	}

	public void setDjlxmc(String djlxmc) {
		this.djlxmc = djlxmc;
	}

	public String getSsjg() {
		return ssjg;
	}

	public void setSsjg(String ssjg) {
		this.ssjg = ssjg;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getHwxx_json() {
		return hwxx_json;
	}

	public void setHwxx_json(String hwxx_json) {
		this.hwxx_json = hwxx_json;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getLldh() {
		return lldh;
	}

	public void setLldh(String lldh) {
		this.lldh = lldh;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getSum_qlsl() {
		return sum_qlsl;
	}

	public void setSum_qlsl(String sum_qlsl) {
		this.sum_qlsl = sum_qlsl;
	}

	public String getSum_slsl() {
		return sum_slsl;
	}

	public void setSum_slsl(String sum_slsl) {
		this.sum_slsl = sum_slsl;
	}

	public List<HwllxxDto> getHwllxxDtos() {
		return hwllxxDtos;
	}

	public void setHwllxxDtos(List<HwllxxDto> hwllxxDtos) {
		this.hwllxxDtos = hwllxxDtos;
	}

	public List<String> getCkids() {
		return ckids;
	}

	public void setCkids(List<String> ckids) {
		this.ckids = ckids;
	}

	public String getLlrmc() {
		return llrmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public void setLlrmc(String llrmc) {
		this.llrmc = llrmc;
	}

	public String getCkmc() {
		return ckmc;
	}

	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}

	public String getCklbmc() {
		return cklbmc;
	}

	public void setCklbmc(String cklbmc) {
		this.cklbmc = cklbmc;
	}

	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}
	public String getCklbcsdm() {
		return cklbcsdm;
	}
	public void setCklbcsdm(String cklbcsdm) {
		this.cklbcsdm = cklbcsdm;
	}
	public String getBmdm() {
		return bmdm;
	}
	public void setBmdm(String bmdm) {
		this.bmdm = bmdm;
	}
	public String getFlrmc() {
		return flrmc;
	}
	public void setFlrmc(String flrmc) {
		this.flrmc = flrmc;
	}
	
}
