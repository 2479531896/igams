package com.matridx.web.config.security;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于添加对http的支持
 * @author lwj
 *
 */
@ConditionalOnProperty(value="server.http.port", matchIfMissing = false)
@Configuration
public class TomcatConfig {
	@Value("${server.http.port:}")
    private String httpPort;
	
	@Value("${server.port:}")
    private String httpsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector connector = createStandardConnector();
        if(connector == null) {
            return null;
        }
        tomcat.addAdditionalTomcatConnectors(connector); // 添加http
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        if(httpPort == null || "".equals(httpPort)) {
            return null;
        }
        connector.setPort(Integer.parseInt(httpPort));
        return connector;
    }

}
