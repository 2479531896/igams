package com.matridx.igams.web.config.security;

import com.matridx.igams.web.service.svcinterface.IXtyhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class OAuth2Config {
	/**
	 * Spring OAuth2.0提供者实际上分为：
	 * 授权服务 Authorization Service.
	 * 资源服务 Resource Service. 资源服务可以多个
	 * 
	 * 1.访问主页，并且点击一个链接
	 * 2、一个请求被发送给服务器，服务器判断你是否在请求一个受保护的资源
	 * 3、如果你当前没有经过验证，服务器返回一个响应表明你必须要进行验证。响应可以是通过HTTP响应码或者直接重定向到一个特定的网页。
	 * 4、根据验证机制，你的浏览器可能会重定向到一个特定的网页以至于你可以填写表单，或者浏览器会检索你的身份(通过一个基础验证对话框，一个cookie或者X.509证书，等等)。
	 * 5、浏览器给服务器回复一个响应。这可能是一个包含你填充好的表单内容的HTTP POST请求，或者一个包含你的验证信息的HTTP请求头。
	 * 6、下一步服务器会判断当前的验证信息是否是正确的。如果是，可以继续下一步。如果不是，通常你的浏览器会被要求重试(因此你又回到了上两步)。
	 * 7、你的原始的验证过程的请求将会被重试。希望你验证后能被赋予足够的权限来访问受保护的资源。如果是，请求将会成功，否则，你将会获得一个403 HTTP响应码，表示"禁止"。
	 * 
	 * AuthenticationEntryPoint 负责上述列表的第三步
	 * @author 武军
	 *
	 */
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
		@Autowired
		private IgamsAuthenticationEntryPoint igamsAuthenticationEntryPoint;
		
		@Autowired
		private IgamsLogoutSuccessHandler igamsLogoutSuccessHandler;
		
		/**
		 * .authorizeRequests()
		 * .antMatchers("/**").authenticated()
		 * 开放的话,全部拦截，注释掉的话，会直接进入相应地址没有任何验证
		 * 
		 * Order(2)情况下，登录会走验证，其他地址会报401 
		 * Order(6)(3)情况下，全部被拦截走spring的验证登录口了
		 */
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.headers().frameOptions().disable();
	        http
	        .exceptionHandling()
	        .authenticationEntryPoint(igamsAuthenticationEntryPoint)
	        .and()
	        .logout()                          //提供注销支持，当使用 WebSecurityConfigurerAdapter时这将会被自动应用
	        .logoutUrl("/logout")              //触发注销操作的url，默认是/logout。如果开启了CSRF保护(默认开启),那么请求必须是POST方式。
	        //.logoutSuccessUrl("/?logout")        //注销操作发生后重定向到W的url，如果不配置自定义登出配置,默认为 /login?logout。  一般情况下，为了自定义注销功能，你可以添加 LogoutHandler 或者 LogoutSuccessHandler 的实现。
	        .logoutSuccessHandler(igamsLogoutSuccessHandler)   //让你指定自定义的 LogoutSuccessHandler。如果指定了， logoutSuccessUrl() 将会被忽略。重定向地址默认采用 loginPage + "?logout"
	        .permitAll()
	        //.invalidateHttpSession(true)       //指定在注销的时候是否销毁 HttpSession 。默认为True。
	        //.addLogoutHandler(logoutHandler)    //添加一个 LogoutHandler。默认情况下， SecurityContextLogoutHandler 被作为最后一个 LogoutHandler 。                
            //.deleteCookies(cookieNamesToClear)  //允许指定当注销成功时要移除的cookie的名称。这是显式添加 CookieClearingLogoutHandler 的一种快捷处理方式。 
	        .and()
	        .authorizeRequests()
	        .antMatchers("/").permitAll()
	        .antMatchers("/**").authenticated()
	        .and()
	        .formLogin()
	        //.loginProcessingUrl("/login")   //登陆处理路径
	        //.defaultSuccessUrl("/index",true)
			.failureUrl("/?error=true") //登陆失败路径
			.loginPage("/")
			//.usernameParameter("uname")//自定义用户名参数名称
			//.passwordParameter("pwd")//自定义密码参数名称
			; //1 如果没有此行指定，则会使用内置的登陆页面
	    }
		
		@Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            //自定义资源访问认证异常，没有token，或token错误，使用MyAuthenticationEntryPoint 2019-08-14
            resources.authenticationEntryPoint(new IgamsAuthenticationEntryPoint());
        }
	}
	
	/**
	 * Spring OAuth2.0提供者实际上分为：
	 * 授权服务 Authorization Service.
	 * 资源服务 Resource Service. 资源服务可以多个
	 * 
	 * 配置OAuth2验证服务器
	 * EnableAuthorizationServer注解开启验证服务器
	 * 
	 * OAuth2.0 定义了 四种授权模式。分别为：
	 * 授权码模式 （流程最全）
	 * 简化模式  （不推荐）
	 * 密码模式   本次采用的方式
	 * 客户端模式
	 * 
	 * 这个类用于开启OAuth2的验证服务器
	 * @author 武军
	 *
	 */
	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{
/*		private static final String ENV_OAUTH = "authentication.oauth";
		private static final String PROP_CLIENTID = "clientid";
		private static final String PROP_SECRET = "secret";
		private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";*/
		
		@Autowired
		private DataSource postsqlDataSource;
        //主要用于规范异常时返回的信息
        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;
        
        @Autowired
        private IXtyhService xtyhservice;
        
        //@Autowired
        //private AuthorizationServerEndpointsConfiguration configuration;
        
		@Bean
		public TokenStore tokenStore(){
			//使用SpringSecurityOAuth2内定义的JdbcStore来操作数据库中的Token，当然有需要我们可以通过SpringDataJPA自定义Sotre
			//使用该类需要创建两张表，令牌（Access Token会保存到数据库）
			return new JdbcTokenStore(postsqlDataSource);
		}
		
		@Bean
		public AuthorizationServerTokenServices tokenService(){
			MatridxTokenServices tokenService = new MatridxTokenServices();
	        tokenService.setTokenStore(tokenStore());
	        tokenService.setSupportRefreshToken(true);
	        tokenService.setTokenEnhancer(tokenEnhancer());
	        return tokenService;
		}
		
		//可以通过扩展AuthorizationCodeServices来覆写已有的生成授权码规则
		/*@Bean  
	    protected AuthorizationCodeServices authorizationCodeServices() {  
	        return new CustomJdbcAuthorizationCodeServices(dataSource());  
	    }*/
		
		// Spring OAuth2提供了一个操作Token的接口TokenEnhancer，通过实现它可以任意操作accessToken和refreshToken。比如，这里实现将Token中的横线去掉。 
		// 自定义生成令牌 
		@Bean  
	    public TokenEnhancer tokenEnhancer() {
			return new MatridxTokenEnhancer();
			//chain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter,externalAccessTokenInfo));
	    }
		
		@Bean
	    public IgamsClientDetailService igamsClientDetailsService() {
	        return new IgamsClientDetailService();
	    }
		
		//@Autowired
		//@Qualifier("authenticationManagerBean")
		//private AuthenticationManager authenticationManager;
		
        /**
         * 创建授权模式,由于有自定义模式所以只能定义授权模式
         * @param endpoints
         * @return
         */
        private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
            List<TokenGranter> list = new ArrayList<>();
            //添加自定义限制错误次数模式
            list.add(new MatridxTokenGranter(endpoints.getTokenServices(),endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
            //刷新模式
            list.add(new RefreshTokenGranter(endpoints.getTokenServices(),endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
            //简易模式
            list.add(new ImplicitTokenGranter(endpoints.getTokenServices(),endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
            //客户端模式
            list.add(new ClientCredentialsTokenGranter(endpoints.getTokenServices(),endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
            
            return new CompositeTokenGranter(list);
        }

		/**
		 * 所有获取令牌的请求都将会在Spring MVC controller endpoints中进行处理，并且访问受保护
		 * 的资源服务的处理流程将会放在标准的Spring Security请求过滤器中(filters)。
		 * AuthorizationEndpoint：用来作为请求者获得授权的服务，默认的URL是/oauth/authorize.
		 */
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
			//endpoints.tokenStore(tokenStore()).tokenServices(tokenService()).authenticationManager(authenticationManager);
            endpoints.allowedTokenEndpointRequestMethods(HttpMethod.POST);
            endpoints.userDetailsService(xtyhservice); //一定要设置这个不然刷新token的会报错  报错内容:o.s.s.o.provider.endpoint.TokenEndpoint  : Handling error: IllegalStateException, UserDetailsService is required
            endpoints.tokenStore(tokenStore()).tokenServices(tokenService());
            endpoints.tokenGranter(tokenGranter(endpoints));
		}
		
		/**
		 * TokenEndpoint：用来作为请求者获得令牌（Token）的服务，默认的URL是/oauth/token.
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(igamsClientDetailsService());
		}
		
		/**
		 * 允许表单认证
		 */
		@Override		
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
			//oauthServer.allowFormAuthenticationForClients();
            String path = "/oauth/token";
            /*try {
                // 获取自定义映射路径，比如 ((AuthorizationServerEndpointsConfigurer) endpoints).pathMapping("/oauth/token", "/my/token");
                path = configuration.oauth2EndpointHandlerMapping().getServletPath(path);
            } catch (Exception e) {
            }*/
            
            MatridxClientCredentialsTokenEndpointFilter endpointFilter = new MatridxClientCredentialsTokenEndpointFilter(oauthServer,path);
            endpointFilter.afterPropertiesSet();
            endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
            oauthServer.addTokenEndpointAuthenticationFilter(endpointFilter);
            // 注意：security不需要在调用allowFormAuthenticationForClients方法
            oauthServer.authenticationEntryPoint(authenticationEntryPoint)
                    .tokenKeyAccess("isAuthenticated()")
                    .checkTokenAccess("permitAll()");
		}
	}
	
	/**
	 * 自定义授权码规则生成器
	 * @author linwu
	 *
	 */
	/*public class CustomJdbcAuthorizationCodeServices extends JdbcAuthorizationCodeServices {  
		  
	    private RandomValueStringGenerator generator = new RandomValueStringGenerator();  
	      
	    public CustomJdbcAuthorizationCodeServices(DataSource dataSource) {  
	        super(dataSource);  
	        this.generator = new RandomValueStringGenerator(32);  
	    }  
	      
	    public String createAuthorizationCode(OAuth2Authentication authentication) {  
	        String code = this.generator.generate();  
	        store(code, authentication);  
	        return code;  
	    }  
	  
	}*/
	
	/**
	 * 自定义生成令牌
	 * @author linwu
	 *
	 */
	/*public class CustomTokenEnhancer implements TokenEnhancer {  
		  
	    @Override  
	    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {  
	        if (accessToken instanceof DefaultOAuth2AccessToken) {  
	            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);  
	            token.setValue(getNewToken());  
	              
	            OAuth2RefreshToken refreshToken = token.getRefreshToken();  
	            if (refreshToken instanceof DefaultOAuth2RefreshToken) {  
	                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));  
	            }  
	              
	            Map<String, Object> additionalInformation = new HashMap<String, Object>();  
	            additionalInformation.put("client_id", authentication.getOAuth2Request().getClientId());  
	            token.setAdditionalInformation(additionalInformation);  
	              
	            return token;  
	        }  
	        return accessToken;  
	    }  
	      
	    private String getNewToken() {  
	        return UUID.randomUUID().toString().replace("-", "");  
	    }  
	  
	}  
	*/
}
