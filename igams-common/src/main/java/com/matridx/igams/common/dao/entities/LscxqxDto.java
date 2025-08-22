package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.Arrays;
import java.util.List;

@Alias(value="LscxqxDto")
public class LscxqxDto extends LscxqxModel{

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
	private List<String> cids;
	public List<String> getCids() {
		return cids;
	}
	public void setCids(String cids) {
		List<String> list;
		String[] str = cids.split(",");
		list = Arrays.asList(str);
		this.cids = list;
	}
	public void setCids(List<String> cids) {
		if(cids!=null&&!cids.isEmpty()){
            cids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.cids = cids;
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
