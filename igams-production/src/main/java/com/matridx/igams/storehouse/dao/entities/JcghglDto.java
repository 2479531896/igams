package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JcghglDto")
public class JcghglDto extends JcghglModel{
	//部门名称
	private String bmmc;

	//部门代码
	private String bmdm;

	//单位名称
	private String dwmc;

	//单位代码
	private String dwdm;

	private String entire;

	private String ghrqstart;
	private String ghrqend;
	private String[] zts;

	//审核跳转标记
	private String xsbj;
	private String ghmx_json;
	//单位类型名称
	private String dwlxmc;

	//仓库ID
	private String ckid;
	//库位编号
	private String kwbh;
	//真实姓名
	private String zsxm;
	//机构ID
	private String jgid;
	//机构名称
	private String jgmc;
	//机构代码
	private String jgdm;
	//供应商ID
	private String gysid;
	//供应商名称
	private String gysmc;
	//供应商代码
	private String gysdm;
	//客户ID
	private String khid;
	//客户名称
	private String khmc;
	//客户代码
	private String khdm;
	private String dwlxdm;
	//通过IDs
	private List<String> tgids;
	//U8业务员code
	private String usercode;
	//销售类型名称
	private String xslxmc;

	public String getXslxmc() {
		return xslxmc;
	}

	public void setXslxmc(String xslxmc) {
		this.xslxmc = xslxmc;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public List<String> getTgids() {
		return tgids;
	}

	public void setTgids(List<String> tgids) {
		this.tgids = tgids;
	}

	public String getDwlxdm() {
		return dwlxdm;
	}

	public void setDwlxdm(String dwlxdm) {
		this.dwlxdm = dwlxdm;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getGysid() {
		return gysid;
	}

	public void setGysid(String gysid) {
		this.gysid = gysid;
	}

	public String getGysmc() {
		return gysmc;
	}

	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}

	public String getGysdm() {
		return gysdm;
	}

	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}

	public String getKhid() {
		return khid;
	}

	public void setKhid(String khid) {
		this.khid = khid;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getKhdm() {
		return khdm;
	}

	public void setKhdm(String khdm) {
		this.khdm = khdm;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getCkid() {
		return ckid;
	}

	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getKwbh() {
		return kwbh;
	}

	public void setKwbh(String kwbh) {
		this.kwbh = kwbh;
	}

	public String getDwlxmc() {
		return dwlxmc;
	}

	public void setDwlxmc(String dwlxmc) {
		this.dwlxmc = dwlxmc;
	}

	public String getGhmx_json() {
		return ghmx_json;
	}

	public void setGhmx_json(String ghmx_json) {
		this.ghmx_json = ghmx_json;
	}

	public String getXsbj() {
		return xsbj;
	}

	public void setXsbj(String xsbj) {
		this.xsbj = xsbj;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String getGhrqstart() {
		return ghrqstart;
	}

	public void setGhrqstart(String ghrqstart) {
		this.ghrqstart = ghrqstart;
	}

	public String getGhrqend() {
		return ghrqend;
	}

	public void setGhrqend(String ghrqend) {
		this.ghrqend = ghrqend;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getBmdm() {
		return bmdm;
	}

	public void setBmdm(String bmdm) {
		this.bmdm = bmdm;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getDwdm() {
		return dwdm;
	}

	public void setDwdm(String dwdm) {
		this.dwdm = dwdm;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
