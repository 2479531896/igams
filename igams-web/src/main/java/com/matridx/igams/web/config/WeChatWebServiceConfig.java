package com.matridx.igams.web.config;

import javax.xml.ws.Endpoint;

import com.matridx.igams.web.service.svcinterface.ISjxxWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WeChatWebServiceConfig {

    @Autowired
	ISjxxWebService sjxxWebService;
    
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

	@Bean
	public Endpoint endpoint(){
		EndpointImpl endpointImpl = new EndpointImpl(springBus(), sjxxWebService);
		endpointImpl.publish("/sjxxWebService");
		System.out.println("发布sjxxWebService！");
		return endpointImpl;
	}
}
