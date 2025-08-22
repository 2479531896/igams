package com.matridx.igams.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataFilterModel implements Serializable {

	private static final long serialVersionUID = 3715642641357933380L;
	
	// private User user;//当前用户
	private boolean activate = false;//是否激活权限控制，true表示启用
	/** 数据权限定义集 */
	private List<DataFilterSingleModel> filters = new ArrayList<>();
	
	public List<DataFilterSingleModel> getFilters() {
		return filters;
	}
	public void setFilters(List<DataFilterSingleModel> filters) {
		this.filters = filters;
	}
	public void addFilter(DataFilterSingleModel filter) {
		//if(null == this.user) this.user = SessionFactory.getUser();
		this.activate = true;
		this.filters.add(filter);
	}
	// public User getUser() {
	// 	return user;
	// }
	// public void setUser(User user) {
	// 	this.user = user;
	// }
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	

}
