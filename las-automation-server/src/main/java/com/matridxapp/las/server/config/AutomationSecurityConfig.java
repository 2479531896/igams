package com.matridxapp.las.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AutomationSecurityConfig {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer(){
		return (web) -> web.ignoring().antMatchers("/image/**", "/js/**", "/autows/**", "/css/**");
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
		//http.headers().frameOptions().sameOrigin();
		//http.antMatcher("/**")
		//.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
	
		http.authorizeRequests()
			.antMatchers("/oauth/**","/autows/**").permitAll()
		    .anyRequest().authenticated()
			.and()
			.cors()
			.and()
			.csrf().disable();
		http.headers().frameOptions().disable();
		
		return http.build();
	}
	
}
