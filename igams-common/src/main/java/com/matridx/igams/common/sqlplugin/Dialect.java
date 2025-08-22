package com.matridx.igams.common.sqlplugin;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.DataFilterModel;
import com.matridx.igams.common.dao.DataPermissionModel;

public abstract class Dialect {
	public enum Type {
		MYSQL, ORACLE ,POSTGRESQL
	}

	public abstract String generatePageSql(String sql, BaseModel page);
	
	/**
	 * 为普通SQL附加数据权限控制
	 * @param sql
	 * @param page
	 * @param dataPermissionModel
	 * @return
	 */
	public abstract String generatePermissionSql(String sql, DataPermissionModel dataPermissionModel);
	
	/**
	 * 为普通SQL附加数据过滤
	 * @param sql
	 * @param page
	 * @param dataFilterModel
	 * @return
	 */
	public abstract String generateFilterSql(String sql, DataFilterModel dataFilterModel);
}
