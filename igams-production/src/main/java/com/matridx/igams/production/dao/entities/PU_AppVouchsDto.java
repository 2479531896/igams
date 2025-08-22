package com.matridx.igams.production.dao.entities;

import java.util.List;

public class PU_AppVouchsDto extends PU_AppVouchsModel{

	//请购明细ids
	private List<String> autoIds;

	public List<String> getAutoIds() {
		return autoIds;
	}

	public void setAutoIds(List<String> autoIds) {
		this.autoIds = autoIds;
	}

}
