package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="YsmxDto")
public class YsmxDto extends YsmxModel {

	//科目大类名称
	private String kmdlmc;
	//科目分类名称
	private String kmflmc;

	public String getKmdlmc() {
		return kmdlmc;
	}

	public void setKmdlmc(String kmdlmc) {
		this.kmdlmc = kmdlmc;
	}

	public String getKmflmc() {
		return kmflmc;
	}

	public void setKmflmc(String kmflmc) {
		this.kmflmc = kmflmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
