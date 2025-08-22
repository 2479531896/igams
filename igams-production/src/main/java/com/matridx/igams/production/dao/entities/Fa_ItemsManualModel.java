package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="Fa_ItemsManualModel")
public class Fa_ItemsManualModel {
	private String sCardNum;
	private String man1063s;

	public String getsCardNum() {
		return sCardNum;
	}

	public void setsCardNum(String sCardNum) {
		this.sCardNum = sCardNum;
	}

	public String getMan1063s() {
		return man1063s;
	}

	public void setMan1063s(String man1063s) {
		this.man1063s = man1063s;
	}
}
