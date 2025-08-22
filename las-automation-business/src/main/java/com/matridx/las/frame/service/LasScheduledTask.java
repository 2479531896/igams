package com.matridx.las.frame.service;

import com.alibaba.fastjson.JSON;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.connect.util.ConnectUtil;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class LasScheduledTask {
    // 系统区域设置
    @Value("${matridx.netty.area:01}")
    private String netty_area;
    // 是否桥接
    @Value("${matridx.netty.bridgingflg:false}")
    private boolean bridgingflg;
    
    Logger log = LoggerFactory.getLogger(LasScheduledTask.class);
    
    @Scheduled(cron = ("${matridx.scheduled.syncChannelCron:0 */2 * * * ?}"))
    public void syncAllChannelToMain(){
        if (bridgingflg){
            Map<String, Map<String, List<IHttpService>>> registerChannels = CommonChannelUtil.getRegisterChannels();
            if (MapUtils.isNotEmpty(registerChannels)){
                Map<String, List<IHttpService>> areaMap = registerChannels.get(netty_area);
                if (MapUtils.isNotEmpty(areaMap)){
                	try {
	                    //发送信息到主服务器
	                    Map<String, String> paramMap= new HashMap<>();
	                    paramMap.put("areaMap",JSON.toJSONString(areaMap));
	                    paramMap.put("netty_area",netty_area);
	                    ConnectUtil.syncAllChannelToMain(paramMap);
                	}catch(Exception e) {
                		log.error("分服务器定时执行注册：出现错误--" + e.getMessage());
                	}
                }
            }
        }
    }
}
