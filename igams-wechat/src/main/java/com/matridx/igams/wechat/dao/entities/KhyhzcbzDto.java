package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

@Alias(value="KhyhzcbzDto")
public class KhyhzcbzDto extends KhyhzcbzModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> htids;

	private String sfcss;//收费测试数

	private String yhxm;//优惠项目

	private String yhxsdm;//优惠形式代码

	private String yhflcskz1;//优惠分类参数扩展1

	private String dcsl;//达成数量，测试数或者金额

	private List<Map<String,Object>> sjlist;

	private List<KhyhzcbzDto> yhzclist;

	private String yhyjmc;//优惠依据名称

	private String yhxs_flg;//优惠形式flg

	public String getYhxs_flg() {
		return yhxs_flg;
	}

	public void setYhxs_flg(String yhxs_flg) {
		this.yhxs_flg = yhxs_flg;
	}

	public String getYhyjmc() {
		return yhyjmc;
	}

	public void setYhyjmc(String yhyjmc) {
		this.yhyjmc = yhyjmc;
	}

	public List<KhyhzcbzDto> getYhzclist() {
		return yhzclist;
	}

	public void setYhzclist(List<KhyhzcbzDto> yhzclist) {
		this.yhzclist = yhzclist;
	}

	public String getDcsl() {
		return dcsl;
	}

	public void setDcsl(String dcsl) {
		this.dcsl = dcsl;
	}



	public String getYhflcskz1() {
		return yhflcskz1;
	}

	public void setYhflcskz1(String yhflcskz1) {
		this.yhflcskz1 = yhflcskz1;
	}

	public List<Map<String, Object>> getSjlist() {
		return sjlist;
	}

	public void setSjlist(List<Map<String, Object>> sjlist) {
		this.sjlist = sjlist;
	}

	public String getSfcss() {
		return sfcss;
	}

	public void setSfcss(String sfcss) {
		this.sfcss = sfcss;
	}

	public String getYhxm() {
		return yhxm;
	}

	public void setYhxm(String yhxm) {
		this.yhxm = yhxm;
	}

	public String getYhxsdm() {
		return yhxsdm;
	}

	public void setYhxsdm(String yhxsdm) {
		this.yhxsdm = yhxsdm;
	}

	public List<String> getHtids() {
		return htids;
	}

	public void setHtids(List<String> htids) {
		this.htids = htids;
	}
}
