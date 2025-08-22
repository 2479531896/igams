package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbbgmbglDto")
public class HbbgmbglDto extends HbbgmbglModel{
	//检测项目名称
	private String jcxmmc;
	//检测子项目名称
	private String jczxmmc;
	//报告模板名称
	private String bgmbmc;
	private String bgmbmc2;

	public String getBgmbmc2() {
		return bgmbmc2;
	}

	public void setBgmbmc2(String bgmbmc2) {
		this.bgmbmc2 = bgmbmc2;
	}

	public String getBgmbmc() {
		return bgmbmc;
	}

	public void setBgmbmc(String bgmbmc) {
		this.bgmbmc = bgmbmc;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
