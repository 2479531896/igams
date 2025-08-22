package com.matridx.igams.production.dao.entities;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RdRecordsDto extends RdRecordsModel{
	//关联申请单id
	private String iMaIDs;
	//复数ID
	private List<String> ids;
		
	public List<String> getIds() {
		return ids;
	}
	public void setIds(String ids) {
		List<String> list;
		String[] str = ids.split(",");
		list = Arrays.asList(str);
		this.ids = list;
	}
	public void setIds(List<String> ids) {
		if(!CollectionUtils.isEmpty(ids)){
            ids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.ids = ids;
	}
	public String getiMaIDs() {
		return iMaIDs;
	}

	public void setiMaIDs(String iMaIDs) {
		this.iMaIDs = iMaIDs;
	}
	
}
