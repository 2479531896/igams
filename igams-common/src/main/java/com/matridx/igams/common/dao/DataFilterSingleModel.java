package com.matridx.igams.common.dao;

import java.io.Serializable;

import com.matridx.igams.common.enums.DataFilterTypeEnum;

/**
 * 单个数据过滤器定义
 * @author goofus
 *
 */
public class DataFilterSingleModel implements Serializable {

	private static final long serialVersionUID = 371564264134333380L;
	
	private String busiType;//业务类型
	private DataFilterTypeEnum filterType;//过滤器类型
	private String tableAlias;//表别名
	private String columnName;//字段名或别名
	
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public DataFilterTypeEnum getFilterType() {
		return filterType;
	}
	public void setFilterType(DataFilterTypeEnum filterType) {
		this.filterType = filterType;
	}
	public String getTableAlias() {
		return tableAlias;
	}
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	

}
