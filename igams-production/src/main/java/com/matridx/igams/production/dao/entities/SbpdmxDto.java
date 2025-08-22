package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SbpdmxDto")
public class SbpdmxDto extends SbpdmxModel{
	//发起时间
	private String fqsj;
	//盘点人员
	private String pdry;
	//发起人员
	private String fqry;
	//部门
	private String bm;
	//盘点人员名称
	private String pdrymc;
	//发起人员名称
	private String fqrymc;
	//部门名称
	private String bmmc;
	//设备编号
	private String sbbh;
	//设备名称
	private String sbmc;
	//固定资产编号
	private String gdzcbh;
	//规格型号
	private String ggxh;
	//序列号
	private String xlh;
	//现使用部门名称
	private String xsybmmc;
	//使用地点
	private String sydd;
	//设备类型名称
	private String sblxmc;
	//设备部门负责人名称
	private String sbbmfzrmc;
	//盘点状态名称
	private String pdztmc;
	//导出参数
	private String sqlParam;
	//查询参数
	private String entire;
	//发起时间开始
	private String fqsjstart;
	//发起时间结束
	private String fqsjend;
	//部门[多]
	private String[] bms;
	//状态[多]
	private String[] zts;

	public String getFqsjstart() {
		return fqsjstart;
	}

	public void setFqsjstart(String fqsjstart) {
		this.fqsjstart = fqsjstart;
	}

	public String getFqsjend() {
		return fqsjend;
	}

	public void setFqsjend(String fqsjend) {
		this.fqsjend = fqsjend;
	}

	public String[] getBms() {
		return bms;
	}

	public void setBms(String[] bms) {
		this.bms = bms;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getPdztmc() {
		return pdztmc;
	}

	public void setPdztmc(String pdztmc) {
		this.pdztmc = pdztmc;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getGdzcbh() {
		return gdzcbh;
	}

	public void setGdzcbh(String gdzcbh) {
		this.gdzcbh = gdzcbh;
	}

	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	public String getXlh() {
		return xlh;
	}

	public void setXlh(String xlh) {
		this.xlh = xlh;
	}

	public String getXsybmmc() {
		return xsybmmc;
	}

	public void setXsybmmc(String xsybmmc) {
		this.xsybmmc = xsybmmc;
	}

	public String getSydd() {
		return sydd;
	}

	public void setSydd(String sydd) {
		this.sydd = sydd;
	}

	public String getSblxmc() {
		return sblxmc;
	}

	public void setSblxmc(String sblxmc) {
		this.sblxmc = sblxmc;
	}

	public String getSbbmfzrmc() {
		return sbbmfzrmc;
	}

	public void setSbbmfzrmc(String sbbmfzrmc) {
		this.sbbmfzrmc = sbbmfzrmc;
	}

	public String getFqsj() {
		return fqsj;
	}

	public void setFqsj(String fqsj) {
		this.fqsj = fqsj;
	}


	public String getPdry() {
		return pdry;
	}


	public void setPdry(String pdry) {
		this.pdry = pdry;
	}


	public String getFqry() {
		return fqry;
	}


	public void setFqry(String fqry) {
		this.fqry = fqry;
	}


	public String getBm() {
		return bm;
	}


	public void setBm(String bm) {
		this.bm = bm;
	}

	public String getPdrymc() {
		return pdrymc;
	}

	public void setPdrymc(String pdrymc) {
		this.pdrymc = pdrymc;
	}

	public String getFqrymc() {
		return fqrymc;
	}

	public void setFqrymc(String fqrymc) {
		this.fqrymc = fqrymc;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
