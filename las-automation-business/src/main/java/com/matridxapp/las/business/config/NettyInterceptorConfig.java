package com.matridxapp.las.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.matridx.igams.common.interceptor.QxCloudInterceptor;

/**
 * 为了添加拦截器
 * @author linwu
 *
 */
@Configuration
public class NettyInterceptorConfig implements WebMvcConfigurer{
	/**
	 * 为了添加拦截器
	 * @author linwu
	 *
	 */
	@Bean
	public QxCloudInterceptor qxCloudInterceptor() {
        return new QxCloudInterceptor();
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
        registry.addInterceptor(qxCloudInterceptor()).addPathPatterns("/**").excludePathPatterns("/","/js/**","/css/**","/images/**","/fonts/**","/error","logout");
    }
}
