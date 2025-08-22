package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.post.IWbcxDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
@RequestMapping("/initWbcx")
public class WbcxRedisInitController implements ApplicationRunner{
	@Autowired
	private RedisUtil redisUtil;

	@Value("${matridx.systemflg.redisreset:}")
	private String redisreset;
	@Autowired
	IWbcxDao wbcxDao;
	private Logger log = LoggerFactory.getLogger(WbcxRedisInitController.class);

	@RequestMapping("/redis")
	@ResponseBody
	public void wbcxRedisInit() {
		log.error("---------------系统外部程序存储redis执行开始---------------");
    	//清除外部程序的所有键
		redisUtil.del("WbcxInfo");

		List<WbcxDto> dtoList = wbcxDao.getDtoList(new WbcxDto());
		for (WbcxDto wbcxDto : dtoList) {
			redisUtil.hset("WbcxInfo",wbcxDto.getWbcxid(), JSONObject.toJSONString(wbcxDto),-1);
		}
	}

	@PostConstruct
	public void runBefore(){
		log.error("---------------PostConstruct注解下的runBefore---------------");
		if ("1".equals(redisreset) || "".equals(redisreset)) {
			wbcxRedisInit();
		}
	}

	@Override
	public void run(ApplicationArguments args) {
		log.error("---------------ApplicationRunner---------------");
	}
}
