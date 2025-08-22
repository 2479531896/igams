package com.matridx.igams.web.config.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 为了添加拦截器
 * @author linwu
 *
 */
@Configuration
public class IgamsInterceptorConfig implements WebMvcConfigurer {
	/**
     * 自己定义的拦截器类
     * @return
     */
    @Bean
	public QxInterceptor qxInterceptor() {
        return new QxInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	// 多个拦截器组成一个拦截器链
    	// addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
    	// 如果interceptor中不注入redis或其他项目可以直接new，否则请使用上面这种方式
        registry.addInterceptor(qxInterceptor()).addPathPatterns("/**").excludePathPatterns("/","/js/**","/css/**","/images/**","/fonts/**","/error","logout","/login");
    }
}
