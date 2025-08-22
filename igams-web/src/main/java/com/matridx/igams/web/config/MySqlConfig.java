package com.matridx.igams.web.config;

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

/**
 * @author : 郭祥杰
 * @date :
 */
@Configuration
@MapperScan(basePackages= MySqlConfig.PACKAGE,sqlSessionFactoryRef="mysqlSqlSessionFactory")
public class MySqlConfig {
    private final Logger logger = LoggerFactory.getLogger(MySqlConfig.class);


    //精确到sqlserver包
    public static final String PACKAGE = "com.matridx.igams.web.dao.matridxsql";
    public static final String ALIAS_PACKAGE = "com.matridx.igams.web";
    public static final String MAPPER_LOCATION = "com/matridx/igams/web/dao/matridxmapper/*.xml";

    @Value("${mysql.datasource.url:}")
    private String url;

    @Value("${mysql.datasource.username:}")
    private String user;

    @Value("${mysql.datasource.password:}")
    private String password;

    @Value("${mysql.datasource.driverClassName:}")
    private String driverClass;

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

    @Value("${mysql.datasource.validationQuery:}")
    private String validationQuery;

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

//    @Value("${spring.datasource.druidLoginName}")
//    private String druidLoginName;

//    @Value("${spring.datasource.druidPassword}")
//    private String druidPassword;

    @Value("${spring.datasource.socketTimeout:30000}")
    private int socketTimeout;

    @Value("${spring.datasource.removeAbandoned:}")
    private boolean removeAbandoned;

    @Value("${spring.datasource.removeAbandonedTimeout:}")
    private int removeAbandonedTimeout;

    @Bean(name = "mySqlDataSource")
    @Qualifier("mySqlDataSource")
    public DataSource mySqlDataSource() {
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

    @Bean(name = "mySqlTransactionManager")
    public DataSourceTransactionManager mySqlTransactionManager() {
        return new DataSourceTransactionManager(mySqlDataSource());
    }

    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mySqlDataSource") DataSource myDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(myDataSource);
        sessionFactory.setVfs(SpringBootVFS.class);
        sessionFactory.setTypeAliasesPackage(MySqlConfig.ALIAS_PACKAGE);

        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MySqlConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
