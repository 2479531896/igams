package com.matridx.las.netty.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
		
@Configuration
public class NettyRabbitMqConfig {
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	private Logger log = LoggerFactory.getLogger(NettyRabbitMqConfig.class);
	
}
