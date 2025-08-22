package com.matridx.igams.common.config.ds;

import com.alibaba.druid.pool.DruidDataSource;
import com.matridx.igams.common.config.security.IgamsSqlSessionFactoryBean;
import com.matridx.igams.common.sqlplugin.SqlPluginInterceptor;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages=PostDSConfig.PACKAGE,sqlSessionFactoryRef="postsqlSqlSessionFactory")
public class PostDSConfig {
	
	private final Logger logger = LoggerFactory.getLogger(PostDSConfig.class);
	
	public static final String PACKAGE = "com.matridx.**.dao.post";
	public static final String ALIAS_PACKAGE = "com.matridx";
	public static final String MAPPER_LOCATION = "classpath*:com/matridx/**/dao/mapper/*.xml";
	
	@Value("${postsql.datasource.url}")
	private String url; 
	
	@Value("${postsql.datasource.username}")
    private String user;
    
    @Value("${postsql.datasource.password:}")
    private String password;
    
    @Value("${postsql.datasource.driverClassName}")
    private String driverClass;
    
    @Value("${spring.datasource.initialSize}")  
    private int initialSize;  
      
    @Value("${spring.datasource.minIdle}")  
    private int minIdle;  
      
    @Value("${spring.datasource.maxActive}")  
    private int maxActive;  
      
    @Value("${spring.datasource.maxWait}")  
    private int maxWait;  
      
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")  
    private int timeBetweenEvictionRunsMillis;  
      
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")  
    private int minEvictableIdleTimeMillis;  
      
    @Value("${postsql.datasource.validationQuery}")  
    private String validationQuery;  
      
    @Value("${spring.datasource.testWhileIdle}")  
    private boolean testWhileIdle;  
      
    @Value("${spring.datasource.testOnBorrow}")  
    private boolean testOnBorrow;  
      
    @Value("${spring.datasource.testOnReturn}")  
    private boolean testOnReturn;  
      
    @Value("${spring.datasource.poolPreparedStatements}")  
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")  
    private int maxPoolPreparedStatementPerConnectionSize;  
      
    @Value("${spring.datasource.filters}")  
    private String filters;  
      
    @Value("${spring.datasource.connectionProperties}")  
    private String connectionProperties;  
      
    @Value("${spring.datasource.useGlobalDataSourceStat}")  
    private boolean useGlobalDataSourceStat;

//    @Value("${spring.datasource.druidLoginName}")
//    private String druidLoginName;

//    @Value("${spring.datasource.druidPassword}")
//    private String druidPassword;
    
    @Value("${spring.datasource.socketTimeout:30000}")  
    private int socketTimeout;
    
    @Value("${spring.datasource.removeAbandoned}")  
    private boolean removeAbandoned;

    @Value("${spring.datasource.removeAbandonedTimeout}")  
    private int removeAbandonedTimeout;
    
    @Autowired
    private SqlPluginInterceptor sqlPluginInterceptor;
    
    @Bean(name = "postsqlDataSource")
    @Primary
    public DataSource postsqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        DBEncrypt encty = new DBEncrypt();
        try {
            //logger.error("url:"+encty.dCode(url));
            //logger.error("user:"+encty.dCode(user));
            //logger.error("password:"+encty.dCode(password));
	        dataSource.setDriverClassName(driverClass);
	        dataSource.setUrl(encty.dCode(url));
	        dataSource.setUsername(encty.dCode(user));
	        dataSource.setPassword(encty.dCode(password));
	
	        dataSource.setInitialSize(initialSize);
	        dataSource.setMinIdle(minIdle);
	        dataSource.setMaxActive(maxActive);
	        dataSource.setMaxWait(maxWait);
	        dataSource.setSocketTimeout(socketTimeout);
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
    
    /** 
     * 注册MyBatis分页插件PageHelper
     */  
    /*@Bean(name = "pageHelper")
    public PageInterceptor pageHelper() {  
    	PageInterceptor pageHelper = new PageInterceptor();  
        Properties p = new Properties();  
        p.setProperty("offsetAsPageNum", "true");  
        p.setProperty("rowBoundsWithCount", "true");  
        p.setProperty("reasonable", "true");  
        pageHelper.setProperties(p);  
        return pageHelper;  
    }*/
    
    @Bean(name = "postsqlTransactionManager")
    @Primary
    public DataSourceTransactionManager postsqlTransactionManager() {
        return new DataSourceTransactionManager(postsqlDataSource());
    }

    @Bean(name = "postsqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory postsqlSqlSessionFactory(@Qualifier("postsqlDataSource") DataSource postsqlDataSource)
            throws Exception {
        final IgamsSqlSessionFactoryBean sessionFactory = new IgamsSqlSessionFactoryBean();
        sessionFactory.setDataSource(postsqlDataSource);
        sessionFactory.setVfs(SpringBootVFS.class);
        sessionFactory.setTypeAliasesPackage(PostDSConfig.ALIAS_PACKAGE);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PostDSConfig.MAPPER_LOCATION));
        //设置拦截器
        //设置 PageHelper 的自动拦截，使用方法为在 正常方法前增加 PageHelper.startPage(1,2);就可以了。 第一个参数是第几页；第二个参数是每页显示条数。
        //Interceptor[] plugins =  new Interceptor[]{(Interceptor) pageHelper()};  
        //sessionFactory.setPlugins(plugins);
        //设置自定义分页拦截器
        sessionFactory.setPlugins(sqlPluginInterceptor);
        //sessionFactory.setPlugins(plugins);
        /*System.out.println("--------------lin-start--------------------");
        Resource[] re =new PathMatchingResourcePatternResolver().getResources(PostDSConfig.MAPPER_LOCATION);
        for(int i=0;i<re.length;i++){
        	System.out.println(re[i]);
        }
        System.out.println("--------------lin-end--------------------");*/
        return sessionFactory.getObject();
    }
}
