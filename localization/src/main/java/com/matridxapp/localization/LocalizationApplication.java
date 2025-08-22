package com.matridxapp.localization;

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
import com.matridx.las.frame.netty.channel.server.LasNettyTextServer;

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
public class LocalizationApplication {
	
	public static void main(String[] args){
		ConfigurableApplicationContext context = SpringApplication.run(LocalizationApplication.class, args);
		if(args!=null) {
			//存在输入参数
			for(int i = 0;i<args.length;i++) {
				String[] s_params = args[i].split("=");
				if(s_params.length <= 1) {
					continue;
				}
				if("--nettyflg".equals(s_params[0])&&"1".equals(s_params[1])) {
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
			}
		}
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
