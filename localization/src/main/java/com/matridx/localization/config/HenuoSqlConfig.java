package com.matridx.localization.config;

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
@MapperScan(basePackages= HenuoSqlConfig.PACKAGE,sqlSessionFactoryRef="HenuosqlSqlSessionFactory")
public class HenuoSqlConfig {
    private final Logger logger = LoggerFactory.getLogger(HenuoSqlConfig.class);


    //精确到sqlserver包
    public static final String PACKAGE = "com.matridx.localization.dao.henuosql";
    public static final String ALIAS_PACKAGE = "com.matridx.localization";
    public static final String MAPPER_LOCATION = "com/matridx/localization/dao/henuomapper/*.xml";

    @Value("${henuosql.datasource.url:}")
    private String url;

    @Value("${henuosql.datasource.username:}")
    private String user;

    @Value("${henuosql.datasource.password:}")
    private String password;

    @Value("${henuosql.datasource.driverClassName:}")
    private String driverClass;
    @Value("${henuosql.datasource.validationQuery:}")
    private String validationQuery;
    @Value("${spring.datasource.initialSize:10}")
    private int initialSize;

    @Value("${spring.datasource.minIdle:10}")
    private int minIdle;

    @Value("${spring.datasource.maxActive:10}")
    private int maxActive;

    @Value("${spring.datasource.maxWait:10}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis:10}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis:10}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.testWhileIdle:false}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow:false}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn:false}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements:false}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize:10}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.filters:}")
    private String filters;

    @Value("${spring.datasource.connectionProperties:}")
    private String connectionProperties;

    @Value("${spring.datasource.useGlobalDataSourceStat:false}")
    private boolean useGlobalDataSourceStat;

    @Value("${spring.datasource.removeAbandoned:false}")
    private boolean removeAbandoned;

    @Value("${spring.datasource.removeAbandonedTimeout:10}")
    private int removeAbandonedTimeout;

    @Bean(name = "HenuoSqlDataSource")
    @Qualifier("HenuoSqlDataSource")
    public DataSource HenuoSqlDataSource() {
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

    @Bean(name = "HenuoSqlTransactionManager")
    public DataSourceTransactionManager HenuoSqlTransactionManager() {
        return new DataSourceTransactionManager(HenuoSqlDataSource());
    }

    @Bean(name = "HenuosqlSqlSessionFactory")
    public SqlSessionFactory HenuosqlSqlSessionFactory(@Qualifier("HenuoSqlDataSource") DataSource HenuoDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(HenuoDataSource);
        sessionFactory.setVfs(SpringBootVFS.class);
        sessionFactory.setTypeAliasesPackage(HenuoSqlConfig.ALIAS_PACKAGE);

        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(HenuoSqlConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
