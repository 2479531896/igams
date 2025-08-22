package com.matridx.igams.common.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="GrszDto")
public class GrszDto extends GrszModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//真实姓名
	private String zsxm;
	//钉钉ID
	private String ddid;
	private String yhm;
	//个人设置标记 :0 不操作，1操作
	private String grsz_flg;

	private String[] szlbs;
	
	private String szzisnull;//存空设置值时候用到
	private String csmc;
	private String dyxxJson;
	private String cskz1;
	private String cskz2;
	private String xxnr;
	private String hbid;
	private String hbmc;
	private String hbJson;
	private String yhJson;
	private String flag;
	private String szlbmc;
	private String yhmc;
	private String entire;
	private List<String> xxlxs;
	private String hbFlg;

	public String getHbFlg() {
		return hbFlg;
	}

	public void setHbFlg(String hbFlg) {
		this.hbFlg = hbFlg;
	}

	public String getSzlbmc() {
		return szlbmc;
	}

	public void setSzlbmc(String szlbmc) {
		this.szlbmc = szlbmc;
	}

	public String getYhmc() {
		return yhmc;
	}

	public void setYhmc(String yhmc) {
		this.yhmc = yhmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}
	public List<String> getXxlxs() {
		return xxlxs;
	}
	public void setXxlxs(String xxlxs) {
		List<String> list = new ArrayList<>();
		if(StringUtil.isNotBlank(xxlxs)) {
			String[] str = xxlxs.split(",");
			list = Arrays.asList(str);
		}
		this.xxlxs = list;
	}
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getHbJson() {
		return hbJson;
	}

	public void setHbJson(String hbJson) {
		this.hbJson = hbJson;
	}

	public String getYhJson() {
		return yhJson;
	}

	public void setYhJson(String yhJson) {
		this.yhJson = yhJson;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}

	public String getXxnr() {
		return xxnr;
	}

	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}

	public String getCsmc() {
		return csmc;
	}

	public void setCsmc(String csmc) {
		this.csmc = csmc;
	}

	public String getDyxxJson() {
		return dyxxJson;
	}

	public void setDyxxJson(String dyxxJson) {
		this.dyxxJson = dyxxJson;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

	public String getCskz2() {
		return cskz2;
	}

	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getGrsz_flg() {
		return grsz_flg;
	}
	public void setGrsz_flg(String grsz_flg) {
		this.grsz_flg = grsz_flg;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String[] getSzlbs() {
		return szlbs;
	}

	public void setSzlbs(String[] szlbs) {
		this.szlbs = szlbs;
	}
	
	public String getSzzisnull() {
		return szzisnull;
	}

	public void setSzzisnull(String szzisnull) {
		this.szzisnull = szzisnull;
	}
}
