package com.matridx.las.frame.connect.config;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasDto;
import com.matridx.las.frame.connect.service.svcinterface.IZyxxLasService;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.ChannelModel;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
public class ResourcesInit implements ApplicationRunner {

	@Autowired
	IZyxxLasService zyxxService;

	@Value("${matridx.netty.area:01}")
	private String netty_area;
	@Value("${matridx.netty.bridgingflg:false}")
	private boolean bridgingflg;

	private Logger log = LoggerFactory.getLogger(ResourcesInit.class);

	//服务配置初始化
	public void resourceInit() {
		List<ZyxxLasDto> zyxxlist=zyxxService.getZyxxInit();
		log.error("---------------服务配置初始化开始---------------");
		if(zyxxlist!=null && !CollectionUtils.isEmpty(zyxxlist)){
			for(ZyxxLasDto zyxxDto:zyxxlist){
				IHttpService Service;
				try {
					ChannelModel channelModel = new ChannelModel();
					channelModel.setProtocol(zyxxDto.getXylx());
					Service = (IHttpService)Class.forName(zyxxDto.getHdl()).getDeclaredConstructor().newInstance();
					Type type = new TypeToken<Map<String,String>>() {}.getType();
					Map<String, Object> map = JSON.parseObject(zyxxDto.getCs(), type);
					map.put("ztqrdz",zyxxDto.getZtqrdz());
					map.put("confirmflg","true");
					map.put("address",zyxxDto.getZydz());
					
					map.put("protocol",zyxxDto.getXylx());
					map.put("netty_area",netty_area);
					map.put("bridgingflg",bridgingflg);
					map.put("deviceid",zyxxDto.getDeviceid());
					if(!CommonChannelUtil.protocols.containsKey(zyxxDto.getXylx())) {
						CommonChannelUtil.addProtocol(zyxxDto.getXylx(), Service);
					}
					//确认设备为可连接状态
					if(Service.init(map)){
						CommonChannelUtil.handleChannel(zyxxDto.getZydz(),Service);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("resourceInit:" + e.getMessage());
				}
			}
		}

		log.error("---------------服务配置初始化结束---------------");
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		resourceInit();
	}

}
