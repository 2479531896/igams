package com.matridx.web.config.security;

import com.matridx.web.service.svcinterface.IXtyhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 安全验证时使用
 * 
 * ResourceServerConfiguration 和 SecurityConfiguration上配置的顺序,  
 * SecurityConfiguration一定要在ResourceServerConfiguration 之前，
 * 因为spring实现安全是通过添加过滤器(Filter)来实现的，基本的安全过滤应该在oauth过滤之前, 
 * 所以在SecurityConfiguration设置@Order(2),
 * 在ResourceServerConfiguration上设置@Order(6)
 * 
 * WebSecurityConfigurerAdapter是默认情况下spring security的http配置
 * ResourceServerConfigurerAdapter是默认情况下spring security oauth2的http配置
 * 
 * EnableWebSecurity:禁用boot的默认security配置，配合Configuration 启用自定义配置，需扩展 WebSecurityConfigurerAdapter
 * @author linwu
 *
 */
@Configuration
@EnableWebSecurity
public class IgamsSecurityConfig extends WebSecurityConfigurerAdapter{
	//自定义的UserDetailsService注入
	@Autowired
	private IXtyhService xtyhservice;
	
	//配置匹配用户时密码规则 SCryptPasswordEncoder还要引入其他包，所以暂时先使用 SCryptPasswordEncoder
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public UserDetailsService userDetailsService() {
		return xtyhservice;
	}
	
	//配置全局设置
	//身份验证配置，用于注入自定义身份验证bean和密码校验规则
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		//设置UserDetailsService以及密码规则
		auth.userDetailsService(xtyhservice)
		.passwordEncoder(new BCryptPasswordEncoder());
		//设置自定义授权
		//auth.authenticationProvider(authenticationProvider);
	}
	
	//排除登录页面路径拦截
	//对应Web层面的配置，一般用来配置无需安全检查的路径
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/","/js/**","/css/**","/images/**",
				"/fonts/**","/error","/favicon.ico","/accessDenied",
				"/druid/**","/druid","/**/static/**","/manifest.**",
				"/vendor.**","/app.**","/wechat/**","/common/view/**","/ws/**","/login","/oauth/externaltoken");
	}

    @Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	//开启全局方法拦截
	@EnableGlobalMethodSecurity(prePostEnabled=true,jsr250Enabled=true)
	public static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration{
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return new OAuth2MethodSecurityExpressionHandler();
		}
	}
	
	/**
	 * 允许springboot的 Spring Security进行跨域访问
	 * @return
	 */
	@Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(false);
		corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
	
	/**
	 * 增加允许跨域获取token的 功能
	 */
	@Override
    protected void configure(HttpSecurity http) throws Exception {

            http.requestMatchers().antMatchers(HttpMethod.OPTIONS, "/oauth/**")
                    .and()
                    .cors()
                    .and()
                    .csrf().disable();
    }
}
