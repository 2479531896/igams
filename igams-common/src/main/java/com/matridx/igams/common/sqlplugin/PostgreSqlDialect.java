package com.matridx.igams.common.sqlplugin;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.DataFilterModel;
import com.matridx.igams.common.dao.DataPermissionModel;
import com.matridx.igams.common.data.DataFilter;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.springboot.util.base.StringUtil;

public class PostgreSqlDialect extends Dialect {

	final String[] SqlStr2 = {"*","'",";","or ","-","--","+","//","/","%","#"};//特殊字符
	
	@Override
	public String generatePageSql(String sql, BaseModel page) {
		// TODO Auto-generated method stub
		if(page != null){
			StringBuilder pageSql = new StringBuilder();
			
			if (!StringUtil.isEmpty(page.getSortName())) {
				
				String sortName = checkParams(page.getSortName());
				String sortOrder = checkParams(page.getSortOrder());
				String sortLastName = checkParams(page.getSortLastName());
				String sortLastOrder = checkParams(page.getSortLastOrder());
				
				pageSql.append(sql);
				pageSql.append(" order by ");
				pageSql.append( sortName );
				if(StringUtil.isNotBlank(sortOrder)){
					pageSql.append(" ");
					pageSql.append(sortOrder);
				}else{
					pageSql.append(" asc");
				}
				if(StringUtil.isNotBlank(sortLastName)) {
					pageSql.append(",");
					pageSql.append( sortLastName );
					if(StringUtil.isNotBlank(sortLastOrder)){
						pageSql.append(" ");
						pageSql.append(sortLastOrder);
					}else{
						pageSql.append(" asc");
					}
				}
				pageSql.append(" LIMIT ");
				pageSql.append(page.getPageSize());
				pageSql.append(" OFFSET ");
				pageSql.append((page.getPageNumber() - 1) * page.getPageSize());
			}else{
				pageSql.append(sql);
				pageSql.append(" tmp_tb LIMIT ");
				pageSql.append(page.getPageSize());
				pageSql.append(" OFFSET ");
				pageSql.append((page.getPageNumber() - 1) * page.getPageSize());
			}
			
			sql = pageSql.toString();
		}
		return sql;
	}

	@Override
	public String generatePermissionSql(String sql, DataPermissionModel dataPermissionModel) {
		// TODO Auto-generated method stub
		if (null == dataPermissionModel || null == dataPermissionModel.getUsers()){
			return sql;
		}
		return DataPermission.handlePermission(sql, dataPermissionModel);
	}
	
	@Override
	public String generateFilterSql(String sql, DataFilterModel dataFilterModel) {
		// if (null == dataFilterModel || null == dataFilterModel.getUser()){
		// 	return sql;
		// }
		if (null == dataFilterModel){
			return sql;
		}
		return DataFilter.handleFilter(sql, dataFilterModel);
	}

	/**
	 * 替换其中的特殊字符
	 * @param info
	 * @return
	 */
	private String checkParams(String info) {
		if (StringUtil.isNotBlank(info)){
            for (String s : SqlStr2) {
                if (info.contains(s)) {
                    info = info.replaceAll(s, "");
                }
            }
		}
		return info;
	}
}
