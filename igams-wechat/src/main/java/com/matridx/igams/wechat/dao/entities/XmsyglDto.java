package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;


@Alias(value="XmsyglDto")
public class XmsyglDto extends XmsyglModel{
	//检测项目名称
	private String jcxmmc;
	//业务IDs
	private List<String> ywids;
	// 信息对应kzcs1
	private String xxdy_kzcs1;
	// 信息对应kzcs7
	private String xxdy_kzcs7;

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public List<String> getYwids() {
		return ywids;
	}

	public void setYwids(List<String> ywids) {
		this.ywids = ywids;
	}

	public String getXxdy_kzcs1() {
		return xxdy_kzcs1;
	}

	public void setXxdy_kzcs1(String xxdy_kzcs1) {
		this.xxdy_kzcs1 = xxdy_kzcs1;
	}

	public String getXxdy_kzcs7() {
		return xxdy_kzcs7;
	}

	public void setXxdy_kzcs7(String xxdy_kzcs7) {
		this.xxdy_kzcs7 = xxdy_kzcs7;
	}

	private static final long serialVersionUID = 1L;

}
