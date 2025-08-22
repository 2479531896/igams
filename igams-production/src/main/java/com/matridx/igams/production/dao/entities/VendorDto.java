package com.matridx.igams.production.dao.entities;

public class VendorDto extends VendorModel{
	//供应商代码（修改时代替主键id）
	private String gysdm;

	public String getGysdm() {
		return gysdm;
	}

	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}
}
