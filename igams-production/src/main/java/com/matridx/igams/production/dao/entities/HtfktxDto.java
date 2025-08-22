package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value="HtfktxDto")
public class HtfktxDto extends HtfktxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//存放付款提醒数据
	private String fktxJson;
	//合同内部编号
	private String htnbbh;
	//付款方式名称
	private String fkfsmc;
	//供应商
	private String gysmc;
	//总金额
	private String zje;
	//已付金额
	private String yfje;
	//entire
	private String entire;
	//录入人员名称
	private String lrrymc;
	//未付款金额
	private String wfkje;
	//供应商开户行
	private String gyskhh;
	//供应商银行账户
	private String gysyyzh;
	//供应商ID
	private String gysid;
	//合同已付金额
	private String htyfje;
	//合同审核状态
	private String zt;
	//用款部门
	private String ykbm;
	//用款部门名称
	private String ykbmmc;
	//合同IDs
	public String htids;
	//合同总金额
	private String htzje;
	private String xbfb;
	private String xzje;
	private String fkzje;

	public String getFkzje() {
		return fkzje;
	}

	public void setFkzje(String fkzje) {
		this.fkzje = fkzje;
	}

	public String getXzje() {
		return xzje;
	}

	public void setXzje(String xzje) {
		this.xzje = xzje;
	}

	public String getXbfb() {
		return xbfb;
	}

	public void setXbfb(String xbfb) {
		this.xbfb = xbfb;
	}

	public String getHtzje() {
		return htzje;
	}

	public void setHtzje(String htzje) {
		this.htzje = htzje;
	}

	public String getHtids() {
		return htids;
	}

	public void setHtids(String htids) {
		this.htids = htids;
	}

	public String getYkbm() {
		return ykbm;
	}

	public void setYkbm(String ykbm) {
		this.ykbm = ykbm;
	}

	public String getYkbmmc() {
		return ykbmmc;
	}

	public void setYkbmmc(String ykbmmc) {
		this.ykbmmc = ykbmmc;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getHtyfje() {
		return htyfje;
	}

	public void setHtyfje(String htyfje) {
		this.htyfje = htyfje;
	}

	public String getGysid() {
		return gysid;
	}

	public void setGysid(String gysid) {
		this.gysid = gysid;
	}

	public String getGysyyzh() {
		return gysyyzh;
	}

	public void setGysyyzh(String gysyyzh) {
		this.gysyyzh = gysyyzh;
	}

	public String getGyskhh() {
		return gyskhh;
	}

	public void setGyskhh(String gyskhh) {
		this.gyskhh = gyskhh;
	}

	public String getWfkje() {
		return wfkje;
	}

	public void setWfkje(String wfkje) {
		this.wfkje = wfkje;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getHtnbbh() {
		return htnbbh;
	}

	public void setHtnbbh(String htnbbh) {
		this.htnbbh = htnbbh;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	@Override
	public String getYfje() {
		return yfje;
	}

	@Override
	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getFktxJson() {
		return fktxJson;
	}

	public void setFktxJson(String fktxJson) {
		this.fktxJson = fktxJson;
	}
}
