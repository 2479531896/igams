package com.matridx.igams.finance.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages= FinanceSqlServerConfig.PACKAGE , sqlSessionFactoryRef = "FinanceSqlSessionFactory")
public class FinanceSqlServerConfig {
	
	private final Logger logger = LoggerFactory.getLogger(FinanceSqlServerConfig.class);
	
	//精确到sqlserver包
	public static final String PACKAGE = "com.matridx.igams.finance.dao.financesql";
	public static final String ALIAS_PACKAGE = "com.matridx.igams.finance";
	public static final String MAPPER_LOCATION = "com/matridx/igams/finance/dao/financemapper/*.xml";
	
	@Value("${sqlserver.matridxds.url:}")
	private String url;

	@Value("${sqlserver.matridxds.username:}")
	private String user;
	
	@Value("${sqlserver.matridxds.password:}")
	private String password;
	
	@Value("${sqlserver.matridxds.driverClassName:}")
	private String driverClass;

	@Value("${sqlserver.matridxds.validationQuery:}")
	private String validationQuery;
	
	@Value("${spring.datasource.initialSize:}")  
	private int initialSize;  
	
	@Value("${spring.datasource.minIdle:}")  
	private int minIdle;  
	
	@Value("${spring.datasource.maxActive:}")  
	private int maxActive;  
	
	@Value("${spring.datasource.maxWait:}")  
	private int maxWait;  
	
	@Value("${spring.datasource.timeBetweenEvictionRunsMillis:}")  
	private int timeBetweenEvictionRunsMillis;  

	@Value("${spring.datasource.minEvictableIdleTimeMillis:}")  
	private int minEvictableIdleTimeMillis;  
	
	@Value("${spring.datasource.testWhileIdle:}")  
	private boolean testWhileIdle;  
	
	@Value("${spring.datasource.testOnBorrow:}")  
	private boolean testOnBorrow;  
	
	@Value("${spring.datasource.testOnReturn:}")  
	private boolean testOnReturn;  
	
	@Value("${spring.datasource.poolPreparedStatements:}")  
	private boolean poolPreparedStatements;

	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize:}")  
	private int maxPoolPreparedStatementPerConnectionSize;  
	
	@Value("${spring.datasource.filters:}")  
	private String filters;  
	
	@Value("${spring.datasource.connectionProperties:}")  
	private String connectionProperties;  
	
	@Value("${spring.datasource.useGlobalDataSourceStat:}")  
	private boolean useGlobalDataSourceStat;
	
	@Value("${spring.datasource.removeAbandoned:}")  
	private boolean removeAbandoned;

	@Value("${spring.datasource.removeAbandonedTimeout:}")  
	private int removeAbandonedTimeout;
	
	@Bean(name = "FinanceSqlServerDataSource")
	@Qualifier("FinanceSqlServerDataSource")
	public DataSource FinanceSqlServerDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		try {
			DBEncrypt encty = new DBEncrypt();
			dataSource.setDriverClassName(driverClass);
			dataSource.setUrl(encty.dCode(url));
			dataSource.setUsername(encty.dCode(user));
			dataSource.setPassword(encty.dCode(password));
			dataSource.setInitialSize(initialSize);	
			dataSource.setMinIdle(minIdle);	
			dataSource.setMaxActive(maxActive);	
			dataSource.setMaxWait(maxWait);	
			dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);	
			dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);	
			dataSource.setValidationQuery(validationQuery);	
			dataSource.setTestWhileIdle(testWhileIdle);	
			dataSource.setTestOnBorrow(testOnBorrow);	
			dataSource.setTestOnReturn(testOnReturn);	
			dataSource.setPoolPreparedStatements(poolPreparedStatements);	
			dataSource.setFilters(filters);	
			dataSource.setConnectionProperties(connectionProperties);  
			dataSource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
			dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
			dataSource.setRemoveAbandoned(removeAbandoned);
			dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		} catch (SQLException e) {	
			logger.error("druid configuration initialization filter", e);	
		}
		return dataSource;
	}
	
	@Bean(name = "FinanceTransactionManager")
	public DataSourceTransactionManager FinanceTransactionManager() {
		return new DataSourceTransactionManager(FinanceSqlServerDataSource());
	}

	@Bean(name = "FinanceSqlSessionFactory")
	public SqlSessionFactory FinanceSqlSessionFactory(@Qualifier("FinanceSqlServerDataSource") DataSource dataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setVfs(SpringBootVFS.class);
        sessionFactory.setTypeAliasesPackage(FinanceSqlServerConfig.ALIAS_PACKAGE);
        
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
				.getResources(FinanceSqlServerConfig.MAPPER_LOCATION));
		return sessionFactory.getObject();
	}
}
