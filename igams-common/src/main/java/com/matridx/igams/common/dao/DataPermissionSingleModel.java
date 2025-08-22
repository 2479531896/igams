package com.matridx.igams.common.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;

/**
 * 单个数据权限定义
 * @author goofus
 *
 */
public class DataPermissionSingleModel implements Serializable {

	private static final long serialVersionUID = 371564264134333380L;
	
	private String busiType;//业务类型
	private DataPermissionTypeEnum permissionType;//权限类型
	private String tableAlias;//表别名
	private String columnName;//字段名或别名
//	private AuditTypeEnum auditType;//审核单个审核类别的情况
	private final Set<AuditTypeEnum> auditTypes = new HashSet<>();//审核多个审核类别的情况
	private SsdwTableEnum ssdw;//所属单位表枚举，多院系处理用
	private String jdbcSql;//纯JDBC SQL
	
	public String getJdbcSql() {
		return jdbcSql;
	}
	public void setJdbcSql(String jdbcSql) {
		this.jdbcSql = jdbcSql;
	}
//	public AuditTypeEnum getAuditType() {
//		return auditType;
//	}
//	public void setAuditType(AuditTypeEnum auditType) {
//		this.auditType = auditType;
//	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public DataPermissionTypeEnum getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(DataPermissionTypeEnum permissionType) {
		this.permissionType = permissionType;
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
	public SsdwTableEnum getSsdw() {
		return ssdw;
	}
	public void setSsdw(SsdwTableEnum ssdw) {
		this.ssdw = ssdw;
	}
	public Set<AuditTypeEnum> getAuditTypes() {
		return auditTypes;
	}
}
