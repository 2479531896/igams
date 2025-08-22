package com.matridxapp.production;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@ServletComponentScan
//开启异步
@EnableAsync
//计划任务
@EnableScheduling
//扫描其他包
@ComponentScan(basePackages= {"com.matridx","com.matridxapp.production"})
@MapperScan("com.matridx.igams.**.dao")
@EnableEurekaClient
public class ProductionApplication 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(ProductionApplication.class, args);
    }
    
    @Bean
    @LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
    
    @Bean
    public JavaMailSender javaMailSender() { 
          return new JavaMailSenderImpl();
    }
    
    /**
     * 跨域支持
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false); // 允许cookies跨域
        config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许
        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
