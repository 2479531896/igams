package com.matridx.server.wechat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.matridx.server.wechat.util.WordRecognitionUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Component
public class WordRecognitionConfig implements CommandLineRunner{
	
	@Value("${matridx.baidu.appkey}")
	private String appkey;
	
	@Value("${matridx.baidu.secret}")
	private String secret;
	
	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * 定时任务：每隔一小时更新微信token
	 */
	@Scheduled(cron = "0 0 0 1/16 * ?") 
	public void refreshToken(){
		if(StringUtil.isBlank(appkey)) {
            return;
        }
		DBEncrypt dbEncrypt = new DBEncrypt();
		//获取访问token
		WordRecognitionUtil.getToken(restTemplate,
				dbEncrypt.dCode(appkey), dbEncrypt.dCode(secret),true);
	}
	
	/**
	 * 启动完成后自动执行的处理
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		if(StringUtil.isBlank(appkey)) {
            return;
        }
		DBEncrypt dbEncrypt = new DBEncrypt();
		//获取访问token
		WordRecognitionUtil.getToken(restTemplate,
				dbEncrypt.dCode(appkey), dbEncrypt.dCode(secret),true);
				
	}
}
