package com.matridxapp.hrm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 关闭 basic 认证
 * 在springboot 1.x 中，你可以在 application.properties 中添加 security.basic.enabled=false即可。
 * 但是在 springboot 2.x 中，这个配置就不管用了
 * 继承 WebSecurityConfigurerAdapter 类，并重写 configure 方法
 * @author linwu
 *
 */
@Configuration
@EnableWebSecurity
public class HrmSecurityConfig extends WebSecurityConfigurerAdapter{
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
	}
	
	//排除登录页面路径拦截
	//对应Web层面的配置，一般用来配置无需安全检查的路径
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/","/**");
	}
}
