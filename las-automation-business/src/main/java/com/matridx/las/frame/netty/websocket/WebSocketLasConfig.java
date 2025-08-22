package com.matridx.las.frame.netty.websocket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
@Configuration
public class WebSocketLasConfig {
	//ServerEndpointExporter的bean重复加载了，spring加载了一次，独立tomcat又加载了一次
    @Bean
    @ConditionalOnBean(TomcatEmbeddedWebappClassLoader.class)
    public ServerEndpointExporter serverEndpointLasExporter() {
        return new ServerEndpointExporter();
    }
}
