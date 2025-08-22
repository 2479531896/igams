package com.matridxapp.las.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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

public class LasAutoApplication {
	public static void main(String[] args){
		SpringApplication.run(LasAutoApplication.class, args);
        System.setProperty("mail.mime.splitlongparameters","false");
        System.getProperties().setProperty("mail.mime.splitlongparameters","false");
        System.setProperty("mail.mime.charset","UTF-8");
	}
}
