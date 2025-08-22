package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Arrays;
import java.util.List;

@Alias(value="DsrwqxDto")
public class DsrwqxDto extends DsrwqxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户名
	private String yhm;
	//用户真实姓名
	private String zsxm;
	//是否锁定
	private String sfsd;
	//查询名称
	private String cxmc;
	//备注
	private String bz;
	//查询区分名称
	private String cxqfmc;
	//显示方式
	private String xsfsmc;
	private String lx;

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	//复数ID查询ID
	private List<String> rwids;
	public List<String> getRwids() {
		return rwids;
	}
	public void setRwids(String rwids) {
		List<String> list;
		String[] str = rwids.split(",");
		list = Arrays.asList(str);
		this.rwids = list;
	}
	public void setRwids(List<String> rwids) {
		if(rwids!=null&&!rwids.isEmpty()){
			rwids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.rwids = rwids;
	}
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm) {
		this.yhm = yhm;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getSfsd() {
		return sfsd;
	}
	public void setSfsd(String sfsd) {
		this.sfsd = sfsd;
	}
	public String getCxmc() {
		return cxmc;
	}
	public void setCxmc(String cxmc) {
		this.cxmc = cxmc;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getCxqfmc() {
		return cxqfmc;
	}
	public void setCxqfmc(String cxqfmc) {
		this.cxqfmc = cxqfmc;
	}
	public String getXsfsmc() {
		return xsfsmc;
	}
	public void setXsfsmc(String xsfsmc) {
		this.xsfsmc = xsfsmc;
	}
	
	

}
