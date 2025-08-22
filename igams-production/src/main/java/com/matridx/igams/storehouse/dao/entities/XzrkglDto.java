package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzrkglDto")
public class XzrkglDto extends XzrkglModel{
	//入库人员名称
	private String rkrymc;
	private String qgmx_json;//请购明细
	private String qgmx_yjson;//原请购明细
	private String ddid;//钉钉id
	//库位名称
	private String kwmc;
	//开始日期
	private String xzrksjstart;
	//结束日期
	private String xzrksjend;
	//库位ids
	private String[] kws;
	//入库类别参数代码
	private String rklbdm;
	//入库类别参数名称
	private String rklbmc;
	//入库类别[多]
	private String[] rklbs;
	//入库数量
	private String rksl;
	//库存量
	private String kcl;
	//请购mxid
	private String qgmxid;
	//行政库存id
	private String xzkcid;
	//行政库存信息库存量
	private String xzkcl;
	//货物名称
	private String hwmc;
	//货物标准
	private String hwbz;
	//请购明细ids
	private String qgmxids;
	//原库位id
	private String ykwid;
	private String entire;

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getYkwid() {
		return ykwid;
	}

	public void setYkwid(String ykwid) {
		this.ykwid = ykwid;
	}

	public String getQgmx_yjson() {
		return qgmx_yjson;
	}

	public void setQgmx_yjson(String qgmx_yjson) {
		this.qgmx_yjson = qgmx_yjson;
	}

	public String getQgmxids() {
		return qgmxids;
	}

	public void setQgmxids(String qgmxids) {
		this.qgmxids = qgmxids;
	}

	public String getHwmc() {
		return hwmc;
	}

	public void setHwmc(String hwmc) {
		this.hwmc = hwmc;
	}

	public String getHwbz() {
		return hwbz;
	}

	public void setHwbz(String hwbz) {
		this.hwbz = hwbz;
	}

	public String getXzkcl() {
		return xzkcl;
	}

	public void setXzkcl(String xzkcl) {
		this.xzkcl = xzkcl;
	}

	public String getRksl() {
		return rksl;
	}

	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getXzkcid() {
		return xzkcid;
	}

	public void setXzkcid(String xzkcid) {
		this.xzkcid = xzkcid;
	}

	public String[] getRklbs() {
		return rklbs;
	}

	public void setRklbs(String[] rklbs) {
		this.rklbs = rklbs;
		for(int i=0;i<this.rklbs.length;i++)
		{
			this.rklbs[i] = this.rklbs[i].replace("'", "");
		}
	}

	public String getRklbdm() {
		return rklbdm;
	}

	public void setRklbdm(String rklbdm) {
		this.rklbdm = rklbdm;
	}

	public String getRklbmc() {
		return rklbmc;
	}

	public void setRklbmc(String rklbmc) {
		this.rklbmc = rklbmc;
	}

	public String[] getKws() {
		return kws;
	}

	public void setKws(String[] kws) {
		this.kws = kws;
	}

	public String getXzrksjstart() {
		return xzrksjstart;
	}

	public void setXzrksjstart(String xzrksjstart) {
		this.xzrksjstart = xzrksjstart;
	}

	public String getXzrksjend() {
		return xzrksjend;
	}

	public void setXzrksjend(String xzrksjend) {
		this.xzrksjend = xzrksjend;
	}

	public String getKwmc() {
		return kwmc;
	}

	public void setKwmc(String kwmc) {
		this.kwmc = kwmc;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getQgmx_json() {
		return qgmx_json;
	}

	public void setQgmx_json(String qgmx_json) {
		this.qgmx_json = qgmx_json;
	}

	public String getRkrymc() {
		return rkrymc;
	}

	public void setRkrymc(String rkrymc) {
		this.rkrymc = rkrymc;
	}
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
