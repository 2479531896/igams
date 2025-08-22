package com.matridx.igams.sample.config.ds;


//@Configuration
//@MapperScan(basePackages=SampleDataSourceConfig.PACKAGE,sqlSessionFactoryRef="postsqlSqlSessionFactory")
public class SampleDSConfig {
	
	/*private Logger logger = LoggerFactory.getLogger(SampleDSConfig.class);
	
	public static final String PACKAGE = "com.matridx.igams.sample.dao.post";
	public static final String ALIAS_PACKAGE = "com.matridx.igams.sample.dao.entities";
	public static final String MAPPER_LOCATION = "classpath:com/matridx/igams/sample/dao/mapper/*.xml";
	
	@Value("${postsql.datasource.url}")
	private String url;
	
	@Value("${postsql.datasource.username}")
    private String user;
    
    @Value("${postsql.datasource.password}")
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
      
    @Value("${spring.datasource.druidLoginName}")  
    private String druidLoginName;  
      
    @Value("${spring.datasource.druidPassword}")  
    private String druidPassword;
    
    @Value("${spring.datasource.removeAbandoned}")  
    private boolean removeAbandoned;

    @Value("${spring.datasource.removeAbandonedTimeout}")  
    private int removeAbandonedTimeout;
    
    //@Bean(name = "postsqlDataSource")
    public DataSource postsqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        DBEncrypt encty = new DBEncrypt();
        try {
	        dataSource.setDriverClassName(driverClass);
	        dataSource.setUrl(encty.dCode(url.getBytes()));
	        dataSource.setUsername(encty.dCode(user.getBytes()));
	        dataSource.setPassword(encty.dCode(password.getBytes()));
	
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
    
    //@Bean(name = "postsqlTransactionManager")
    public DataSourceTransactionManager postsqlTransactionManager() {
        return new DataSourceTransactionManager(postsqlDataSource());
    }

    //@Bean(name = "postsqlSqlSessionFactory")
    public SqlSessionFactory postsqlSqlSessionFactory(@Qualifier("postsqlDataSource") DataSource postsqlDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(postsqlDataSource);
        sessionFactory.setTypeAliasesPackage(SampleDSConfig.ALIAS_PACKAGE);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(SampleDSConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }*/
}
