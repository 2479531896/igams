package com.matridx.igams.common.data;

import com.matridx.igams.common.dao.DataFilterModel;
import com.matridx.igams.common.dao.DataFilterSingleModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import com.matridx.igams.common.enums.DataFilterTypeEnum;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 数据过滤那点事；可附加通用查询语句；
 * @author goofus
 *
 */
public final class DataFilter {
	
	final static DataFilter DF = new DataFilter();
	
	/**
	 * 添加一个数据过滤。
	 * @param model 具体业务的model，不应为空。
	 * @param filterType com.matridx.igams.common.enums.DataFilterTypeEnum，不应为空
	 * @param columnName 具体的表字段名或字段别名，若有别名应传别名，不应为空
	 * @author goofus
	 */
	public static DataFilter add(BaseBasicModel model,
			DataFilterTypeEnum filterType, String columnName) {
		return add(model, filterType, null, columnName);
	}
	
	/**
	 * 添加一个数据过滤。
	 * @param model 具体业务的model，不应为空。
	 * @param filterType com.matridx.igams.common.enums.DataFilterTypeEnum，不应为空
	 * @param tableAlias 具体表的别名，传null或空串表示无别名
	 * @param columnName 具体的表字段名或字段别名，若有别名应传别名，不应为空
	 * @author goofus
	 */
	public static DataFilter add(BaseBasicModel model,
			DataFilterTypeEnum filterType, String tableAlias,
			String columnName) {
		if(null == model || null == filterType || StringUtils.isBlank(columnName)) return DF;
		if(StringUtils.isBlank(tableAlias)) tableAlias = "";
		
		DataFilterModel dataFilterModel = model.dataFilterModel;
		DataFilterSingleModel filter = new DataFilterSingleModel();
		filter.setFilterType(filterType);
		filter.setTableAlias(tableAlias);
		filter.setColumnName(columnName);
		dataFilterModel.addFilter(filter);
		
		return DF;
	}
	
	/**
	 * 对SQL附加数据过滤
	 * @param sql JDBC SQL
	 * @param dataFilterModel 数据过滤器Model实例
	 * @author goofus
	 */
	public static String handleFilter(String sql, DataFilterModel dataFilterModel){
		StringBuffer rawSql = new StringBuffer(sql);
		
		List<DataFilterSingleModel> filters = dataFilterModel.getFilters();
		// User user = dataFilterModel.getUser();//当前用户
		// if (null == user || null == dataFilterModel
		// 		|| null == filters
		// 		|| filters.size() < 1){
		// 	return rawSql.toString();
		// }
		if ( null == filters
				|| filters.isEmpty()){
			return rawSql.toString();
		}
		
		//处理start.
		StringBuffer dfSql = new StringBuffer();
        for (DataFilterSingleModel single : filters) {
            String type = single.getFilterType().getCode();
            String columnName = single.getColumnName();
            if (StringUtils.isBlank(type) || StringUtils.isBlank(columnName)) continue;
            String tabAlias = single.getTableAlias();

            if (DataFilterTypeEnum.FILTER_TYPE_SHCZJS.getCode().equalsIgnoreCase(type)) {
                if (StringUtils.isBlank(tabAlias)) tabAlias = "inner_table";
                dfSql.append("select (select count(1) from matridx_shxx shxx where shxx.ywid=")
                        .append(tabAlias).append(".").append(columnName)
                        .append(" and shxx.shsj>(select shgc.sqsj from matridx_shgc shgc where shgc.ywid=")
                        .append(tabAlias).append(".").append(columnName)
                        .append(" )) as shczjs, ")
                        .append(tabAlias).append(".* from ( ").append(rawSql).append(" ) ").append(tabAlias);
            }

        }
		if(dfSql.length() > 0)
			rawSql = dfSql;
		//end.
		
		return rawSql.toString();
	}

	//private static ICommonSqlService commonSqlService = (ICommonSqlService) ServiceFactory.getService(ICommonSqlService.class);
	
}
