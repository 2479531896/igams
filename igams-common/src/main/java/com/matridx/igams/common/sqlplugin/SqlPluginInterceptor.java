package com.matridx.igams.common.sqlplugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.DataFilterModel;
import com.matridx.igams.common.dao.DataPermissionModel;
import com.matridx.springboot.util.reflect.ReflectHelper;

/**
 * 可以拦截的类：
 * StatementHandler (prepare, parameterize, batch, update, query)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * ParameterHandler (getParameterObject, setParameters)
 * Executor (update, query, flushStatements, commit, rollback,getTransaction, close, isClosed)
 * @author linwu
 *
 */
@Component
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})})
public class SqlPluginInterceptor implements Interceptor{
	@Value("${sql.interceptor.dialect}")
	private String dialect; // 数据库方言
	@Value("${sql.interceptor.pagesqlid}")
	private String pageSqlId; // 分页Id,mapper.xml中需要拦截的ID(正则匹配)
	private static final String sqlName = "sql";
	private static final String delegateName = "delegate";
	private static final String statementName = "mappedStatement";
	private static final String dataPermissionName = "dataPermissionModel";
	private static final String dataFilterName = "dataFilterModel";
	private static final String pageNumber = "pageNumber";//当前页数
	private static final String pageSize = "pageSize";//每页条数
	private static final String pageStart = "pageStart";//起始条数
	//private static final String totalPage = "totalPage";//总页数
	private static final String totalNumber = "totalNumber";//总条数
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		//RoutingStatementHandler类似路由器，在其构造函数中会根据Mapper文件中设置的StatementType来选择使用
		//SimpleStatementHandler、PreparedStatementHandler和CallableStatementHandler
		if (!(invocation.getTarget() instanceof RoutingStatementHandler)) {
			return invocation.proceed();
		}
		
		RoutingStatementHandler statementHandler= (RoutingStatementHandler) invocation.getTarget();
		BaseStatementHandler baseHandler = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, delegateName);
		MappedStatement statement = (MappedStatement) ReflectHelper.getValueByFieldName(baseHandler, statementName);
		
		Connection connection = (Connection) invocation.getArgs()[0];
		
		BoundSql boundSql = statementHandler.getBoundSql();
		String sql = boundSql.getSql();
		
		String statementId = statement.getId();
		Dialect dialect2 = new PostgreSqlDialect();
		Object parameter = boundSql.getParameterObject();
		
		//权限处理
		Object dpo = ReflectHelper.getValueByFieldName(parameter, dataPermissionName);
		if(null != dpo){
			DataPermissionModel dataPermissionModel = (DataPermissionModel) dpo;
			sql = dialect2.generatePermissionSql(sql, dataPermissionModel);
		}
		
		//过滤器处理
		Object dfo = ReflectHelper.getValueByFieldName(parameter, dataFilterName);
		if(null != dfo){
			DataFilterModel dataFilterModel = (DataFilterModel) dfo;
			sql = dialect2.generateFilterSql(sql, dataFilterModel);
		}
		
		if (statementId.matches(pageSqlId)) { // 拦截需要分页的SQL
			// 分页SQL<select>中parameterType属性对应的实体参数
			if (parameter == null) {
				return invocation.proceed();
			}
			
			String countSql = "select count(0) as c from (" + sql + ") t_count "; // 记录统计

			PreparedStatement countStmt = connection.prepareStatement(countSql);
			//使用原boundSql，通过反射修改执行的sql
			ReflectHelper.setValueByFieldName(boundSql, sqlName, countSql);
			setParameters(countStmt, statement, boundSql, parameter);
			//end
			// 执行count SQl
			ResultSet rs = countStmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			
			try {
				// 当前页数
				int i_pageNumber = (int) ReflectHelper.getValueByFieldName(parameter, pageNumber);
				if (i_pageNumber == 0) {
					i_pageNumber = 1;
				}
				//
				int i_pageSize = (int) ReflectHelper.getValueByFieldName(parameter, pageSize);
				// 每页条数
				ReflectHelper.setValueByFieldName(parameter,pageNumber,i_pageNumber);
				// 起始条数
				ReflectHelper.setValueByFieldName(parameter,pageStart,(i_pageNumber-1)*i_pageSize);
				// 总条数
				ReflectHelper.setValueByFieldName(parameter,totalNumber,count);

				sql = dialect2.generatePageSql(sql, (BaseModel)parameter);

			} finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    countStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
		}
		
		// 将分页sql语句反射回BoundSql.
		ReflectHelper.setValueByFieldName(boundSql, sqlName, sql);

		return invocation.proceed();
	}
	
	private void setParameters(PreparedStatement ps,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {

		ErrorContext.instance().activity("setting parameters").object(
				mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();

		if (parameterMappings == null) {
			return;
		}

		Configuration configuration = mappedStatement.getConfiguration();
		TypeHandlerRegistry typeHandlerRegistry = configuration
				.getTypeHandlerRegistry();
		MetaObject metaObject = null;

		if (parameterObject != null) {
			metaObject = configuration.newMetaObject(parameterObject);
		}

		for (int i = 0; i < parameterMappings.size(); i++) {
			ParameterMapping parameterMapping = parameterMappings.get(i);

			if (parameterMapping.getMode() == ParameterMode.OUT) {
				continue;
			}

			Object value;

			String propertyName = parameterMapping.getProperty();
			PropertyTokenizer prop = new PropertyTokenizer(propertyName);

			if (parameterObject == null) {
				value = null;
			} else if (typeHandlerRegistry.hasTypeHandler(parameterObject
					.getClass())) {
				value = parameterObject;
			} else if (boundSql.hasAdditionalParameter(propertyName)) {
				value = boundSql.getAdditionalParameter(propertyName);
			} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
					&& boundSql.hasAdditionalParameter(prop.getName())) {
				value = boundSql.getAdditionalParameter(prop.getName());
				if (value != null) {
					value = configuration.newMetaObject(value).getValue(
							propertyName.substring(prop.getName().length()));
				}
			} else {
				value = metaObject == null ? null : metaObject
						.getValue(propertyName);
			}

			TypeHandler typeHandler = parameterMapping
					.getTypeHandler();
			if (typeHandler != null) {
				typeHandler.setParameter(ps, i + 1, value, parameterMapping
						.getJdbcType());
			}
		}
	}

	@Override
	public Object plugin(Object target) {
		// TODO Auto-generated method stub
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		dialect = properties.getProperty("sql.interceptor.dialect");
		pageSqlId = properties.getProperty("sql.interceptor.pagesqlid");
	}
	
	public String getDialect() {
		return dialect;
	}
}
