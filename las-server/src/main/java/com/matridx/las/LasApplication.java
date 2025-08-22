package com.matridx.las;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.matridx.las.netty.channel.server.LasNettyServer;
import com.matridx.las.netty.channel.server.LasNettyTextServer;

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
@ComponentScan("com.matridx")
public class LasApplication {
	public static void main(String[] args) {
		
		ConfigurableApplicationContext context =SpringApplication.run(LasApplication.class, args);
		
		//启动Netty服务器
		/*LasNettyTextServer nettyServer = context.getBean(LasNettyTextServer.class);
		try {
			nettyServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Thread protoThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//启动Netty服务器
				LasNettyServer nettyServer = context.getBean(LasNettyServer.class);
				try {
					nettyServer.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		protoThread.start();
			
		Thread textThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					//启动Netty的文本服务器
					LasNettyTextServer textServer = context.getBean(LasNettyTextServer.class);
					textServer.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});	
		textThread.start();
		
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
