package com.matridxapp.localization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages= {"com.matridx","com.matridxapp.las.server.config"})
public class LocalizationBusinessApplication {
	
	public static void main(String[] args){
		SpringApplication.run(LocalizationBusinessApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
