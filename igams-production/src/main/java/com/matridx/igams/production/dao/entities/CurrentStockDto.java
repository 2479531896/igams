package com.matridx.igams.production.dao.entities;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrentStockDto extends  CurrentStockModel implements Cloneable{

//	数量标记
	private String iQuantitybj;

	public String getiQuantitybj() {
		return iQuantitybj;
	}

	public void setiQuantitybj(String iQuantitybj) {
		this.iQuantitybj = iQuantitybj;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

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
}
