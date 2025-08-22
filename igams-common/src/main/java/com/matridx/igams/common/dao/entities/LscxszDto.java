package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Arrays;
import java.util.List;

@Alias(value="LscxszDto")
public class LscxszDto extends LscxszModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//查询区分代码
	private String cxqfdm;
	//查询区分名称
	private String cxqfmc;
	//显示方式
	private String xsfsmc;
	//查询条件(复数)
	private List<String> cxtjs;
	//查询区分代码(复数)
	private List<String> cxqfdms;
	private List<String> cxqfs;
	private List<String> xsfss;
	//显示代码
	private String xsfsdm;
	//用户ID
	private String yhid;
	//豁免ALL类型的标记，如果为1 则 把ALL也检索出来
	private String allflg;
	//复数ID查询ID
	private List<String> cids;
	private String jsid;
	private String cxqfcskz1;

	public String getCxqfcskz1() {
		return cxqfcskz1;
	}

	public void setCxqfcskz1(String cxqfcskz1) {
		this.cxqfcskz1 = cxqfcskz1;
	}

	public List<String> getCxqfs() {
		return cxqfs;
	}

	public void setCxqfs(List<String> cxqfs) {
		if(cxqfs!=null && !cxqfs.isEmpty()){
			cxqfs.replaceAll(s -> s.replace("[", "").replace("]", "").replace("'", "").trim());
		}
		this.cxqfs = cxqfs;
	}

	public void setCxqfs(String cxqfs) {
		List<String> list;
		String[] str = cxqfs.split(",");
		list = Arrays.asList(str);
		this.cxqfs = list;
	}
	public List<String> getXsfss() {
		return xsfss;
	}

	public void setXsfss(String xsfss) {
		List<String> list;
		String[] str = xsfss.split(",");
		list = Arrays.asList(str);
		this.xsfss = list;
	}

	public void setXsfss(List<String> xsfss) {
		if(xsfss!=null && !xsfss.isEmpty()){
			xsfss.replaceAll(s -> s.replace("[", "").replace("]", "").replace("'", "").trim());
		}
		this.xsfss = xsfss;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public List<String> getCids() {
		return cids;
	}
	public void setCids(List<String> cids) {
		this.cids = cids;
	}
	public String getXsfsdm() {
		return xsfsdm;
	}
	public void setXsfsdm(String xsfsdm) {
		this.xsfsdm = xsfsdm;
	}
	public List<String> getCxtjs() {
		return cxtjs;
	}
	public void setCxtjs(List<String> cxtjs) {
		this.cxtjs = cxtjs;
	}
	public String getXsfsmc() {
		return xsfsmc;
	}
	public void setXsfsmc(String xsfsmc) {
		this.xsfsmc = xsfsmc;
	}
	public String getCxqfmc() {
		return cxqfmc;
	}
	public void setCxqfmc(String cxqfmc) {
		this.cxqfmc = cxqfmc;
	}
	public String getCxqfdm() {
		return cxqfdm;
	}
	public void setCxqfdm(String cxqfdm) {
		this.cxqfdm = cxqfdm;
	}
	public List<String> getCxqfdms() {
		return cxqfdms;
	}
	public void setCxqfdms(List<String> cxqfdms) {
		this.cxqfdms = cxqfdms;
	}
	public void setCxqfdms(String cxqfdms) {
		List<String> list;
		String[] str = cxqfdms.split(",");
		list = Arrays.asList(str);
		this.cxqfdms = list;
	}
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid) {
		this.yhid = yhid;
	}
	public String getAllflg() {
		return allflg;
	}
	public void setAllflg(String allflg) {
		this.allflg = allflg;
	}
}
