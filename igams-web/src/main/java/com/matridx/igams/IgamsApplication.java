package com.matridx.igams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//开启事务
@EnableTransactionManagement
@EnableCaching
@ServletComponentScan
//开启异步
@EnableAsync
//计划任务
@EnableScheduling
//扫描其他包
@ComponentScan(basePackages= {"com.matridx","com.matridxcomm"})
//开启路由功能
//@EnableZuulProxy

@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class IgamsApplication {
	/**
	 * 应用程序主入口
	 * @param args 命令行参数
	 */
	public static void main(String[] args){
		SpringApplication.run(IgamsApplication.class, args);
        System.setProperty("mail.mime.splitlongparameters","false");
        System.getProperties().setProperty("mail.mime.splitlongparameters","false");
        System.setProperty("mail.mime.charset","UTF-8");
        System.setProperty("file.encoding","UTF-8");
	}

	/**
	 * 创建并配置RestTemplate Bean
	 * 使用HttpComponentsClientHttpRequestFactory实现连接池和超时管理
	 * @return 配置好的RestTemplate实例
	 */
	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		//默认情况下,RestTemplate使用SimpleClientHttpRequestFactory,它依赖于HttpURLConnection的默认配置.
		//HttpComponentsClientHttpRequestFactory有连接池配置 对超时进行管理
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();  
		return new RestTemplate(requestFactory);
	}
}
