package com.matridx.server.wechat.config;

import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class WechatAllConfig implements CommandLineRunner{
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	IWbcxService wbcxService;
	@Value("${matridx.systemflg.remindflg:}")
	private String remindflg;

	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 定时任务：每隔一小时更新微信token
	 */
	@Scheduled(cron = "0 0 0/1 * * ?") 
	public void refreshToken(){
		DBEncrypt dbEncrypt = new DBEncrypt();
		//获取外部程序列表
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setLx(CharacterEnum.WECHAT.getCode());
		if( !"0".equals(remindflg)){
			List<WbcxDto> wbcxList=wbcxService.getDtoList(wbcxDto);
			if(wbcxList != null && wbcxList.size() > 0){
				for (int i = 0; i < wbcxList.size(); i++) {
					//获取公众号访问token
					WeChatUtils.getToken(restTemplate,"client_credential", wbcxList.get(i).getWbcxid(), dbEncrypt.dCode(wbcxList.get(i).getAppid()), dbEncrypt.dCode(wbcxList.get(i).getSecret()), true, wbcxList.get(i).getWbcxdm(),redisUtil);
				}
			}
		}

	}
	
	/**
	 * 启动完成后自动执行的处理
	 * @param args
	 */
	@Override
	public void run(String... args) {
		// TODO Auto-generated method stub
		DBEncrypt dbEncrypt = new DBEncrypt();
		//获取外部程序列表
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setLx(CharacterEnum.WECHAT.getCode());
		List<WbcxDto> wbcxList=wbcxService.getDtoList(wbcxDto);
		if(!"0".equals(remindflg)){
			if(wbcxList != null && wbcxList.size() > 0){
				for (int i = 0; i < wbcxList.size(); i++) {
					if (!StringUtil.isAnyBlank(wbcxList.get(i).getWbcxid(),wbcxList.get(i).getAppid(), wbcxList.get(i).getSecret(),wbcxList.get(i).getWbcxdm())){
						//获取公众号访问token
						WeChatUtils.getToken(restTemplate,"client_credential", wbcxList.get(i).getWbcxid(),
								dbEncrypt.dCode(wbcxList.get(i).getAppid()), dbEncrypt.dCode(wbcxList.get(i).getSecret()), true, wbcxList.get(i).getWbcxdm(),redisUtil);
					}
				}
			}
		}

	}
}
